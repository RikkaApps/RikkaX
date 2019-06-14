/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;


/** Listener to a result of acknowledge purchase request */
public interface AcknowledgePurchaseResponseListener {

  /**
   * Called to notify that an acknowledge purchase operation has finished
   *
   * @param billingResult BillingResult of the update.
   */
  void onAcknowledgePurchaseResponse(BillingResult billingResult);
}
