/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */
package com.android.billingclient.api;

/** Listener to a result of load reward request */
public interface RewardResponseListener {

  /**
   * Called to notify that a load reward operation has finished.
   *
   * @param billingResult BillingResult of the update.
   */
  void onRewardResponse(BillingResult billingResult);
}
