/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

/** Parameters to load a rewarded SKU. (See {@link BillingClient#loadRewardedSku} */
public class RewardLoadParams {

  private SkuDetails skuDetails;

  public SkuDetails getSkuDetails() {
    return skuDetails;
  }

  /** Constructs a new {@link Builder} instance. */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Helps construct {@link RewardLoadParams} that are used to load rewarded SKUs */
  public static class Builder {
    private SkuDetails skuDetails;

    /**
     * Specify the SKU to load
     *
     * @param skuDetails Required, the sku details object from {@link
     *     BillingClient#querySkuDetailsAsync(SkuDetailsParams, SkuDetailsResponseListener)}.
     * @return this to continue construction.
     */
    public Builder setSkuDetails(SkuDetails skuDetails) {
      this.skuDetails = skuDetails;
      return this;
    }

    /** Returns {@link RewardLoadParams} reference to initiate load. */
    public RewardLoadParams build() {
      RewardLoadParams params = new RewardLoadParams();
      params.skuDetails = skuDetails;
      return params;
    }
  }
}
