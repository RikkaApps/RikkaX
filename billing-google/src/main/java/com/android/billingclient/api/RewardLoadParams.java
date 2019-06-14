/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import org.json.JSONException;

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

    // Used by C++.
    // C++ PBL sets original skuDetails directly and this is used to parse string into JSON object.
    private Builder setSkuDetails(String originalSkuDetails) {
      try {
        this.skuDetails = new SkuDetails(originalSkuDetails);
      } catch (JSONException ex) {
        throw new RuntimeException("Incorrect skuDetails JSON object!");
      }
      return this;
    }

    /** Returns {@link RewardLoadParams} reference to initiate load. */
    public RewardLoadParams build() {
      RewardLoadParams params = new RewardLoadParams();
      if (skuDetails != null) {
        params.skuDetails = skuDetails;
      } else {
        throw new RuntimeException("SkuDetails must be set");
      }
      return params;
    }
  }
}
