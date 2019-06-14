/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;


/** Callback that notifies when a consumption operation finishes. */
public interface ConsumeResponseListener {
  /**
   * Called to notify that a consume operation has finished.
   *
   * @param billingResult BillingResult, the result of the consume operation.
   * @param purchaseToken The purchase token that was (or was to be) consumed.
   */
  void onConsumeResponse(BillingResult billingResult, String purchaseToken);
}
