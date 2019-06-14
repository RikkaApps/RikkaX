/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement"). By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */
package com.android.billingclient.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient.BillingResponseCode;
import com.android.billingclient.api.BillingClient.ChildDirected;
import com.android.billingclient.api.BillingClient.UnderAgeOfConsent;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.RewardLoadParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Helper methods for billing client. */
public final class BillingHelper {
  // Keys for the responses from InAppBillingService
  public static final String RESPONSE_CODE = "RESPONSE_CODE";
  public static final String DEBUG_MESSAGE = "DEBUG_MESSAGE";
  public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
  /** Key in the response bundle of getBuyIntent for the purchase intent */
  public static final String RESPONSE_BUY_INTENT_KEY = "BUY_INTENT";
  /** Key in the response bundle of getSubsManagement for the subs management intent */
  public static final String RESPONSE_SUBS_MANAGEMENT_INTENT_KEY = "SUBS_MANAGEMENT_INTENT";
  // StringArrayList containing the list of SKUs
  public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
  // StringArrayList containing the purchase information
  public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
  // StringArrayList containing the signatures of the purchase information
  public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
  public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
  private static final String TAG = "BillingHelper";
  // Keys for Purchase data parsing
  private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
  private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
  // Keys for bundle used in subscription management intent
  public static final String EXTRA_PARAM_KEY_SUBS_PRICE_CHANGE = "subs_price_change";
  // Keys for enabling pending purchases in extra param
  public static final String EXTRA_PARAMS_ENABLE_PENDING_PURCHASES = "enablePendingPurchases";
  // Key for including developer payload in extra params
  public static final String EXTRA_PARAMS_DEVELOPER_PAYLOAD = "developerPayload";
  // Key for sku details server token in extra params.
  public static final String EXTRA_PARAM_KEY_SKU_DETAILS_TOKEN = "skuDetailsToken";
  /** Field's key to hold library version key constant. */
  public static final String LIBRARY_VERSION_KEY = "playBillingLibraryVersion";
  /** Total number of cores of current device */
  public static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

  private static final String INTERNAL_ERROR = "An internal error occurred.";

  /**
   * Logs a verbose message
   *
   * @param tag Tag to be used inside logging
   * @param msg Message to log
   */
  public static void logVerbose(String tag, String msg) {
    if (Log.isLoggable(tag, Log.VERBOSE)) {
      Log.v(tag, msg);
    }
  }

  /**
   * Logs a warning message
   *
   * @param tag Tag to be used inside logging
   * @param msg Message to log
   */
  public static void logWarn(String tag, String msg) {
    if (Log.isLoggable(tag, Log.WARN)) {
      Log.w(tag, msg);
    }
  }

  /** Retrieves a response code from the intent */
  @BillingResponseCode
  public static int getResponseCodeFromIntent(Intent intent, String tag) {
    return getBillingResultFromIntent(intent, tag).getResponseCode();
  }

