/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

/**
 * Listener to a result of the price change confirmation flow.
 */
public interface PriceChangeConfirmationListener {

    /**
     * Called to notify when a price change confirmation flow has finished.
     *
     * @param responseCode Response code of the update.
     */
    void onPriceChangeConfirmationResult(@BillingClient.BillingResponse int responseCode);
}
