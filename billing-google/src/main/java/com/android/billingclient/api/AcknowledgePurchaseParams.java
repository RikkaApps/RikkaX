/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import androidx.annotation.Nullable;

/**
 * Parameters to acknowledge a purchase. (See {@link
 * BillingClient#acknowledgePurchase(AcknowledgePurchaseParams, ConsumeResponseListener)}
 */
public final class AcknowledgePurchaseParams {
  private String developerPayload;
  private String purchaseToken;

  private AcknowledgePurchaseParams() {}

  /** Returns developer data associated with the purchase to be acknowledged. */
  @Nullable
  public String getDeveloperPayload() {
    return developerPayload;
  }

  /** Returns token that identifies the purchase to be acknowledged */
  public String getPurchaseToken() {
    return purchaseToken;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /** Helps construct {@link AcknowledgePurchaseParams} that are used to acknowledge a purchase. */
  public static final class Builder {
    private String developerPayload;
    private String purchaseToken;

    private Builder() {}

    /**
     * Specify the token that identifies the purchase to be acknowledged.
     *
     * <p>Mandatory.
     */
    public Builder setPurchaseToken(String purchaseToken) {
      this.purchaseToken = purchaseToken;
      return this;
    }

    /**
     * Specify developer payload be sent back with the purchase information.
     *
     * <p>Optional.
     */
    public Builder setDeveloperPayload(String developerPayload) {
      this.developerPayload = developerPayload;
      return this;
    }

    /** Returns {@link AcknowledgePurchaseParams} reference to initiate acknowledge action. */
    public AcknowledgePurchaseParams build() {
      AcknowledgePurchaseParams params = new AcknowledgePurchaseParams();
      params.developerPayload = developerPayload;
      params.purchaseToken = purchaseToken;
      return params;
    }
  }
}
