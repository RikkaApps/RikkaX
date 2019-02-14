/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.BillingResponse;

import java.util.List;

/**
 * Listener to a result of purchases history query.
 */
public interface PurchaseHistoryResponseListener {
    /**
     * Called to notify that purchase history fetch operation has finished.
     *
     * @param responseCode  Response code of the query.
     * @param purchasesList List of purchases (even if that purchase is expired, canceled, or consumed
     *                      - up to 1 per each SKU) or null with corresponding {@link BillingResponse} responseCode if
     *                      purchase history was not queried successfully.
     */
    void onPurchaseHistoryResponse(@BillingResponse int responseCode, List<Purchase> purchasesList);
}
