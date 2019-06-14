/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.BillingResponseCode;

/** Params containing the response code and the debug message from In-app Billing API response. */
public final class BillingResult {
  private @BillingResponseCode int mResponseCode;
  private String mDebugMessage;

  /** Response code returned in In-app Billing API calls. */
  public @BillingResponseCode int getResponseCode() {
    return mResponseCode;
  }

  /** Debug message returned in In-app Billing API calls. */
  public String getDebugMessage() {
    return mDebugMessage;
  }

  /** Constructs a new {@link BillingResult.Builder} instance. */
  public static BillingResult.Builder newBuilder() {
    return new BillingResult.Builder();
  }

  /** Helps to construct {@link BillingResult} that are used to return response from IAB api. */
  public static class Builder {
    private @BillingResponseCode int mResponseCode;
    private String mDebugMessage;

    private Builder() {}

    public BillingResult.Builder setResponseCode(@BillingResponseCode int responseCode) {
      this.mResponseCode = responseCode;
      return this;
    }

    public BillingResult.Builder setDebugMessage(String debugMessage) {
      this.mDebugMessage = debugMessage;
      return this;
    }

    /** Returns {@link BillingResult} reference. */
    public BillingResult build() {
      BillingResult billingResult = new BillingResult();
      billingResult.mResponseCode = this.mResponseCode;
      billingResult.mDebugMessage = this.mDebugMessage;
      return billingResult;
    }
  }
}
