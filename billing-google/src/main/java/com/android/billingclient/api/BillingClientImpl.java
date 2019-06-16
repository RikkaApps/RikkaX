/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement"). By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */
package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.WorkerThread;

import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.api.SkuDetails.SkuDetailsResult;
import com.android.billingclient.util.BillingHelper;
import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.android.billingclient.api.PurchaseApiResponseChecker.checkPurchasesBundleValidity;
import static com.android.billingclient.util.BillingHelper.INAPP_CONTINUATION_TOKEN;
import static com.android.billingclient.util.BillingHelper.RESPONSE_BUY_INTENT_KEY;
import static com.android.billingclient.util.BillingHelper.RESPONSE_CODE;
import static com.android.billingclient.util.BillingHelper.RESPONSE_SUBS_MANAGEMENT_INTENT_KEY;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Implementation of {@link BillingClient} for communication between the in-app billing library and
 * client's application code.
 */
class BillingClientImpl extends BillingClient {
  private static final String TAG = "BillingClient";

  /**
   * The maximum waiting time in millisecond for Play in-app billing synchronous service call. The
   * call is blocking UI thread so we set to 5 seconds to avoid ANRs.
   */
  private static final long SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS = 5_000L;

  /** The maximum waiting time in millisecond for Play in-app billing asynchronous service call. */
  private static final long ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS = 30_000L;

  /**
   * The maximum number of items than can be requested by a call to Billing service's
   * getSkuDetails() method
   */
  private static final int MAX_SKU_DETAILS_ITEMS_PER_REQUEST = 20;

  /** Possible client/billing service relationship states. */
  @IntDef({
    ClientState.DISCONNECTED,
    ClientState.CONNECTING,
    ClientState.CONNECTED,
    ClientState.CLOSED
  })
  @Retention(SOURCE)
  public @interface ClientState {
    /** This client was not yet connected to billing service or was already disconnected from it. */
    int DISCONNECTED = 0;
    /** This client is currently in process of connecting to billing service. */
    int CONNECTING = 1;
    /** This client is currently connected to billing service. */
    int CONNECTED = 2;
    /** This client was already closed and shouldn't be used again. */
    int CLOSED = 3;
  }

  private @ClientState int mClientState = ClientState.DISCONNECTED;

  /** A list of SKUs inside getSkuDetails request bundle. */
  private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";

  /** Maximum IAP version currently supported. */
  private static final int MAX_IAP_VERSION = 9;

  /** Minimum IAP version currently supported. */
  private static final int MIN_IAP_VERSION = 3;

  /** Version name of this library. */
  private final String mQualifiedVersionNumber;

