/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement"). By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.BillingResponseCode;

import java.util.List;

/** Listener to a result of purchases history query. */
public interface PurchaseHistoryResponseListener {
  /**
   * Called to notify that purchase history fetch operation has finished.
   *
   * @param billingResult BillingResult of the query.
   * @param purchaseHistoryRecordList List of purchase records (even if that purchase is expired,
   *     canceled, or consumed - up to 1 per each SKU) or null with corresponding {@link
   *     BillingResponseCode} responseCode if purchase history was not queried successfully.
   */
  void onPurchaseHistoryResponse(
      BillingResult billingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList);
}
