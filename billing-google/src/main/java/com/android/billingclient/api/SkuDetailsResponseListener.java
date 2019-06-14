/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import java.util.List;

/** Listener to a result of SKU details query */
public interface SkuDetailsResponseListener {
  /**
   * Called to notify that a fetch SKU details operation has finished.
   *
   * @param billingResult BillingResult of the update.
   * @param skuDetailsList List of SKU details.
   */
  void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList);
}