  /** Main (UI) thread handler to post results from Executor. */
  private final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());

  /**
   * Wrapper on top of PURCHASES_UPDATED broadcast receiver to return all purchases receipts to the
   * developer in one place for both app initiated and Play Store initated purhases.
   */
  private final BillingBroadcastManager mBroadcastManager;

  /** Context of the application that initialized this client. */
  private final Context mApplicationContext;

  /** Whether this client is for child directed use. This is mainly used for rewarded skus. */
  @ChildDirected private final int mChildDirected;

  /** Whether this client is for under of age consent use. This is mainly used for rewarded skus. */
  @UnderAgeOfConsent private final int mUnderAgeOfConsent;

  /** Service binder */
  private IInAppBillingService mService;

  /** Connection to the service. */
  private BillingServiceConnection mServiceConnection;

  /** If subscriptions are is supported (for billing v3 and higher) or not. */
  private boolean mSubscriptionsSupported;

  /** If subscription update is supported (for billing v5 and higher) or not. */
  private boolean mSubscriptionUpdateSupported;

  /**
   * If purchaseHistory and buyIntentExtraParams are supported (for billing v6 and higher) or not.
   */
  private boolean mIABv6Supported;

  /** Indicates if IAB v8 or higher is supported. */
  private boolean mIABv8Supported;

  /** If getPurchasesExtraParams is supported ( for billing v9 and higher) or not. */
  private boolean mIABv9Supported;

  /**
   * If pending purchases are supported for purchases made using this client and purchases returned
   * to this client.
   */
  private final boolean mEnablePendingPurchases;

  /**
   * Service that helps us to keep a pool of background threads suitable for current device specs.
   */
  private ExecutorService mExecutorService;

  @VisibleForTesting
  void setExecutorService(ExecutorService executorService) {
    mExecutorService = executorService;
  }

  /** This receiver is triggered by {@link ProxyBillingActivity}. */
  private final ResultReceiver onPurchaseFinishedReceiver =
      new ResultReceiver(mUiThreadHandler) {
        @Override
        public void onReceiveResult(@BillingResponseCode int resultCode, Bundle resultData) {
          PurchasesUpdatedListener purchasesUpdatedListener = mBroadcastManager.getListener();
          if (purchasesUpdatedListener == null) {
            BillingHelper.logWarn(
                TAG, "PurchasesUpdatedListener is null - no way to return the response.");
            return;
          }
          List<Purchase> purchases = BillingHelper.extractPurchases(resultData);
          BillingResult billingResult =
              BillingResult.newBuilder()
                  .setResponseCode(resultCode)
                  .setDebugMessage(BillingHelper.getDebugMessageFromBundle(resultData, TAG))
                  .build();
          purchasesUpdatedListener.onPurchasesUpdated(billingResult, purchases);
        }
      };

  @UiThread
  BillingClientImpl(
      @NonNull Context context,
      @ChildDirected int childDirected,
      @UnderAgeOfConsent int underAgeOfConsent,
      boolean enablePendingPurchases,
      @NonNull PurchasesUpdatedListener listener) {
    this(
        context,
        childDirected,
        underAgeOfConsent,
        enablePendingPurchases,
        listener,
        "1.2.1");
  }

  // Used by C++.
  // TODO (b/132100367): allow C++ PBL to get version name from gradle directly.
  private BillingClientImpl(
      Activity activity,
      int childDirected,
      int underAgeOfConsent,
      boolean enablePendingPurchases,
      String versionOverride) {
    this(
        activity.getApplicationContext(),
        childDirected,
        underAgeOfConsent,
        enablePendingPurchases,
        new BillingClientNativeCallback(),
        versionOverride);
  }

  private BillingClientImpl(
      @NonNull Context context,
      @ChildDirected int childDirected,
      @UnderAgeOfConsent int underAgeOfConsent,
      boolean enablePendingPurchases,
      @NonNull PurchasesUpdatedListener listener,
      String versionNumber) {
    mApplicationContext = context.getApplicationContext();
    mChildDirected = childDirected;
    mUnderAgeOfConsent = underAgeOfConsent;
    mEnablePendingPurchases = enablePendingPurchases;
    mBroadcastManager = new BillingBroadcastManager(mApplicationContext, listener);
    mQualifiedVersionNumber = versionNumber;
  }

  @Override
  public BillingResult isFeatureSupported(@FeatureType String feature) {
    if (!isReady()) {
      return BillingResults.SERVICE_DISCONNECTED;
    }

    switch (feature) {
      case FeatureType.SUBSCRIPTIONS:
        return mSubscriptionsSupported ? BillingResults.OK : BillingResults.FEATURE_NOT_SUPPORTED;

      case FeatureType.SUBSCRIPTIONS_UPDATE:
        return mSubscriptionUpdateSupported
            ? BillingResults.OK
            : BillingResults.FEATURE_NOT_SUPPORTED;

      case FeatureType.IN_APP_ITEMS_ON_VR:
        return isBillingSupportedOnVr(SkuType.INAPP);

      case FeatureType.SUBSCRIPTIONS_ON_VR:
        return isBillingSupportedOnVr(SkuType.SUBS);

      case FeatureType.PRICE_CHANGE_CONFIRMATION:
        return mIABv8Supported ? BillingResults.OK : BillingResults.FEATURE_NOT_SUPPORTED;

      default:
        BillingHelper.logWarn(TAG, "Unsupported feature: " + feature);
        return BillingResults.UNKNOWN_FEATURE;
    }
  }

  @Override
  public boolean isReady() {
    return mClientState == ClientState.CONNECTED && mService != null && mServiceConnection != null;
  }

  @Override
  public void startConnection(@NonNull BillingClientStateListener listener) {
    if (isReady()) {
      BillingHelper.logVerbose(TAG, "Service connection is valid. No need to re-initialize.");
      listener.onBillingSetupFinished(BillingResults.OK);
      return;
    }

    if (mClientState == ClientState.CONNECTING) {
      BillingHelper.logWarn(
          TAG, "Client is already in the process of connecting to billing service.");
      listener.onBillingSetupFinished(BillingResults.CLIENT_CONNECTING);
      return;
    }

    if (mClientState == ClientState.CLOSED) {
      BillingHelper.logWarn(
          TAG, "Client was already closed and can't be reused. Please create another instance.");
      listener.onBillingSetupFinished(BillingResults.SERVICE_DISCONNECTED);
      return;
    }

    // Switch current state to connecting to avoid race conditions
    mClientState = ClientState.CONNECTING;

    // Start listening for asynchronous purchase results via PURCHASES_UPDATED broadcasts
    mBroadcastManager.registerReceiver();

    // Connection to billing service
    BillingHelper.logVerbose(TAG, "Starting in-app billing setup.");
    mServiceConnection = new BillingServiceConnection(listener);

    Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
    serviceIntent.setPackage("com.android.vending");
    List<ResolveInfo> intentServices =
        mApplicationContext.getPackageManager().queryIntentServices(serviceIntent, 0);

    if (intentServices != null && !intentServices.isEmpty()) {
      // Get component info and create ComponentName
      ResolveInfo resolveInfo = intentServices.get(0);
      if (resolveInfo.serviceInfo != null) {
        String packageName = resolveInfo.serviceInfo.packageName;
        String className = resolveInfo.serviceInfo.name;
        if ("com.android.vending".equals(packageName) && className != null) {
          ComponentName component = new ComponentName(packageName, className);
          // Specify component explicitly and don't allow stripping or replacing the package name
          // to avoid exceptions inside 3rd party apps when Play Store was hacked:
          // "IllegalArgumentException: Service Intent must be explicit".
          // See: https://github.com/googlesamples/android-play-billing/issues/62 for more context.
          Intent explicitServiceIntent = new Intent(serviceIntent);
          explicitServiceIntent.setComponent(component);
          explicitServiceIntent.putExtra(
              BillingHelper.LIBRARY_VERSION_KEY, mQualifiedVersionNumber);
          boolean connectionResult =
              mApplicationContext.bindService(
                  explicitServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
          if (connectionResult) {
            // Service connected successfully, listener will be called from mServiceConnection
            BillingHelper.logVerbose(TAG, "Service was bonded successfully.");
            return;
          } else {
            // Service connection was blocked (e.g. this could happen in China), so we are closing
            // the connection and notifying the listener
            BillingHelper.logWarn(TAG, "Connection to Billing service is blocked.");
          }
        } else {
          // Play Store package name is not valid, ending connection
          BillingHelper.logWarn(TAG, "The device doesn't have valid Play Store.");
        }
      }
    }
    // No service available to handle that Intent or service connection was blocked
    mClientState = ClientState.DISCONNECTED;
    BillingHelper.logVerbose(TAG, "Billing service unavailable on device.");
    listener.onBillingSetupFinished(BillingResults.BILLING_UNAVAILABLE);
  }

  // Used by C++.
  private void startConnection(long futureHandle) {
    startConnection(new BillingClientNativeCallback(futureHandle));
  }

  @Override
  public void endConnection() {
    try {
      mBroadcastManager.destroy();
      if (mServiceConnection != null) {
        mServiceConnection.markDisconnectedAndCleanUp();
      }
      if (mServiceConnection != null && mService != null) {
        BillingHelper.logVerbose(TAG, "Unbinding from service.");
        mApplicationContext.unbindService(mServiceConnection);
        mServiceConnection = null;
      }
      mService = null;
      if (mExecutorService != null) {
        mExecutorService.shutdownNow();
        mExecutorService = null;
      }
      // TODO(b/128430724): Figure out better exception catching.
    } catch (Exception ex) {
      // TODO(b/128430840): Use better logging method instead of string concatentation.
      BillingHelper.logWarn(TAG, "There was an exception while ending connection: " + ex);
    } finally {
      mClientState = ClientState.CLOSED;
    }
  }

  // Used for C++.
  private void launchPriceChangeConfirmationFlow(
      Activity activity, PriceChangeFlowParams priceChangeFlowParams, final long futureHandle) {
    launchPriceChangeConfirmationFlow(
        activity, priceChangeFlowParams, new BillingClientNativeCallback(futureHandle));
  }

  @Override
  public void launchPriceChangeConfirmationFlow(
      Activity activity,
      PriceChangeFlowParams priceChangeFlowParams,
      @NonNull final PriceChangeConfirmationListener listener) {
    if (!isReady()) {
      listener.onPriceChangeConfirmationResult(BillingResults.SERVICE_DISCONNECTED);
      return;
    }
    if (priceChangeFlowParams == null || priceChangeFlowParams.getSkuDetails() == null) {
      BillingHelper.logWarn(
          TAG, "Please fix the input params. priceChangeFlowParams must contain valid sku.");
      listener.onPriceChangeConfirmationResult(BillingResults.NULL_SKU);
      return;
    }
    final String sku = priceChangeFlowParams.getSkuDetails().getSku();
    if (sku == null) {
      BillingHelper.logWarn(
          TAG, "Please fix the input params. priceChangeFlowParams must contain valid sku.");
      listener.onPriceChangeConfirmationResult(BillingResults.NULL_SKU);
      return;
    }
    if (!mIABv8Supported) {
      BillingHelper.logWarn(TAG, "Current client doesn't support price change confirmation flow.");
      listener.onPriceChangeConfirmationResult(BillingResults.FEATURE_NOT_SUPPORTED);
      return;
    }

    Bundle extraParams = new Bundle();
    extraParams.putString(BillingHelper.LIBRARY_VERSION_KEY, mQualifiedVersionNumber);
    extraParams.putBoolean(BillingHelper.EXTRA_PARAM_KEY_SUBS_PRICE_CHANGE, true);
    final Bundle extraParamsFinal = extraParams;

    Future<Bundle> futurePriceChangeIntentBundle =
        executeAsync(
            new Callable<Bundle>() {
              @Override
              public Bundle call() throws Exception {
                return mService.getSubscriptionManagementIntent(
                    /* apiVersion= */ 8,
                    mApplicationContext.getPackageName(),
                    sku,
                    SkuType.SUBS,
                    extraParamsFinal);
              }
            },
            SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            /* onTimeout= */ null);

    try {
      Bundle priceChangeIntentBundle =
          futurePriceChangeIntentBundle.get(
              SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);

      int responseCode = BillingHelper.getResponseCodeFromBundle(priceChangeIntentBundle, TAG);
      String debugMessage = BillingHelper.getDebugMessageFromBundle(priceChangeIntentBundle, TAG);
      BillingResult billingResult =
          BillingResult.newBuilder()
              .setResponseCode(responseCode)
              .setDebugMessage(debugMessage)
              .build();
      if (responseCode != BillingResponseCode.OK) {
        BillingHelper.logWarn(
            TAG, "Unable to launch price change flow, error response code: " + responseCode);
        listener.onPriceChangeConfirmationResult(billingResult);
        return;
      }

      final ResultReceiver onPriceChangeConfirmationReceiver =
          new ResultReceiver(mUiThreadHandler) {
            @Override
            public void onReceiveResult(@BillingResponseCode int resultCode, Bundle resultData) {
              BillingResult billingResult =
                  BillingResult.newBuilder()
                      .setResponseCode(resultCode)
                      .setDebugMessage(BillingHelper.getDebugMessageFromBundle(resultData, TAG))
                      .build();
              // Receiving the result from local broadcast and triggering a callback on listener.
              listener.onPriceChangeConfirmationResult(billingResult);
            }
          };

      // Launching an invisible activity that will handle the price change flow
      Intent intent = new Intent(activity, ProxyBillingActivity.class);
      PendingIntent pendingIntent =
          priceChangeIntentBundle.getParcelable(RESPONSE_SUBS_MANAGEMENT_INTENT_KEY);
      intent.putExtra(RESPONSE_SUBS_MANAGEMENT_INTENT_KEY, pendingIntent);
      intent.putExtra(ProxyBillingActivity.KEY_RESULT_RECEIVER, onPriceChangeConfirmationReceiver);
      // We need an activity reference here to avoid using FLAG_ACTIVITY_NEW_TASK.
      // But we don't want to keep a reference to it inside the field to avoid memory leaks.
      // Plus all the other methods need just a Context reference, so could be used from the
      // Service or Application.
      activity.startActivity(intent);
    } catch (TimeoutException | CancellationException ex) {
      String msg =
          "Time out while launching Price Change Flow for sku: " + sku + "; try to reconnect";
      BillingHelper.logWarn(TAG, msg);
      listener.onPriceChangeConfirmationResult(BillingResults.SERVICE_TIMEOUT);
    } catch (Exception ex) {
      String msg =
          "Exception caught while launching Price Change Flow for sku: "
              + sku
              + "; try to reconnect";
      BillingHelper.logWarn(TAG, msg);
      listener.onPriceChangeConfirmationResult(BillingResults.SERVICE_DISCONNECTED);
    }
  }

  @Override
  public BillingResult launchBillingFlow(Activity activity, final BillingFlowParams params) {
    if (!isReady()) {
      return broadcastFailureAndReturnBillingResponse(BillingResults.SERVICE_DISCONNECTED);
    }

    final @SkuType String skuType = params.getSkuType();
    final String newSku = params.getSku();
    final SkuDetails skuDetails = params.getSkuDetails();
    final boolean rewardedSku = skuDetails != null && skuDetails.isRewarded();

    // Checking for mandatory params fields
    if (newSku == null) {
      BillingHelper.logWarn(TAG, "Please fix the input params. SKU can't be null.");
      return broadcastFailureAndReturnBillingResponse(BillingResults.NULL_SKU);
    }

    if (skuType == null) {
      BillingHelper.logWarn(TAG, "Please fix the input params. SkuType can't be null.");
      return broadcastFailureAndReturnBillingResponse(BillingResults.NULL_SKU_TYPE);
    }

    // Checking for requested features support
    if (skuType.equals(SkuType.SUBS) && !mSubscriptionsSupported) {
      BillingHelper.logWarn(TAG, "Current client doesn't support subscriptions.");
      return broadcastFailureAndReturnBillingResponse(BillingResults.SUBSCRIPTIONS_NOT_SUPPORTED);
    }

    boolean isSubscriptionUpdate = (params.getOldSku() != null);

    if (isSubscriptionUpdate && !mSubscriptionUpdateSupported) {
      BillingHelper.logWarn(TAG, "Current client doesn't support subscriptions update.");
      return broadcastFailureAndReturnBillingResponse(
          BillingResults.SUBSCRIPTIONS_UPDATE_NOT_SUPPORTED);
    }

    if (params.hasExtraParams() && !mIABv6Supported) {
      BillingHelper.logWarn(TAG, "Current client doesn't support extra params for buy intent.");
      return broadcastFailureAndReturnBillingResponse(BillingResults.EXTRA_PARAMS_NOT_SUPPORTED);
    }

    if (rewardedSku && !mIABv6Supported) {
      BillingHelper.logWarn(TAG, "Current client doesn't support extra params for buy intent.");
      return broadcastFailureAndReturnBillingResponse(BillingResults.EXTRA_PARAMS_NOT_SUPPORTED);
    }

    BillingHelper.logVerbose(
        TAG, "Constructing buy intent for " + newSku + ", item type: " + skuType);

    Future<Bundle> futureBuyIntentBundle;
    // If IAB v6 is supported, we always try to use buyIntentExtraParams and report the version
    if (mIABv6Supported) {
      Bundle extraParams =
          BillingHelper.constructExtraParamsForLaunchBillingFlow(
              params, mIABv9Supported, mEnablePendingPurchases, mQualifiedVersionNumber);

      // ---- modified start ----
      String token = skuDetails != null ? skuDetails.getSkuDetailsToken() : null;
      if (!TextUtils.isEmpty(token)) {
        extraParams.putString(
            BillingHelper.EXTRA_PARAM_KEY_SKU_DETAILS_TOKEN, token);
      }
      // ---- modified end ----

      if (rewardedSku) {
        extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_RSKU, skuDetails.rewardToken());
        if (mChildDirected != ChildDirected.UNSPECIFIED) {
          extraParams.putInt(BillingFlowParams.EXTRA_PARAM_CHILD_DIRECTED, mChildDirected);
        }
        if (mUnderAgeOfConsent != UnderAgeOfConsent.UNSPECIFIED) {
          extraParams.putInt(
              BillingFlowParams.EXTRA_PARAM_UNDER_AGE_OF_CONSENT, mUnderAgeOfConsent);
        }
      }
      final Bundle extraParamsFinal = extraParams;
      int apiVersion = 6;
      if (mIABv9Supported) {
        apiVersion = 9;
      } else if (params.getVrPurchaseFlow()) {
        apiVersion = 7;
      }
      final int finalApiVersion = apiVersion;
      futureBuyIntentBundle =
          executeAsync(
              new Callable<Bundle>() {
                @Override
                public Bundle call() throws Exception {
                  return mService.getBuyIntentExtraParams(
                      finalApiVersion,
                      mApplicationContext.getPackageName(),
                      newSku,
                      skuType,
                      null,
                      extraParamsFinal);
                }
              },
              SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
              /* onTimeout= */ null);
    } else if (isSubscriptionUpdate) {
      // For subscriptions update we are calling corresponding service method
      futureBuyIntentBundle =
          executeAsync(
              new Callable<Bundle>() {
                @Override
                public Bundle call() throws Exception {
                  return mService.getBuyIntentToReplaceSkus(
                      /* apiVersion */ 5,
                      mApplicationContext.getPackageName(),
                      Arrays.asList(params.getOldSku()),
                      newSku,
                      SkuType.SUBS,
                      /* developerPayload */ null);
                }
              },
              SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
              /* onTimeout= */ null);
    } else {
      futureBuyIntentBundle =
          executeAsync(
              new Callable<Bundle>() {
                @Override
                public Bundle call() throws Exception {
                  return mService.getBuyIntent(
                      /* apiVersion */ 3,
                      mApplicationContext.getPackageName(),
                      newSku,
                      skuType,
                      /* developerPayload */ null);
                }
              },
              SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
              /* onTimeout= */ null);
    }
    try {
      Bundle buyIntentBundle =
          futureBuyIntentBundle.get(SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
      int responseCode = BillingHelper.getResponseCodeFromBundle(buyIntentBundle, TAG);
      String debugMessage = BillingHelper.getDebugMessageFromBundle(buyIntentBundle, TAG);
      if (responseCode != BillingResponseCode.OK) {
        BillingHelper.logWarn(TAG, "Unable to buy item, Error response code: " + responseCode);
        BillingResult billingResult =
            BillingResult.newBuilder()
                .setResponseCode(responseCode)
                .setDebugMessage(debugMessage)
                .build();
        return broadcastFailureAndReturnBillingResponse(billingResult);
      }
      // Launching an invisible activity that will handle the purchase result
      Intent intent = new Intent(activity, ProxyBillingActivity.class);
      intent.putExtra(ProxyBillingActivity.KEY_RESULT_RECEIVER, onPurchaseFinishedReceiver);
      PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT_KEY);
      intent.putExtra(RESPONSE_BUY_INTENT_KEY, pendingIntent);
      // We need an activity reference here to avoid using FLAG_ACTIVITY_NEW_TASK.
      // But we don't want to keep a reference to it inside the field to avoid memory leaks.
      // Plus all the other methods need just a Context reference, so could be used from the
      // Service or Application.
      activity.startActivity(intent);
    } catch (TimeoutException | CancellationException ex) {
      String msg =
          "Time out while launching billing flow: " + "; for sku: " + newSku + "; try to reconnect";
      BillingHelper.logWarn(TAG, msg);
      return broadcastFailureAndReturnBillingResponse(BillingResults.SERVICE_TIMEOUT);
    } catch (Exception ex) {
      String msg =
          "Exception while launching billing flow: "
              + "; for sku: "
              + newSku
              + "; try to reconnect";
      BillingHelper.logWarn(TAG, msg);
      return broadcastFailureAndReturnBillingResponse(BillingResults.SERVICE_DISCONNECTED);
    }

    return BillingResults.OK;
  }

  private BillingResult broadcastFailureAndReturnBillingResponse(BillingResult billingResult) {
    mBroadcastManager.getListener().onPurchasesUpdated(billingResult, /* List<Purchase>= */ null);
    return billingResult;
  }

  private int launchBillingFlowCpp(Activity activity, final BillingFlowParams params) {
    return launchBillingFlow(activity, params).getResponseCode();
  }

  @Override
  public PurchasesResult queryPurchases(final @SkuType String skuType) {
    if (!isReady()) {
      return new PurchasesResult(BillingResults.SERVICE_DISCONNECTED, /* purchasesList */ null);
    }

    // Checking for the mandatory argument
    if (TextUtils.isEmpty(skuType)) {
      BillingHelper.logWarn(TAG, "Please provide a valid SKU type.");
      return new PurchasesResult(BillingResults.EMPTY_SKU_TYPE, /* purchasesList */ null);
    }

    Future<PurchasesResult> futurePurchaseResult =
        executeAsync(
            new Callable<PurchasesResult>() {
              @Override
              public PurchasesResult call() throws Exception {
                return queryPurchasesInternal(skuType);
              }
            },
            SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            /* onTimeout= */ null);
    try {
      return futurePurchaseResult.get(SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException | CancellationException ex) {
      return new PurchasesResult(BillingResults.SERVICE_TIMEOUT, /* purchasesList */ null);
    } catch (Exception ex) {
      return new PurchasesResult(BillingResults.INTERNAL_ERROR, /* purchasesList */ null);
    }
  }

  // Used for C++.
  private void queryPurchases(final String skuType, final long futureHandle) {
    final BillingClientNativeCallback callback = new BillingClientNativeCallback(futureHandle);
    if (!isReady()) {
      callback.onQueryPurchasesResponse(
          BillingResults.SERVICE_DISCONNECTED, /* purchasesList */ null);
    }
    Future result =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                final PurchasesResult result = queryPurchasesInternal(skuType);

                // Post the result to main thread
                postToUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        callback.onQueryPurchasesResponse(
                            result.getBillingResult(), result.getPurchasesList());
                      }
                    });
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                callback.onQueryPurchasesResponse(
                    BillingResults.SERVICE_TIMEOUT, /* purchases= */ null);
              }
            });
    if (result == null) {
      callback.onQueryPurchasesResponse(
          getBillingResultForNullFutureResult(), /* purchasesList */ null);
    }
  }

  @Override
  public void querySkuDetailsAsync(
      SkuDetailsParams params, final SkuDetailsResponseListener listener) {
    if (!isReady()) {
      listener.onSkuDetailsResponse(BillingResults.SERVICE_DISCONNECTED, /* skuDetailsList */ null);
      return;
    }

    final @SkuType String skuType = params.getSkuType();
    final List<String> skusList = params.getSkusList();

    // Checking for mandatory params fields
    if (TextUtils.isEmpty(skuType)) {
      String msg = "Please fix the input params. SKU type can't be empty.";
      BillingHelper.logWarn(TAG, msg);
      listener.onSkuDetailsResponse(BillingResults.EMPTY_SKU_TYPE, /* skuDetailsList */ null);
      return;
    }

    if (skusList == null) {
      String msg = "Please fix the input params. The list of SKUs can't be empty.";
      BillingHelper.logWarn(TAG, msg);
      listener.onSkuDetailsResponse(BillingResults.EMPTY_SKU_LIST, /* skuDetailsList */ null);
      return;
    }

    Future result =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                final SkuDetailsResult result = querySkuDetailsInternal(skuType, skusList);
                // Post the result to main thread
                postToUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        listener.onSkuDetailsResponse(
                            BillingResult.newBuilder()
                                .setResponseCode(result.getResponseCode())
                                .setDebugMessage(result.getDebugMessage())
                                .build(),
                            result.getSkuDetailsList());
                      }
                    });
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                listener.onSkuDetailsResponse(
                    BillingResults.SERVICE_TIMEOUT, /* skuDetailsList */ null);
              }
            });
    if (result == null) {
      listener.onSkuDetailsResponse(
          getBillingResultForNullFutureResult(), /* skuDetailsList */ null);
    }
  }

  // Used for C++.
  private void querySkuDetailsAsync(
      final String skuType, String[] skusList, final long futureHandle) {
    querySkuDetailsAsync(
        SkuDetailsParams.newBuilder().setType(skuType).setSkusList(Arrays.asList(skusList)).build(),
        new BillingClientNativeCallback(futureHandle));
  }

  @Override
  public void consumeAsync(
      final ConsumeParams consumeParams, final ConsumeResponseListener listener) {
    if (!isReady()) {
      listener.onConsumeResponse(BillingResults.SERVICE_DISCONNECTED, /* purchaseToken */ null);
      return;
    }

    Future result =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                consumeInternal(consumeParams, listener);
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                listener.onConsumeResponse(
                    BillingResults.SERVICE_TIMEOUT, /* purchaseToken */ null);
              }
            });
    if (result == null) {
      listener.onConsumeResponse(getBillingResultForNullFutureResult(), /* purchaseToken */ null);
    }
  }

  // Used for C++.
  private void consumeAsync(final ConsumeParams consumeParams, final long futureHandle) {
    consumeAsync(consumeParams, new BillingClientNativeCallback(futureHandle));
  }


  @Override
  public void queryPurchaseHistoryAsync(
      final @SkuType String skuType, final PurchaseHistoryResponseListener listener) {
    if (!isReady()) {
      listener.onPurchaseHistoryResponse(
          BillingResults.SERVICE_DISCONNECTED,
          /* purchasesList */
          null);
      return;
    }

    Future result =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                final PurchaseHistoryResult result = queryPurchaseHistoryInternal(skuType);

                // Post the result to main thread
                postToUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        listener.onPurchaseHistoryResponse(
                            result.getBillingResult(), result.getPurchaseHistoryRecordList());
                      }
                    });
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                listener.onPurchaseHistoryResponse(
                    BillingResults.SERVICE_TIMEOUT, /* purchaseHistoryRecordList= */ null);
              }
            });
    if (result == null) {
      listener.onPurchaseHistoryResponse(
          getBillingResultForNullFutureResult(),
          /* purchasesList */
          null);
    }
  }

  /** Queries purchases history and combines all the multi-page results into one list. */
  private PurchaseHistoryResult queryPurchaseHistoryInternal(@SkuType String skuType) {
    BillingHelper.logVerbose(TAG, "Querying purchase history, item type: " + skuType);

    String continueToken = null;
    List<PurchaseHistoryRecord> resultList = new ArrayList<PurchaseHistoryRecord>();
    Bundle extraParams =
        BillingHelper.constructExtraParamsForQueryPurchases(
            mIABv9Supported, mEnablePendingPurchases, mQualifiedVersionNumber);

    do {
      Bundle purchasedItems;
      try {
        // If current client doesn't support IABv6, then there is no such method yet
        if (!mIABv6Supported) {
          BillingHelper.logWarn(TAG, "getPurchaseHistory is not supported on current device");
          return new PurchaseHistoryResult(
              BillingResults.GET_PURCHASE_HISTORY_NOT_SUPPORTED,
              /* purchaseHistoryRecordList= */ null);
        }
        purchasedItems =
            mService.getPurchaseHistory(
                /* apiVersion= */ 6,
                mApplicationContext.getPackageName(),
                skuType,
                continueToken,
                extraParams);
      } catch (RemoteException e) {
        BillingHelper.logWarn(
            TAG, "Got exception trying to get purchase history: " + e + "; try to reconnect");
        return new PurchaseHistoryResult(
            BillingResults.SERVICE_DISCONNECTED, /* purchaseHistoryRecordList= */ null);
      }

      BillingResult billingResult =
          checkPurchasesBundleValidity(purchasedItems, TAG, "getPurchaseHistory()");
      if (billingResult != BillingResults.OK) {
        return new PurchaseHistoryResult(billingResult, /* purchaseHistoryRecordList= */ null);
      }

      ArrayList<String> ownedSkus =
          purchasedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_ITEM_LIST);
      ArrayList<String> purchaseDataList =
          purchasedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST);
      ArrayList<String> signatureList =
          purchasedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST);

      for (int i = 0; i < purchaseDataList.size(); ++i) {
        String purchaseData = purchaseDataList.get(i);
        String signature = signatureList.get(i);
        String sku = ownedSkus.get(i);

        BillingHelper.logVerbose(TAG, "Purchase record found for sku : " + sku);
        PurchaseHistoryRecord purchaseHistoryRecord;
        try {
          purchaseHistoryRecord = new PurchaseHistoryRecord(purchaseData, signature);
        } catch (JSONException e) {
          BillingHelper.logWarn(TAG, "Got an exception trying to decode the purchase: " + e);
          return new PurchaseHistoryResult(
              BillingResults.INTERNAL_ERROR, /* purchaseHistoryRecordList= */ null);
        }

        if (TextUtils.isEmpty(purchaseHistoryRecord.getPurchaseToken())) {
          BillingHelper.logWarn(TAG, "BUG: empty/null token!");
        }

        resultList.add(purchaseHistoryRecord);
      }

      continueToken = purchasedItems.getString(INAPP_CONTINUATION_TOKEN);
      BillingHelper.logVerbose(TAG, "Continuation token: " + continueToken);
    } while (!TextUtils.isEmpty(continueToken));

    return new PurchaseHistoryResult(BillingResults.OK, resultList);
  }

  // Used for C++.
  private void queryPurchaseHistoryAsync(
      @NonNull final @SkuType String skuType, final long futureHandle) {
    queryPurchaseHistoryAsync(skuType, new BillingClientNativeCallback(futureHandle));
  }

  @Override
  public void loadRewardedSku(
      final RewardLoadParams params, final RewardResponseListener listener) {

    if (!mIABv6Supported) {
      listener.onRewardResponse(BillingResults.ITEM_UNAVAILABLE);
      // return unavailable
      return;
    }

    Future result =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                Bundle extraParams =
                    BillingHelper.constructExtraParamsForLoadRewardedSku(
                        params.getSkuDetails().rewardToken(),
                        mChildDirected,
                        mUnderAgeOfConsent,
                        mQualifiedVersionNumber);
                Bundle buyIntentBundle;
                try {
                  buyIntentBundle =
                      mService.getBuyIntentExtraParams(
                          /* apiVersion= */ 6,
                          mApplicationContext.getPackageName(),
                          params.getSkuDetails().getSku(),
                          params.getSkuDetails().getType(),
                          null,
                          extraParams);
                } catch (final Exception e) {
                  postToUiThread(
                      new Runnable() {
                        @Override
                        public void run() {
                          listener.onRewardResponse(BillingResults.INTERNAL_ERROR);
                        }
                      });
                  return null;
                }

                final BillingResult billingResult =
                    BillingResult.newBuilder()
                        .setResponseCode(
                            BillingHelper.getResponseCodeFromBundle(buyIntentBundle, TAG))
                        .setDebugMessage(
                            BillingHelper.getDebugMessageFromBundle(buyIntentBundle, TAG))
                        .build();

                postToUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        listener.onRewardResponse(billingResult);
                      }
                    });
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                listener.onRewardResponse(BillingResults.SERVICE_TIMEOUT);
              }
            });
    if (result == null) {
      listener.onRewardResponse(getBillingResultForNullFutureResult());
    }
  }

  // Used for C++.
  private void loadRewardedSku(final RewardLoadParams params, final long futureHandle) {
    loadRewardedSku(params, new BillingClientNativeCallback(futureHandle));
  }

  @Override
  public void acknowledgePurchase(
      final AcknowledgePurchaseParams acknowledgePurchaseParams,
      final AcknowledgePurchaseResponseListener listener) {
    if (!isReady()) {
      listener.onAcknowledgePurchaseResponse(BillingResults.SERVICE_DISCONNECTED);
      return;
    }

    // Checking for the mandatory argument
    if (TextUtils.isEmpty(acknowledgePurchaseParams.getPurchaseToken())) {
      BillingHelper.logWarn(TAG, "Please provide a valid purchase token.");
      listener.onAcknowledgePurchaseResponse(BillingResults.INVALID_PURCHASE_TOKEN);
      return;
    }

    if (!mIABv9Supported) {
      listener.onAcknowledgePurchaseResponse(BillingResults.API_VERSION_NOT_V9);
      return;
    }

    Future<?> futureAcknowledgePurchase =
        executeAsync(
            new Callable<Void>() {
              @Override
              public Void call() {
                Bundle responseBundle;
                try {
                  responseBundle =
                      mService.acknowledgePurchaseExtraParams(
                          /* apiVersion= */ 9,
                          mApplicationContext.getPackageName(),
                          acknowledgePurchaseParams.getPurchaseToken(),
                          BillingHelper.constructExtraParamsForAcknowledgePurchase(
                              acknowledgePurchaseParams, mQualifiedVersionNumber));
                } catch (final Exception e) {
                  postToUiThread(
                      new Runnable() {
                        @Override
                        public void run() {
                          // TODO(b/128430840): Use better logging method instead of string
                          // concatentation.
                          BillingHelper.logWarn(TAG, "Error acknowledge purchase; ex: " + e);
                          listener.onAcknowledgePurchaseResponse(
                              BillingResults.SERVICE_DISCONNECTED);
                        }
                      });
                  return null;
                }

                final int responseCode =
                    BillingHelper.getResponseCodeFromBundle(responseBundle, TAG);
                final String debugMessage =
                    BillingHelper.getDebugMessageFromBundle(responseBundle, TAG);

                postToUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        listener.onAcknowledgePurchaseResponse(
                            BillingResult.newBuilder()
                                .setResponseCode(responseCode)
                                .setDebugMessage(debugMessage)
                                .build());
                      }
                    });
                return null;
              }
            },
            ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            new Runnable() {
              @Override
              public void run() {
                listener.onAcknowledgePurchaseResponse(BillingResults.SERVICE_TIMEOUT);
              }
            });
    if (futureAcknowledgePurchase == null) {
      listener.onAcknowledgePurchaseResponse(getBillingResultForNullFutureResult());
    }
  }

  // Used for C++.
  private void acknowledgePurchase(
      final AcknowledgePurchaseParams acknowledgePurchaseParams, final long futureHandle) {
    acknowledgePurchase(acknowledgePurchaseParams, new BillingClientNativeCallback(futureHandle));
  }

  /**
   * Execute a task in a background thread.
   *
   * @param callable The task to run
   * @param maxTimeout The duration before the task is timed out and cancelled. Actual applied
   *     timeout is 95% of that value.
   * @param onTimeout A runnable to execute when the task time out
   * @return A future allowing to wait on the task
   */
  @Nullable
  private <T> Future<T> executeAsync(
      @NonNull Callable<T> callable, long maxTimeout, @Nullable final Runnable onTimeout) {
    long actualTimeout = (long) (0.95 * maxTimeout);
    if (mExecutorService == null) {
      mExecutorService = Executors.newFixedThreadPool(BillingHelper.NUMBER_OF_CORES);
    }

    final Future<T> task;
    try {
      task = mExecutorService.submit(callable);
    } catch (Exception e) {
      // TODO(b/128430840): Use better logging method instead of string concatentation.
      BillingHelper.logWarn(TAG, "Async task throws exception " + e);
      return null;
    }
    mUiThreadHandler.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            if (!task.isDone() && !task.isCancelled()) {
              // Cancel the task, get() method will return CancellationException and it is handled
              // in catch block.
              task.cancel(true);
              BillingHelper.logWarn(TAG, "Async task is taking too long, cancel it!");
              if (onTimeout != null) {
                onTimeout.run();
              }
            }
          }
        },
        actualTimeout);
    return task;
  }

  /** Checks if billing on VR is supported for corresponding billing type. */
  private BillingResult isBillingSupportedOnVr(final @SkuType String skuType) {
    Future<Integer> futureSupportedResult =
        executeAsync(
            new Callable<Integer>() {
              @Override
              public Integer call() throws Exception {
                return mService.isBillingSupportedExtraParams(
                    /* apiVersion= */ 7,
                    mApplicationContext.getPackageName(),
                    skuType,
                    generateVrBundle());
              }
            },
            SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
            /* onTimeout= */ null);

    try {
      int supportedResult =
          futureSupportedResult.get(SYNCHRONOUS_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
      return (supportedResult == BillingResponseCode.OK)
          ? BillingResults.OK
          : BillingResults.FEATURE_NOT_SUPPORTED;
    } catch (Exception e) {
      BillingHelper.logWarn(
          TAG, "Exception while checking if billing is supported; try to reconnect");
      return BillingResults.SERVICE_DISCONNECTED;
    }
  }

  /**
   * Generates a Bundle to indicate that we are request a method for VR experience within
   * extraParams
   */
  private Bundle generateVrBundle() {
    Bundle result = new Bundle();
    result.putBoolean(BillingFlowParams.EXTRA_PARAM_KEY_VR, true);
    return result;
  }

  @VisibleForTesting
  SkuDetailsResult querySkuDetailsInternal(@SkuType String skuType, List<String> skuList) {
    List<SkuDetails> resultList = new ArrayList<>();

    // Split the sku list into packs of no more than MAX_SKU_DETAILS_ITEMS_PER_REQUEST elements
    int startIndex = 0;
    int listSize = skuList.size();
    while (startIndex < listSize) {
      // Prepare a network request up to a maximum amount of supported elements
      int endIndex = startIndex + MAX_SKU_DETAILS_ITEMS_PER_REQUEST;
      if (endIndex > listSize) {
        endIndex = listSize;
      }
      ArrayList<String> curSkuList = new ArrayList<>(skuList.subList(startIndex, endIndex));
      Bundle querySkus = new Bundle();
      querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, curSkuList);
      // TODO(b/133788487): remove this once phonesky change to not read from query skus is made.
      querySkus.putString(BillingHelper.LIBRARY_VERSION_KEY, mQualifiedVersionNumber);
      Bundle skuDetails;
      try {
        if (mIABv9Supported) {
          skuDetails =
              mService.getSkuDetailsExtraParams(
                  /* apiVersion= */ 9,
                  mApplicationContext.getPackageName(),
                  skuType,
                  querySkus,
                  BillingHelper.constructExtraParamsForGetSkuDetails(
                      mIABv9Supported, mEnablePendingPurchases, mQualifiedVersionNumber));
        } else {
          skuDetails =
              mService.getSkuDetails(
                  /* apiVersion= */ 3, mApplicationContext.getPackageName(), skuType, querySkus);
        }
        // TODO(b/128430724): Figure out better exception catching.
      } catch (Exception e) {
        String loggedMsg = "querySkuDetailsAsync got a remote exception (try to reconnect).";
        // TODO(b/128430840): Use better logging method instead of string concatentation.
        BillingHelper.logWarn(TAG, loggedMsg + e);
        return new SkuDetailsResult(
            BillingResponseCode.SERVICE_DISCONNECTED,
            DebugMessages.SERVICE_DISCONNECTED, /* skuDetailsList */
            null);
      }

      if (skuDetails == null) {
        String debugMessage = "querySkuDetailsAsync got null sku details list";
        BillingHelper.logWarn(TAG, debugMessage);
        return new SkuDetailsResult(
            BillingResponseCode.ITEM_UNAVAILABLE,
            DebugMessages.NULL_SKU_DETAILS_LIST, /* skuDetailsList */
            null);
      }

      if (!skuDetails.containsKey(BillingHelper.RESPONSE_GET_SKU_DETAILS_LIST)) {
        @BillingResponseCode
        int responseCode = BillingHelper.getResponseCodeFromBundle(skuDetails, TAG);
        String debugMessage = BillingHelper.getDebugMessageFromBundle(skuDetails, TAG);

        if (responseCode != BillingResponseCode.OK) {
          String msg = "getSkuDetails() failed. Response code: " + responseCode;
          BillingHelper.logWarn(TAG, msg);
          return new SkuDetailsResult(responseCode, debugMessage, resultList);
        } else {
          String msg = "getSkuDetails() returned a bundle with neither an error nor a detail list.";
          BillingHelper.logWarn(TAG, msg);
          return new SkuDetailsResult(BillingResponseCode.ERROR, debugMessage, resultList);
        }
      }

      ArrayList<String> skuDetailsJsonList =
          skuDetails.getStringArrayList(BillingHelper.RESPONSE_GET_SKU_DETAILS_LIST);

      if (skuDetailsJsonList == null) {
        String debugMessage = "querySkuDetailsAsync got null response list";
        BillingHelper.logWarn(TAG, debugMessage);
        return new SkuDetailsResult(
            BillingResponseCode.ITEM_UNAVAILABLE, debugMessage, /* skuDetailsList */ null);
      }

      for (int i = 0; i < skuDetailsJsonList.size(); ++i) {
        String thisResponse = skuDetailsJsonList.get(i);
        SkuDetails currentSkuDetails;
        try {
          currentSkuDetails = new SkuDetails(thisResponse);
        } catch (JSONException e) {
          String msg = "Got a JSON exception trying to decode SkuDetails.";
          BillingHelper.logWarn(TAG, msg);
          return new SkuDetailsResult(
              BillingResponseCode.ERROR,
              DebugMessages.ERROR_DECODING_SKU_DETAILS, /* skuDetailsList */
              null);
        }
        BillingHelper.logVerbose(TAG, "Got sku details: " + currentSkuDetails);
        resultList.add(currentSkuDetails);
      }

      // Switching start index to the end of just received pack
      startIndex += MAX_SKU_DETAILS_ITEMS_PER_REQUEST;
    }

    return new SkuDetailsResult(BillingResponseCode.OK, /* DebugMessage */ "", resultList);
  }

  /** Queries purchases and combines all the multi-page results into one list */
  private PurchasesResult queryPurchasesInternal(@SkuType String skuType) {
    BillingHelper.logVerbose(TAG, "Querying owned items, item type: " + skuType);

    String continueToken = null;
    List<Purchase> resultList = new ArrayList<>();

    Bundle extraParams =
        BillingHelper.constructExtraParamsForQueryPurchases(
            mIABv9Supported, mEnablePendingPurchases, mQualifiedVersionNumber);

    do {
      Bundle ownedItems;
      try {
        if (mIABv9Supported) {
          ownedItems =
              mService.getPurchasesExtraParams(
                  /* apiVersion */ 9,
                  mApplicationContext.getPackageName(),
                  skuType,
                  continueToken,
                  extraParams);
        } else {
          ownedItems =
              mService.getPurchases(
                  3 /* apiVersion */, mApplicationContext.getPackageName(), skuType, continueToken);
        }
        // TODO(b/128430724): Figure out better exception catching.
      } catch (Exception e) {
        // TODO(b/128430840): Use better logging method instead of string concatentation.
        BillingHelper.logWarn(
            TAG, "Got exception trying to get purchases: " + e + "; try to reconnect");
        return new PurchasesResult(
            BillingResults.SERVICE_DISCONNECTED,
            /* purchasesList */
            null);
      }

      BillingResult billingResult = checkPurchasesBundleValidity(ownedItems, TAG, "getPurchase()");
      if (billingResult != BillingResults.OK) {
        return new PurchasesResult(billingResult, /* purchasesList= */ null);
      }

      ArrayList<String> ownedSkus =
          ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_ITEM_LIST);
      ArrayList<String> purchaseDataList =
          ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST);
      ArrayList<String> signatureList =
          ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST);

      for (int i = 0; i < purchaseDataList.size(); ++i) {
        String purchaseData = purchaseDataList.get(i);
        String signature = signatureList.get(i);
        String sku = ownedSkus.get(i);

        BillingHelper.logVerbose(TAG, "Sku is owned: " + sku);
        Purchase purchase;
        try {
          purchase = new Purchase(purchaseData, signature);
        } catch (JSONException e) {
          // TODO(b/128430840): Use better logging method instead of string concatentation.
          BillingHelper.logWarn(TAG, "Got an exception trying to decode the purchase: " + e);
          return new PurchasesResult(BillingResults.INTERNAL_ERROR, /* purchasesList */ null);
        }

        if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
          BillingHelper.logWarn(TAG, "BUG: empty/null token!");
        }

        resultList.add(purchase);
      }

      continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
      BillingHelper.logVerbose(TAG, "Continuation token: " + continueToken);
    } while (!TextUtils.isEmpty(continueToken));

    return new PurchasesResult(BillingResults.OK, resultList);
  }

  /** Execute the runnable on the UI/Main Thread */
  private void postToUiThread(Runnable runnable) {
    // Check thread as the task could be interrupted due to timeout and prevent double notification
    if (Thread.interrupted()) {
      return;
    }
    mUiThreadHandler.post(runnable);
  }

  /** Consume the purchase and execute listener's callback on the Ui/Main thread */
  @WorkerThread
  private void consumeInternal(
      final ConsumeParams consumeParams, final ConsumeResponseListener listener) {
    final String purchaseToken = consumeParams.getPurchaseToken();
    try {
      BillingHelper.logVerbose(TAG, "Consuming purchase with token: " + purchaseToken);
      final @BillingResponseCode int responseCode;
      final String debugMessage;
      if (mIABv9Supported) {
        Bundle responseBundle =
            mService.consumePurchaseExtraParams(
                9 /* apiVersion */,
                mApplicationContext.getPackageName(),
                purchaseToken,
                BillingHelper.constructExtraParamsForConsume(
                    consumeParams, mIABv9Supported, mQualifiedVersionNumber));
        responseCode = responseBundle.getInt(RESPONSE_CODE);
        debugMessage = BillingHelper.getDebugMessageFromBundle(responseBundle, TAG);
      } else {
        responseCode =
            mService.consumePurchase(
                3 /* apiVersion */, mApplicationContext.getPackageName(), purchaseToken);
        debugMessage = "";
      }
      final BillingResult billingResult =
          BillingResult.newBuilder()
              .setResponseCode(responseCode)
              .setDebugMessage(debugMessage)
              .build();

      if (responseCode == BillingResponseCode.OK) {
        postToUiThread(
            new Runnable() {
              @Override
              public void run() {
                BillingHelper.logVerbose(TAG, "Successfully consumed purchase.");
                listener.onConsumeResponse(billingResult, purchaseToken);
              }
            });
      } else {
        postToUiThread(
            new Runnable() {
              @Override
              public void run() {
                BillingHelper.logWarn(
                    TAG, "Error consuming purchase with token. Response code: " + responseCode);
                listener.onConsumeResponse(billingResult, purchaseToken);
              }
            });
      }
    } catch (final Exception e) {
      postToUiThread(
          new Runnable() {
            @Override
            public void run() {
              // TODO(b/128430840): Use better logging method instead of string concatentation.
              BillingHelper.logWarn(TAG, "Error consuming purchase; ex: " + e);
              listener.onConsumeResponse(BillingResults.SERVICE_DISCONNECTED, purchaseToken);
            }
          });
    }
  }

  /** Connect with Billing service and notify listener about important states. */
  private final class BillingServiceConnection implements ServiceConnection {
    private final Object lock = new Object();
    private boolean disconnected = false;
    private BillingClientStateListener mListener;

    private BillingServiceConnection(@NonNull BillingClientStateListener listener) {
      mListener = listener;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      BillingHelper.logWarn(TAG, "Billing service disconnected.");
      mService = null;
      mClientState = ClientState.DISCONNECTED;
      synchronized (lock) {
        if (mListener != null) {
          mListener.onBillingServiceDisconnected();
        }
      }
    }

    void markDisconnectedAndCleanUp() {
      synchronized (lock) {
        mListener = null;
        disconnected = true;
      }
    }

    private void notifySetupResult(final BillingResult result) {
      postToUiThread(
          new Runnable() {
            @Override
            public void run() {
              synchronized (lock) {
                if (mListener != null) {
                  mListener.onBillingSetupFinished(result);
                }
              }
            }
          });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      BillingHelper.logVerbose(TAG, "Billing service connected.");
      mService = IInAppBillingService.Stub.asInterface(service);
      Future result =
          executeAsync(
              new Callable<Void>() {
                @Override
                public Void call() {
                  // Check in case endConnection() is called before service gets connected.
                  synchronized (lock) {
                    if (disconnected) {
                      return null;
                    }
                  }
                  int setupResponse = BillingResponseCode.BILLING_UNAVAILABLE;
                  try {
                    String packageName = mApplicationContext.getPackageName();
                    // Determine the highest supported level for Subs.
                    int highestLevelSupportedForSubs = 0;
                    for (int apiVersion = MAX_IAP_VERSION;
                        apiVersion >= MIN_IAP_VERSION;
                        apiVersion--) {
                      setupResponse =
                          mService.isBillingSupported(apiVersion, packageName, SkuType.SUBS);
                      if (setupResponse == BillingResponseCode.OK) {
                        highestLevelSupportedForSubs = apiVersion;
                        break;
                      }
                    }
                    mSubscriptionUpdateSupported = highestLevelSupportedForSubs >= 5;
                    mSubscriptionsSupported = highestLevelSupportedForSubs >= 3;
                    if (highestLevelSupportedForSubs < MIN_IAP_VERSION) {
                      BillingHelper.logVerbose(
                          TAG, "In-app billing API does not support subscription on this device.");
                    }

                    // Determine the highest supported level for InApp.
                    int highestLevelSupportedForInApp = 0;
                    for (int apiVersion = MAX_IAP_VERSION;
                        apiVersion >= MIN_IAP_VERSION;
                        apiVersion--) {
                      setupResponse =
                          mService.isBillingSupported(apiVersion, packageName, SkuType.INAPP);
                      if (setupResponse == BillingResponseCode.OK) {
                        highestLevelSupportedForInApp = apiVersion;
                        break;
                      }
                    }
                    // ---- modified start ----
                    //mIABv9Supported = highestLevelSupportedForInApp >= 9;
                    // ---- modified end ----
                    mIABv8Supported = highestLevelSupportedForInApp >= 8;
                    mIABv6Supported = highestLevelSupportedForInApp >= 6;
                    if (highestLevelSupportedForInApp < MIN_IAP_VERSION) {
                      BillingHelper.logWarn(
                          TAG, "In-app billing API version 3 is not supported on this device.");
                    }
                    if (setupResponse == BillingResponseCode.OK) {
                      mClientState = ClientState.CONNECTED;
                    } else {
                      mClientState = ClientState.DISCONNECTED;
                      mService = null;
                    }
                  } catch (Exception e) {
                    BillingHelper.logWarn(
                        TAG, "Exception while checking if billing is supported; try to reconnect");
                    mClientState = ClientState.DISCONNECTED;
                    mService = null;
                  }
                  if (setupResponse == BillingResponseCode.OK) {
                    notifySetupResult(BillingResults.OK);
                  } else {
                    notifySetupResult(BillingResults.API_VERSION_NOT_V3);
                  }
                  return null;
                }
              },
              ASYNCHRONOUS_TIMEOUT_IN_MILLISECONDS,
              new Runnable() {
                @Override
                public void run() {
                  mClientState = ClientState.DISCONNECTED;
                  mService = null;
                  notifySetupResult(BillingResults.SERVICE_TIMEOUT);
                }
              });
      if (result == null) {
        notifySetupResult(getBillingResultForNullFutureResult());
      }
    }
  }

  private BillingResult getBillingResultForNullFutureResult() {
    return (mClientState == ClientState.DISCONNECTED || mClientState == ClientState.CLOSED)
        ? BillingResults.SERVICE_DISCONNECTED
        : BillingResults.INTERNAL_ERROR;
  }

  /** Result list and code for queryPurchaseHistory method. */
  private static class PurchaseHistoryResult {
    private List<PurchaseHistoryRecord> mPurchaseHistoryRecordList;
    private BillingResult mBillingResult;

    PurchaseHistoryResult(
        BillingResult mBillingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList) {
      this.mPurchaseHistoryRecordList = purchaseHistoryRecordList;
      this.mBillingResult = mBillingResult;
    }

    /** Returns the {@link BillingResult} of the operation. */
    BillingResult getBillingResult() {
      return mBillingResult;
    }

    /** Returns the list of {@link PurchaseHistoryRecord}. */
    List<PurchaseHistoryRecord> getPurchaseHistoryRecordList() {
      return mPurchaseHistoryRecordList;
    }
  }
}