  /** Retrieves billingResult from the intent */
  public static BillingResult getBillingResultFromIntent(Intent intent, String tag) {
    if (intent == null) {
      logWarn(TAG, "Got null intent!");
      return BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.ERROR)
          .setDebugMessage(INTERNAL_ERROR)
          .build();
    } else {
      return BillingResult.newBuilder()
          .setResponseCode(getResponseCodeFromBundle(intent.getExtras(), tag))
          .setDebugMessage(getDebugMessageFromBundle(intent.getExtras(), tag))
          .build();
    }
  }

  /** Retrieves a response code from the bundle */
  @BillingResponseCode
  public static int getResponseCodeFromBundle(Bundle bundle, String tag) {
    // Returning the error for null bundle
    if (bundle == null) {
      logWarn(tag, "Unexpected null bundle received!");
      return BillingResponseCode.ERROR;
    }
    // Getting the responseCode to report
    Object responseCode = bundle.get(RESPONSE_CODE);
    if (responseCode == null) {
      logVerbose(tag, "getResponseCodeFromBundle() got null response code, assuming OK");
      return BillingResponseCode.OK;
    } else if (responseCode instanceof Integer) {
      // noinspection WrongConstant
      return (Integer) responseCode;
    } else {
      logWarn(
          tag, "Unexpected type for bundle response code: " + responseCode.getClass().getName());
      return BillingResponseCode.ERROR;
    }
  }

  /** Retrieves a response code from the bundle */
  public static String getDebugMessageFromBundle(Bundle bundle, String tag) {
    String emptyString = "";
    // Since bundle is null there is no debug message.
    if (bundle == null) {
      logWarn(tag, "Unexpected null bundle received!");
      return emptyString;
    }
    // Getting the responseCode to report
    Object debugMessage = bundle.get(DEBUG_MESSAGE);
    if (debugMessage == null) {
      logVerbose(tag, "getDebugMessageFromBundle() got null response code, assuming OK");
      return emptyString;
    } else if (debugMessage instanceof String) {
      // noinspection WrongConstant
      return (String) debugMessage;
    } else {
      logWarn(tag, "Unexpected type for debug message: " + debugMessage.getClass().getName());
      return emptyString;
    }
  }

  /**
   * Gets a purchase data and signature (or lists of them) from the Bundle and returns the
   * constructed list of {@link Purchase}
   *
   * @param bundle The bundle to parse
   * @return New Purchase instance with the data extracted from the provided intent
   */
  public static List<Purchase> extractPurchases(Bundle bundle) {
    if (bundle == null) {
      return null;
    }

    List<String> purchaseDataList = bundle.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
    List<String> dataSignatureList = bundle.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);

    List<Purchase> resultList = new ArrayList<>();

    // If there were no lists of data, try to find single purchase data inside the Bundle
    if (purchaseDataList == null || dataSignatureList == null) {
      BillingHelper.logWarn(TAG, "Couldn't find purchase lists, trying to find single data.");

      String purchaseData = bundle.getString(RESPONSE_INAPP_PURCHASE_DATA);
      String dataSignature = bundle.getString(RESPONSE_INAPP_SIGNATURE);

      Purchase tmpPurchase = extractPurchase(purchaseData, dataSignature);

      if (tmpPurchase == null) {
        BillingHelper.logWarn(TAG, "Couldn't find single purchase data as well.");
        return null;
      } else {
        resultList.add(tmpPurchase);
      }
    } else {
      for (int i = 0; (i < purchaseDataList.size() && i < dataSignatureList.size()); ++i) {
        Purchase tmpPurchase = extractPurchase(purchaseDataList.get(i), dataSignatureList.get(i));

        if (tmpPurchase != null) {
          resultList.add(tmpPurchase);
        }
      }
    }
    return resultList;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#launchBillingFlow(Activity, BillingFlowParams)} }
   *
   * @param params params to initiate a purchase flow.
   * @param isIabV9Supported whether InAppBilling version 9 is supported by Play Store.
   * @param enablePendingPurchases whether to enable pending purchase in purchase flow.
   * @return extraParams bundle.
   */
  public static Bundle constructExtraParamsForLaunchBillingFlow(
      BillingFlowParams params,
      boolean isIabV9Supported,
      boolean enablePendingPurchases,
      String libraryVersion) {
    Bundle extraParams = new Bundle();
    extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);

    if (params.getReplaceSkusProrationMode()
        != BillingFlowParams.ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY) {
      extraParams.putInt(
          BillingFlowParams.EXTRA_PARAM_KEY_REPLACE_SKUS_PRORATION_MODE,
          params.getReplaceSkusProrationMode());
    }
    if (!TextUtils.isEmpty(params.getAccountId())) {
      extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, params.getAccountId());
    }
    if (params.getVrPurchaseFlow()) {
      extraParams.putBoolean(BillingFlowParams.EXTRA_PARAM_KEY_VR, true);
    }
    if (!TextUtils.isEmpty(params.getOldSku())) {
      extraParams.putStringArrayList(
          BillingFlowParams.EXTRA_PARAM_KEY_OLD_SKUS,
          new ArrayList<>(Arrays.asList(params.getOldSku())));
    }
    if (!TextUtils.isEmpty(params.getDeveloperId())) {
      extraParams.putString(
          BillingFlowParams.EXTRA_PARAM_KEY_DEVELOPER_ID, params.getDeveloperId());
    }

    if (isIabV9Supported && enablePendingPurchases) {
      extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
    }
    return extraParams;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#queryPurchaseHistoryAsync(String,
   * PurchaseHistoryResponseListener)} and {@link
   * com.android.billingclient.api.BillingClient#queryPurchases(String)}
   *
   * @param isIabV9Supported whether InAppBilling version 9 is supported by Play Store.
   * @param enablePendingPurchases whether to receive pending purchases in result.
   * @return extraParams bundle.
   */
  public static Bundle constructExtraParamsForQueryPurchases(
      boolean isIabV9Supported, boolean enablePendingPurchases, String libraryVersion) {
    Bundle extraParams = new Bundle();
    extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
    if (isIabV9Supported && enablePendingPurchases) {
      extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
    }
    return extraParams;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#loadRewardedSku(
   * RewardLoadParams params, final RewardResponseListener listener)}
   */
  public static Bundle constructExtraParamsForLoadRewardedSku(
      String rewardToken, int childDirected, int underAgeOfConsent, String libraryVersion) {
    Bundle extraParams = new Bundle();
    extraParams.putString(BillingFlowParams.EXTRA_PARAM_KEY_RSKU, rewardToken);
    extraParams.putString(BillingHelper.LIBRARY_VERSION_KEY, libraryVersion);
    if (childDirected != ChildDirected.UNSPECIFIED) {
      extraParams.putInt(BillingFlowParams.EXTRA_PARAM_CHILD_DIRECTED, childDirected);
    }
    if (underAgeOfConsent != UnderAgeOfConsent.UNSPECIFIED) {
      extraParams.putInt(BillingFlowParams.EXTRA_PARAM_UNDER_AGE_OF_CONSENT, childDirected);
    }
    return extraParams;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#querySkuDetailsAsync(SkuDetailsParams,
   * SkuDetailsResponseListener)}
   *
   * @param isIabV9Supported whether InAppBilling version 9 is supported by Play Store.
   * @param enablePendingPurchases whether to enable pending purchases in purchase flow.
   * @return extraParams bundle.
   */
  public static Bundle constructExtraParamsForGetSkuDetails(
      boolean isIabV9Supported, boolean enablePendingPurchases, String libraryVersion) {
    Bundle extraParams = new Bundle();
    if (isIabV9Supported) {
      extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
    }
    if (isIabV9Supported && enablePendingPurchases) {
      extraParams.putBoolean(EXTRA_PARAMS_ENABLE_PENDING_PURCHASES, true);
    }
    return extraParams;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#consumeAsync(ConsumeParams,
   * ConsumeResponseListener)}
   *
   * @param consumeParams params to consume purchase
   * @param isIabV9Supported whether InAppBilling version 9 is supported by Play Store.
   * @return extraParams bundle.
   */
  public static Bundle constructExtraParamsForConsume(
      ConsumeParams consumeParams, boolean isIabV9Supported, String libraryVersion) {
    Bundle extraParams = new Bundle();
    if (isIabV9Supported) {
      extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
    }
    String developerPayload = consumeParams.getDeveloperPayload();
    if (isIabV9Supported && !TextUtils.isEmpty(developerPayload)) {
      extraParams.putString(EXTRA_PARAMS_DEVELOPER_PAYLOAD, developerPayload);
    }
    return extraParams;
  }

  /**
   * Constructs bundle to provide extra params to {@link
   * com.android.billingclient.api.BillingClient#acknowledgePurchase(AcknowledgePurchaseParams,
   * AcknowledgePurchaseResponseListener)}
   *
   * @param acknowledgePurchaseParams params to initiate acknowledge purchase action.
   * @return extraParams bundle.
   */
  public static Bundle constructExtraParamsForAcknowledgePurchase(
      AcknowledgePurchaseParams acknowledgePurchaseParams, String libraryVersion) {
    Bundle extraParams = new Bundle();
    extraParams.putString(LIBRARY_VERSION_KEY, libraryVersion);
    String developerPayload = acknowledgePurchaseParams.getDeveloperPayload();
    if (!TextUtils.isEmpty(developerPayload)) {
      extraParams.putString(EXTRA_PARAMS_DEVELOPER_PAYLOAD, developerPayload);
    }
    return extraParams;
  }

  private static Purchase extractPurchase(String purchaseData, String signatureData) {

    if (purchaseData == null || signatureData == null) {
      BillingHelper.logWarn(TAG, "Received a bad purchase data.");
      return null;
    }

    Purchase purchase = null;
    try {
      purchase = new Purchase(purchaseData, signatureData);
    } catch (JSONException e) {
      BillingHelper.logWarn(TAG, "Got JSONException while parsing purchase data: " + e);
    }

    return purchase;
  }
}
