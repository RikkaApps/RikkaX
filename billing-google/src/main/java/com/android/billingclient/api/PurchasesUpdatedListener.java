/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Listener interface for purchase updates which happen when, for example, the user buys something
 * within the app or by initiating a purchase from Google Play Store.
 */
public interface PurchasesUpdatedListener {
  /**
   * Implement this method to get notifications for purchases updates. Both purchases initiated by
   * your app and the ones initiated by Play Store will be reported here.
   *
   * @param billingResult BillingResult of the update.
   * @param purchases List of updated purchases if present.
   */
  void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases);
}
