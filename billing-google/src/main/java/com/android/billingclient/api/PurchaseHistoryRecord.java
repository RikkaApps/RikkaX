/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app billing purchase history record.
 *
 * <p>This class includes a subset of fields in {@link Purchase}.
 */
public class PurchaseHistoryRecord {
  private final String mOriginalJson;
  private final String mSignature;
  private final JSONObject mParsedJson;

  public PurchaseHistoryRecord(String jsonPurchaseInfo, String signature) throws JSONException {
    mOriginalJson = jsonPurchaseInfo;
    mSignature = signature;
    mParsedJson = new JSONObject(mOriginalJson);
  }

  /** Returns the product Id. */
  public String getSku() {
    return mParsedJson.optString("productId");
  }

  /** Returns the time the product was purchased, in milliseconds since the epoch (Jan 1, 1970). */
  public long getPurchaseTime() {
    return mParsedJson.optLong("purchaseTime");
  }

  /** Returns a token that uniquely identifies a purchase for a given item and user pair. */
  public String getPurchaseToken() {
    return mParsedJson.optString("token", mParsedJson.optString("purchaseToken"));
  }

  /** Returns the payload specified when the purchase was acknowledged or consumed. */
  @Nullable
  public String getDeveloperPayload() {
    return mParsedJson.optString("developerPayload");
  }

  /** Returns a String in JSON format that contains details about the purchase order. */
  public String getOriginalJson() {
    return mOriginalJson;
  }

  /**
   * Returns String containing the signature of the purchase data that was signed with the private
   * key of the developer. The data signature uses the RSASSA-PKCS1-v1_5 scheme.
   */
  public String getSignature() {
    return mSignature;
  }

  @Override
  public String toString() {
    return "PurchaseHistoryRecord. Json: " + mOriginalJson;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof PurchaseHistoryRecord)) {
      return false;
    }

    PurchaseHistoryRecord purchase = (PurchaseHistoryRecord) o;

    return TextUtils.equals(mOriginalJson, purchase.getOriginalJson())
        && TextUtils.equals(mSignature, purchase.getSignature());
  }

  @Override
  public int hashCode() {
    return mOriginalJson.hashCode();
  }
}
