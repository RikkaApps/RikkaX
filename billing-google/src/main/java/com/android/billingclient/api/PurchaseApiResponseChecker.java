/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.os.Bundle;

import com.android.billingclient.api.BillingClient.BillingResponseCode;
import com.android.billingclient.util.BillingHelper;

import java.util.ArrayList;

final class PurchaseApiResponseChecker {

  /**
   * Check validity of the {@link Bundle} returned from getPurchases() or getPurchaseHistory()
   *
   * @param bundle bundle to check.
   * @param logTag tag used in logging.
   * @param methodName name of method where the bundle comes from.
   * @return {@link BillingResult} with response code OK if there's no error.
   */
  static BillingResult checkPurchasesBundleValidity(
      Bundle bundle, String logTag, String methodName) {
    BillingResult internalErrorResult = BillingResults.INTERNAL_ERROR;

    if (bundle == null) {
      BillingHelper.logWarn(logTag, String.format("%s got null owned items list", methodName));
      return internalErrorResult;
    }

    @BillingResponseCode int responseCode = BillingHelper.getResponseCodeFromBundle(bundle, logTag);
    String debugMessage = BillingHelper.getDebugMessageFromBundle(bundle, logTag);
    BillingResult billingResult =
        BillingResult.newBuilder()
            .setResponseCode(responseCode)
            .setDebugMessage(debugMessage)
            .build();

    if (responseCode != BillingResponseCode.OK) {
      BillingHelper.logWarn(
          logTag, String.format("%s failed. Response code: %s", methodName, responseCode));
      return billingResult;
    }

    if (!bundle.containsKey(BillingHelper.RESPONSE_INAPP_ITEM_LIST)
        || !bundle.containsKey(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST)
        || !bundle.containsKey(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST)) {
      BillingHelper.logWarn(
          logTag,
          String.format("Bundle returned from %s doesn't contain required fields.", methodName));
      return internalErrorResult;
    }

    ArrayList<String> ownedSkus = bundle.getStringArrayList(BillingHelper.RESPONSE_INAPP_ITEM_LIST);
    ArrayList<String> purchaseDataList =
        bundle.getStringArrayList(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST);
    ArrayList<String> signatureList =
        bundle.getStringArrayList(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST);

    if (ownedSkus == null) {
      BillingHelper.logWarn(
          logTag, String.format("Bundle returned from %s contains null SKUs list.", methodName));
      return internalErrorResult;
    }

    if (purchaseDataList == null) {
      BillingHelper.logWarn(
          logTag,
          String.format("Bundle returned from %s contains null purchases list.", methodName));
      return internalErrorResult;
    }

    if (signatureList == null) {
      BillingHelper.logWarn(
          logTag,
          String.format("Bundle returned from %s contains null signatures list.", methodName));
      return internalErrorResult;
    }

    return BillingResults.OK;
  }
}
