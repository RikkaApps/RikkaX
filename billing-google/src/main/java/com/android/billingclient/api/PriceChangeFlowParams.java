/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

/**
 * Parameters to launch a price change confirmation flow. (See
 * {@link BillingClient#launchPriceChangeConfirmationFlow}
 */
public class PriceChangeFlowParams {

    private SkuDetails skuDetails;

    public SkuDetails getSkuDetails() {
        return skuDetails;
    }

    /**
     * Constructs a new {@link Builder} instance.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Helps construct {@link PriceChangeFlowParams} that are used to launch a price change
     * confirmation flow
     */
    public static class Builder {
        private SkuDetails skuDetails;

        /**
         * Specify the SKU that has the pending price change.
         *
         * @param skuDetails Required, the sku details object from {@link
         *                   BillingClient#querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)}.
         * @return this to continue construction.
         */
        public Builder setSkuDetails(SkuDetails skuDetails) {
            this.skuDetails = skuDetails;
            return this;
        }

        /**
         * Returns the {@link PriceChangeFlowParams} to initiate price change confirmation flow.
         */
        public PriceChangeFlowParams build() {
            PriceChangeFlowParams params = new PriceChangeFlowParams();
            params.skuDetails = skuDetails;
            return params;
        }
    }
}
