/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

/**
 * Callback for setup process. This listener's {@link #onBillingSetupFinished} method is called when
 * the setup process is complete.
 */
public interface BillingClientStateListener {
  /**
   * Called to notify that setup is complete.
   *
   * @param billingResult The {@link BillingResult} which returns the status of the setup process.
   */
  void onBillingSetupFinished(BillingResult billingResult);

  /**
   * Called to notify that connection to billing service was lost
   *
   * <p>Note: This does not remove billing service connection itself - this binding to the service
   * will remain active, and you will receive a call to {@link #onBillingSetupFinished} when billing
   * service is next running and setup is complete.
   */
  void onBillingServiceDisconnected();
}
