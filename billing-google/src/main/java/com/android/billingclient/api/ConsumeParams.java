/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import androidx.annotation.Nullable;

/**
 * Parameters to consume a purchase. (See {@link BillingClient#consumeAsync(ConsumeParams,
 * ConsumeResponseListener)}
 */
public final class ConsumeParams {
  private String purchaseToken;
  private String developerPayload;

  private ConsumeParams() {}

  /** Returns developer data associated with the purchase to be consumed */
  @Nullable
  public String getDeveloperPayload() {
    return developerPayload;
  }

  /** Returns token that identifies the purchase to be consumed */
  public String getPurchaseToken() {
    return purchaseToken;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /** Helps construct {@link ConsumeParams} that are used to consume a purchase. */
  public static final class Builder {
    private String developerPayload;
    private String purchaseToken;

    private Builder() {}

    /**
     * Specify the token that identifies the purchase to be consumed.
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

    /** Returns {@link ConsumeParams} reference to initiate consume action. */
    public ConsumeParams build() {
      ConsumeParams params = new ConsumeParams();
      params.purchaseToken = purchaseToken;
      params.developerPayload = developerPayload;
      return params;
    }
  }
}
