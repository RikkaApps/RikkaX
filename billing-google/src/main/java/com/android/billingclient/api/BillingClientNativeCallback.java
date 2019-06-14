/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement"). By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */
package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.BillingResponseCode;

import java.util.Collections;
import java.util.List;

/** Callbacks that notifies caller important billing events. */
class BillingClientNativeCallback
    implements AcknowledgePurchaseResponseListener,
        BillingClientStateListener,
        ConsumeResponseListener,
        PriceChangeConfirmationListener,
        PurchaseHistoryResponseListener,
        PurchasesUpdatedListener,
        RewardResponseListener,
        SkuDetailsResponseListener {

  // Future handle for callbacks.
  private final long futureHandle;

  BillingClientNativeCallback() {
    this.futureHandle = 0;
  }

  BillingClientNativeCallback(long futureHandle) {
    this.futureHandle = futureHandle;
  }

  @Override
  public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
    if (skuDetailsList == null) {
      // Do not return null list.
      skuDetailsList = Collections.emptyList();
    }
    SkuDetails[] skuDetailsArray = skuDetailsList.toArray(new SkuDetails[skuDetailsList.size()]);
    nativeOnSkuDetailsResponse(
        billingResult.getResponseCode(),
        billingResult.getDebugMessage(),
        skuDetailsArray,
        futureHandle);
  }

  @Override
  public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
    nativeOnAcknowledgePurchaseResponse(
        billingResult.getResponseCode(), billingResult.getDebugMessage(), futureHandle);
  }

  @Override
  public void onBillingSetupFinished(BillingResult billingResult) {
    nativeOnBillingSetupFinished(
        billingResult.getResponseCode(), billingResult.getDebugMessage(), futureHandle);
  }

  @Override
  public void onBillingServiceDisconnected() {
    nativeOnBillingServiceDisconnected();
  }

  @Override
  public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
    nativeOnConsumePurchaseResponse(
        billingResult.getResponseCode(),
        billingResult.getDebugMessage(),
        purchaseToken,
        futureHandle);
  }

  @Override
  public void onPriceChangeConfirmationResult(BillingResult billingResult) {
    nativeOnPriceChangeConfirmationResult(
        billingResult.getResponseCode(), billingResult.getDebugMessage(), futureHandle);
  }

  @Override
  public void onPurchaseHistoryResponse(
      BillingResult billingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList) {
    if (purchaseHistoryRecordList == null) {
      // Do not return null list.
      purchaseHistoryRecordList = Collections.emptyList();
    }
    PurchaseHistoryRecord[] purchaseHistoryArray =
        purchaseHistoryRecordList.toArray(
            new PurchaseHistoryRecord[purchaseHistoryRecordList.size()]);
    nativeOnPurchaseHistoryResponse(
        billingResult.getResponseCode(),
        billingResult.getDebugMessage(),
        purchaseHistoryArray,
        futureHandle);
  }

  @Override
  public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
    if (purchases == null) {
      // Do not return null list.
      purchases = Collections.emptyList();
    }
    Purchase[] purchaseArray = purchases.toArray(new Purchase[purchases.size()]);
    nativeOnPurchasesUpdated(
        billingResult.getResponseCode(), billingResult.getDebugMessage(), purchaseArray);
  }

  @Override
  public void onRewardResponse(BillingResult billingResult) {
    nativeOnRewardResponse(
        billingResult.getResponseCode(), billingResult.getDebugMessage(), futureHandle);
  }

  // Package visible only.
  void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> purchases) {
    if (purchases == null) {
      // Do not return null list.
      purchases = Collections.emptyList();
    }
    Purchase[] purchaseArray = purchases.toArray(new Purchase[purchases.size()]);
    nativeOnQueryPurchasesResponse(
        billingResult.getResponseCode(),
        billingResult.getDebugMessage(),
        purchaseArray,
        futureHandle);
  }

  /**
   * Called to notify that an acknowledge operation has finished.
   *
   * @param responseCode The response code from {@link BillingResponseCode} set to report the result
   *     of consume operation.
   * @param debugMessage Debug message of this request.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnAcknowledgePurchaseResponse(
      int responseCode, String debugMessage, long futureHandle);

  /**
   * Called to notify that a consume operation has finished.
   *
   * @param responseCode The response code from {@link BillingResponseCode} set to report the result
   *     of consume operation.
   * @param debugMessage Debug message of this request.
   * @param purchaseToken The purchase token that was (or was to be) consumed.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnConsumePurchaseResponse(
      int responseCode, String debugMessage, String purchaseToken, long futureHandle);

  /**
   * Called to notify that setup is complete.
   *
   * @param responseCode The response code from {@link BillingResponseCode} which returns the status
   *     of the setup process.
   * @param debugMessage Debug message of this request.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnBillingSetupFinished(
      int responseCode, String debugMessage, long futureHandle);

  /**
   * Called to notify that connection to billing service was lost
   *
   * <p>Note: This does not remove billing service connection itself - this binding to the service
   * will remain active, and you will receive a call to {@link #nativeOnBillingSetupFinished} when
   * billing service is next running and setup is complete.
   */
  public static native void nativeOnBillingServiceDisconnected();

  /**
   * Implement this method to get notifications for purchases updates. Both purchases initiated by
   * your app and the ones initiated by Play Store will be reported here.
   *
   * @param responseCode Response code of the update.
   * @param debugMessage Debug message of the update.
   * @param purchaseArray Array of updated purchases in original JSON format, if present.
   */
  public static native void nativeOnPurchasesUpdated(
      int responseCode, String debugMessage, Purchase[] purchaseArray);

  /**
   * Called to notify that purchase history fetch operation has finished.
   *
   * @param responseCode Response code of the query.
   * @param debugMessage Debug message of the query.
   * @param purchaseHistoryArray Array of purchases (even if that purchase is expired, canceled, or
   *     consumed - up to 1 per each SKU) or null with corresponding {@link BillingResponseCode}
   *     responseCode if purchase history was not queried successfully.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnPurchaseHistoryResponse(
      int responseCode,
      String debugMessage,
      PurchaseHistoryRecord[] purchaseHistoryArray,
      long futureHandle);

  /**
   * Called to notify that purchase history fetch operation has finished.
   *
   * @param responseCode Response code of the query.
   * @param debugMessage Debug message of the query.
   * @param purchaseArray Array of unconsumed purchases.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnQueryPurchasesResponse(
      int responseCode, String debugMessage, Purchase[] purchaseArray, long futureHandle);

  /**
   * Called to notify that a fetch SKU details operation has finished.
   *
   * @param responseCode Response code of the update.
   * @param debugMessage Debug message of this request.
   * @param skuDetailsArray Array of SkuDetails.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnSkuDetailsResponse(
      int responseCode, String debugMessage, SkuDetails[] skuDetailsArray, long futureHandle);

  /**
   * Called to notify when a price change confirmation flow has finished.
   *
   * @param responseCode Response code of the update.
   * @param debugMessage Debug message of this request.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnPriceChangeConfirmationResult(
      int responseCode, String debugMessage, long futureHandle);

  /**
   * Called to notify that a load reward operation has finished.
   *
   * @param responseCode Response code of the update.
   * @param debugMessage Debug message of this request.
   * @param futureHandle Future handle id.
   */
  public static native void nativeOnRewardResponse(
      int responseCode, String debugMessage, long futureHandle);
}
