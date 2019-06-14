/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.annotation.UiThread;

import com.android.billingclient.api.Purchase.PurchasesResult;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Main interface for communication between the library and user application code.
 *
 * <p>It provides convenience methods for in-app billing. You can create one instance of this class
 * for your application and use it to process in-app billing operations. It provides synchronous
 * (blocking) and asynchronous (non-blocking) methods for many common in-app billing operations.
 *
 * <p>All methods are supposed to be called from the Ui thread and all the asynchronous callbacks
 * will be returned on the Ui thread as well.
 *
 * <p>After instantiating, you must perform setup in order to start using the object. To perform
 * setup, call the {@link #startConnection} method and provide a listener; that listener will be
 * notified when setup is complete, after which (and not before) you may start calling other
 * methods. After setup is complete, you will typically want to request an inventory of owned items
 * and subscriptions. See {@link #queryPurchases} and {@link #querySkuDetailsAsync}.
 *
 * <p>When you are done with this object, don't forget to call {@link #endConnection()} to ensure
 * proper cleanup. This object holds a binding to the in-app billing service and the manager to
 * handle broadcast events, which will leak unless you dispose it correctly. If you created the
 * object inside the {@link Activity#onCreate} method, then the recommended place to dispose is the
 * {@link Activity#onDestroy} method.
 *
 * <p>To get library logs inside Android logcat, set corresponding logging level.
 * E.g.: <code>adb shell setprop log.tag.BillingClient VERBOSE</code>
 */
public abstract class BillingClient {
  /** Supported SKU types. */
  @StringDef({SkuType.INAPP, SkuType.SUBS})
  @Retention(SOURCE)
  public @interface SkuType {
    /** A type of SKU for in-app products. */
    String INAPP = "inapp";
    /** A type of SKU for subscriptions. */
    String SUBS = "subs";
  }

  /** Features/capabilities supported by {@link #isFeatureSupported(String)}. */
  @StringDef({
    FeatureType.SUBSCRIPTIONS,
    FeatureType.SUBSCRIPTIONS_UPDATE,
    FeatureType.IN_APP_ITEMS_ON_VR,
    FeatureType.SUBSCRIPTIONS_ON_VR,
    FeatureType.PRICE_CHANGE_CONFIRMATION
  })
  @Retention(SOURCE)
  public @interface FeatureType {
    /** Purchase/query for subscriptions. */
    String SUBSCRIPTIONS = "subscriptions";
    /** Subscriptions update/replace. */
    String SUBSCRIPTIONS_UPDATE = "subscriptionsUpdate";
    /** Purchase/query for in-app items on VR. */
    String IN_APP_ITEMS_ON_VR = "inAppItemsOnVr";
    /** Purchase/query for subscriptions on VR. */
    String SUBSCRIPTIONS_ON_VR = "subscriptionsOnVr";
    /** Launch a price change confirmation flow. */
    String PRICE_CHANGE_CONFIRMATION = "priceChangeConfirmation";
  }

  /** Possible response codes. */
  @IntDef({
    BillingResponseCode.SERVICE_TIMEOUT,
    BillingResponseCode.FEATURE_NOT_SUPPORTED,
    BillingResponseCode.SERVICE_DISCONNECTED,
    BillingResponseCode.OK,
    BillingResponseCode.USER_CANCELED,
    BillingResponseCode.SERVICE_UNAVAILABLE,
    BillingResponseCode.BILLING_UNAVAILABLE,
    BillingResponseCode.ITEM_UNAVAILABLE,
    BillingResponseCode.DEVELOPER_ERROR,
    BillingResponseCode.ERROR,
    BillingResponseCode.ITEM_ALREADY_OWNED,
    BillingResponseCode.ITEM_NOT_OWNED,
  })
  @Retention(SOURCE)
  public @interface BillingResponseCode {
    /** The request has reached the maximum timeout before Google Play responds. */
    int SERVICE_TIMEOUT = -3;
    /** Requested feature is not supported by Play Store on the current device. */
    int FEATURE_NOT_SUPPORTED = -2;
    /**
     * Play Store service is not connected now - potentially transient state.
     *
     * <p>E.g. Play Store could have been updated in the background while your app was still
     * running. So feel free to introduce your retry policy for such use case. It should lead to a
     * call to {@link #startConnection} right after or in some time after you received this code.
     */
    int SERVICE_DISCONNECTED = -1;
    /** Success */
    int OK = 0;
    /** User pressed back or canceled a dialog */
    int USER_CANCELED = 1;
    /** Network connection is down */
    int SERVICE_UNAVAILABLE = 2;
    /** Billing API version is not supported for the type requested */
    int BILLING_UNAVAILABLE = 3;
    /** Requested product is not available for purchase */
    int ITEM_UNAVAILABLE = 4;
    /**
     * Invalid arguments provided to the API. This error can also indicate that the application was
     * not correctly signed or properly set up for In-app Billing in Google Play, or does not have
     * the necessary permissions in its manifest
     */
    int DEVELOPER_ERROR = 5;
    /** Fatal error during the API action */
    int ERROR = 6;
    /** Failure to purchase since item is already owned */
    int ITEM_ALREADY_OWNED = 7;
    /** Failure to consume since item is not owned */
    int ITEM_NOT_OWNED = 8;
  }

  /**
   * Developers are able to specify whether you would like your app to be treated as child-directed
   * for purposes of the Children’s Online Privacy Protection Act (COPPA) - <a
   * href="http://business.ftc.gov/privacy-and-security/childrens-privacy">
   * http://business.ftc.gov/privacy-and-security/childrens-privacy</a>.
   *
   * <p>This is most relevant for Rewarded Skus.
   *
   * <p>If you set this method to {@link ChildDirected.CHILD_DIRECTED}, you will indicate that your
   * ad requests should be treated as child-directed for purposes of the Children’s Online Privacy
   * Protection Act (COPPA).
   *
   * <p>If you set this method to {@link ChildDirected.NOT_CHILD_DIRECTED}, you will indicate that
   * your ad requests should not be treated as child-directed for purposes of the Children’s Online
   * Privacy Protection Act (COPPA).
   *
   * <p>If you do not set this, ad requests from this session will include no indication of how you
   * would like your app treated with respect to COPPA.
   *
   * <p>By setting this method, you certify that this notification is accurate and you are
   * authorized to act on behalf of the owner of the app. You understand that abuse of this setting
   * may result in termination of your Google account.
   *
   * <p>Note: it may take some time for this designation to be fully implemented in applicable
   * Google services.
   */
  @IntDef({
    ChildDirected.UNSPECIFIED,
    ChildDirected.CHILD_DIRECTED,
    ChildDirected.NOT_CHILD_DIRECTED,
  })
  @Retention(SOURCE)
  public @interface ChildDirected {
    /** App has not specified whether its ad requests should be treated as child directed or not. */
    int UNSPECIFIED = 0;
    /** App indicates its ad requests should be treated as child-directed. */
    int CHILD_DIRECTED = 1;
    /** App indicates its ad requests should NOT be treated as child-directed. */
    int NOT_CHILD_DIRECTED = 2;
  }

  /**
   * Developers are able to specify whether to mark your ad requests to receive treatment for users
   * in the European Economic Area (EEA) under the age of consent. This feature is designed to help
   * facilitate compliance with the <a
   * href="https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32016R0679">General Data
   * Protection Regulation (GDPR)</a>. Note that you may have other legal obligations under GDPR.
   * Please review the European Union's guidance and consult with your own legal counsel. Please
   * remember that Google's tools are designed to facilitate compliance and do not relieve any
   * particular publisher of its obligations under the law.
   *
   * <p>This is most relevant for Rewarded Skus.
   *
   * <p>When using this feature, a Tag For Users under the Age of Consent in Europe (TFUA) parameter
   * will be included in the ad request. This parameter disables personalized advertising, including
   * remarketing, for that specific ad request. It also disables requests to third-party ad vendors,
   * such as ad measurement pixels and third-party ad servers.
   *
   * <p>If you set this method to {@link UnderAgeOfConsent.UNDER_AGE_OF_CONSENT}, you will indicate
   * that you want the ad request to be handled in a manner suitable for users under the age of
   * consent.
   *
   * <p>If you set this method to {@link UnderAgeOfConsent.NOT_UNDER_AGE_OF_CONSENT}, you will
   * indicate that you don't want the ad request to be handled in a manner suitable for users under
   * the age of consent.
   *
   * <p>If you do not set this method, or set this method to {@link UnderAgeOfConsent.UNSPECIFIED},
   * ad requests will include no indication of how you would like your app to be handled in a manner
   * suitable for users under the age of consent.
   */
  @IntDef({
    UnderAgeOfConsent.UNSPECIFIED,
    UnderAgeOfConsent.UNDER_AGE_OF_CONSENT,
    UnderAgeOfConsent.NOT_UNDER_AGE_OF_CONSENT,
  })
  @Retention(SOURCE)
  public @interface UnderAgeOfConsent {
    /** App has not specified how ad requests shall be handled. */
    int UNSPECIFIED = 0;
    /**
     * App indicates the ad requests shall be handled in a manner suitable for users under the age
     * of consent.
     */
    int UNDER_AGE_OF_CONSENT = 1;
    /**
     * App indicates the ad requests shall NOT be handled in a manner suitable for users under the
     * age of consent.
     */
    int NOT_UNDER_AGE_OF_CONSENT = 2;
  }

  /** Builder to configure and create a BillingClient instance. */
  public static final class Builder {
    private final Context mContext;
    @ChildDirected private int mChildDirected = ChildDirected.UNSPECIFIED;
    @UnderAgeOfConsent private int mUnderAgeOfConsent = UnderAgeOfConsent.UNSPECIFIED;
    private boolean mEnablePendingPurchases;
    private PurchasesUpdatedListener mListener;

    private Builder(Context context) {
      mContext = context;
    }

    /**
     * Specify a valid listener for onPurchasesUpdated event.
     *
     * @param listener Your listener for app initiated and Play Store initiated purchases.
     */
    @UiThread
    public Builder setListener(PurchasesUpdatedListener listener) {
      mListener = listener;
      return this;
    }

    /**
     * Developers are able to specify whether this app is child directed or not to ensure compliance
     * with US COPPA & EEA age of consent laws.
     *
     * <p>This is most relevant for rewarded skus as child directed applications are explicitly not
     * allowed to collect information that can be used to personalize the rewarded videos to the
     * user.
     */
    @UiThread
    public Builder setChildDirected(@ChildDirected int childDirected) {
      this.mChildDirected = childDirected;
      return this;
    }

    /**
     * Developers are able to specify whether this app is under age of consent or not to ensure
     * compliance with US COPPA & EEA age of consent laws.
     *
     * <p>This is most relevant for rewarded skus as under age of consent applications are
     * explicitly not allowed to collect information that can be used to personalize the rewarded
     * videos to the user.
     */
    @UiThread
    public Builder setUnderAgeOfConsent(@UnderAgeOfConsent int underAgeOfConsent) {
      this.mUnderAgeOfConsent = underAgeOfConsent;
      return this;
    }

    /**
     * Enables pending purchase support.
     *
     * <p>This method is required to be called to acknowledge your application has been updated to
     * support purchases that are pending. Pending purchases are not automatically enabled since
     * your application will require updates to ensure entitlement is not granted before payment has
     * been secured. For more information on how to handle pending transactions see
     * https://developer.android.com/google/play/billing/billing_library_overview.
     *
     * <p>If this method is not called, BillingClient instance creation fails.
     */
    @UiThread
    public Builder enablePendingPurchases() {
      mEnablePendingPurchases = true;
      return this;
    }

    /**
     * Creates a Billing client instance.
     *
     * <p>After creation, it will not yet be ready to use. You must initiate setup by calling {@link
     * #startConnection} and wait for setup to complete.
     *
     * @return BillingClient instance
     * @throws IllegalArgumentException if Context or PurchasesUpdatedListener were not set.
     */
    @UiThread
    public BillingClient build() {
      if (mContext == null) {
        throw new IllegalArgumentException("Please provide a valid Context.");
      }
      if (mListener == null) {
        throw new IllegalArgumentException(
            "Please provide a valid listener for" + " purchases updates.");
      }
      if (mEnablePendingPurchases != true) {
        throw new IllegalArgumentException(
            "Support for pending purchases must be enabled. Enable "
                + "this by calling 'enablePendingPurchases()' on BillingClientBuilder.");
      }
      return new BillingClientImpl(
          mContext, mChildDirected, mUnderAgeOfConsent, mEnablePendingPurchases, mListener);
    }
  }

  /**
   * Constructs a new {@link Builder} instance.
   *
   * @param context It will be used to get an application context to bind to the in-app billing
   *     service.
   */
  @UiThread
  public static Builder newBuilder(@NonNull Context context) {
    return new Builder(context);
  }

  /**
   * Check if specified feature or capability is supported by the Play Store.
   *
   * @param feature One of {@link FeatureType} constants.
   * @return BILLING_RESULT_OK if feature is supported and corresponding error code otherwise.
   */
  @UiThread
  public abstract BillingResult isFeatureSupported(@FeatureType String feature);

  /**
   * Checks if the client is currently connected to the service, so that requests to other methods
   * will succeed.
   *
   * <p>Returns true if the client is currently connected to the service, false otherwise.
   *
   * <p>Note: It also means that INAPP items are supported for purchasing, queries and all other
   * actions. If you need to check support for SUBSCRIPTIONS or something different, use {@link
   * #isFeatureSupported(String)} method.
   */
  @UiThread
  public abstract boolean isReady();

  /**
   * Starts up BillingClient setup process asynchronously. You will be notified through the {@link
   * BillingClientStateListener} listener when the setup process is complete.
   *
   * @param listener The listener to notify when the setup process is complete.
   */
  @UiThread
  public abstract void startConnection(@NonNull final BillingClientStateListener listener);

  /**
   * Close the connection and release all held resources such as service connections.
   *
   * <p>Call this method once you are done with this BillingClient reference.
   */
  @UiThread
  public abstract void endConnection();

  /**
   * Initiate the billing flow for an in-app purchase or subscription.
   *
   * <p>It will show the Google Play purchase screen. The result will be delivered via the {@link
   * PurchasesUpdatedListener} interface implementation reported to the {@link BillingClientImpl}
   * constructor.
   *
   * @param activity An activity reference from which the billing flow will be launched.
   * @param params Params specific to the request {@link BillingFlowParams}).
   * @return int The response code ({@link BillingResponseCode}) of launch flow operation.
   */
  @UiThread
  public abstract BillingResult launchBillingFlow(Activity activity, BillingFlowParams params);

  /**
   * Initiate a flow to confirm the change of price for an item subscribed by the user.
   *
   * <p>When the price of a user subscribed item has changed, launch this flow to take users to
   * a screen with price change information. User can confirm the new price or cancel the flow.
   *
   * @param activity An activity reference from which the billing flow will be launched.
   * @param params Params specific to the request {@link PriceChangeFlowParams}).
   * @param listener Implement it to get the result of your price change flow.
   */
  @UiThread
  public abstract void launchPriceChangeConfirmationFlow(
      Activity activity,
      PriceChangeFlowParams params,
      @NonNull PriceChangeConfirmationListener listener);

  /**
   * Get purchases details for all the items bought within your app. This method uses a cache of
   * Google Play Store app without initiating a network request.
   *
   * <p>Note: It's recommended for security purposes to go through purchases verification on your
   * backend (if you have one) by calling the following API:
   * https://developers.google.com/android-publisher/api-ref/purchases/products/get
   *
   * @param skuType The type of SKU, either "inapp" or "subs" as in {@link SkuType}.
   * @return PurchasesResult The {@link PurchasesResult} containing the list of purchases and the
   *     response code ({@link BillingResponseCode}
   */
  public abstract PurchasesResult queryPurchases(@SkuType String skuType);

  /**
   * Perform a network query to get SKU details and return the result asynchronously.
   *
   * @param params Params specific to this query request {@link SkuDetailsParams}.
   * @param listener Implement it to get the result of your query operation returned asynchronously
   *     through the callback with the {@link BillingResponseCode} and the list of {@link
   *     SkuDetails}.
   */
  public abstract void querySkuDetailsAsync(
      SkuDetailsParams params, @NonNull SkuDetailsResponseListener listener);

  /**
   * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a
   * result of consumption, the user will no longer own it.
   *
   * <p>Consumption is done asynchronously and the listener receives the callback specified upon
   * completion.
   *
   * @param ConsumeParams Params specific to consume purchase.
   * @param listener Implement it to get the result of your consume operation returned
   *     asynchronously through the callback with token and {@link BillingResponseCode} parameters.
   */
  public abstract void consumeAsync(
      ConsumeParams consumeParams, @NonNull ConsumeResponseListener listener);

  /**
   * Returns the most recent purchase made by the user for each SKU, even if that purchase is
   * expired, canceled, or consumed.
   *
   * @param skuType The type of SKU, either "inapp" or "subs" as in {@link SkuType}.
   * @param listener Implement it to get the result of your query returned asynchronously through
   *     the callback with a {@link PurchasesResult} parameter.
   */
  public abstract void queryPurchaseHistoryAsync(
      @SkuType String skuType, @NonNull PurchaseHistoryResponseListener listener);

  /**
   * Loads a rewarded sku in the background and returns the result asynchronously.
   *
   * <p>If the rewarded sku is available, the response will be BILLING_RESULT_OK. Otherwise the
   * response will be ITEM_UNAVAILABLE. There is no guarantee that a rewarded sku will always be
   * available. After a successful response, only then should the offer be given to a user to obtain
   * a rewarded item and call launchBillingFlow.
   *
   * @param params Params specific to this load request {@link RewardLoadParams}
   * @param listener Implement it to get the result of the load operation returned asynchronously
   *     through the callback with the {@link BillingResponseCode}
   */
  public abstract void loadRewardedSku(
      RewardLoadParams params, @NonNull RewardResponseListener listener);

  /**
   * Acknowledge in-app purchases.
   *
   * <p>Developers are required to acknowledge that they have granted entitlement for all in-app
   * purchases for their application.
   *
   * @param params Params specific to this acknowledge purchase request {@link
   *     AcknowledgePurchaseParams}
   * @param listener Implement it to get the result of the acknowledge operation returned
   *     asynchronously through the callback with the {@link BillingResponseCode}
   */
  public abstract void acknowledgePurchase(
      AcknowledgePurchaseParams params, AcknowledgePurchaseResponseListener listener);
}
