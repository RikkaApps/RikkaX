/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient.BillingResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/** Represents an in-app billing purchase. */
public class Purchase {
  /** Possible purchase states.*/
  @IntDef({
      PurchaseState.UNSPECIFIED_STATE,
      PurchaseState.PURCHASED,
      PurchaseState.PENDING,
  })

  @Retention(SOURCE)
  public @interface PurchaseState {
    // Purchase with unknown state.
    int UNSPECIFIED_STATE = 0;
    // Purchase is completed.
    int PURCHASED = 1;
    // Purchase is waiting for payment completion.
    int PENDING = 2;
  }

  private final String mOriginalJson;
  private final String mSignature;
  private final JSONObject mParsedJson;

  public Purchase(String jsonPurchaseInfo, String signature) throws JSONException {
    mOriginalJson = jsonPurchaseInfo;
    mSignature = signature;
    mParsedJson = new JSONObject(mOriginalJson);
  }

  /**
   * Returns an unique order identifier for the transaction. This identifier corresponds to the
   * Google payments order ID.
   */
  public String getOrderId() {
    return mParsedJson.optString("orderId");
  }

  /** Returns the application package from which the purchase originated. */
  public String getPackageName() {
    return mParsedJson.optString("packageName");
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

  /** Returns the state of purchase. */
  public @PurchaseState int getPurchaseState() {
    switch (mParsedJson.optInt("purchaseState", PurchaseState.PURCHASED)) {
      case 4:
        return PurchaseState.PENDING;
      default:
        return PurchaseState.PURCHASED;
    }
  }

  /** Returns the payload specified when the purchase was acknowledged or consumed. */
  @Nullable
  public String getDeveloperPayload() {
    return mParsedJson.optString("developerPayload");
  }

  /**
   * Indicates whether the purchase has been acknowledged.
   */
  public boolean isAcknowledged() {
    // Always returns true for purchases before acknowledgePurchase API is introduced.
    return mParsedJson.optBoolean("acknowledged", true);
  }

  /**
   * Indicates whether the subscription renews automatically. If true, the subscription is active,
   * and will automatically renew on the next billing date. If false, indicates that the user has
   * canceled the subscription. The user has access to subscription content until the next billing
   * date and will lose access at that time unless they re-enable automatic renewal (or manually
   * renew, as described in Manual Renewal). If you offer a grace period, this value remains set to
   * true for all subscriptions, as long as the grace period has not lapsed. The next billing date
   * is extended dynamically every day until the end of the grace period or until the user fixes
   * their payment method.
   */
  public boolean isAutoRenewing() {
    return mParsedJson.optBoolean("autoRenewing");
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
    return "Purchase. Json: " + mOriginalJson;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Purchase)) {
      return false;
    }

    Purchase purchase = (Purchase) o;

    return TextUtils.equals(mOriginalJson, purchase.getOriginalJson())
        && TextUtils.equals(mSignature, purchase.getSignature());
  }

  @Override
  public int hashCode() {
    return mOriginalJson.hashCode();
  }

  /** Result list and code for queryPurchases method */
  public static class PurchasesResult {
    private List<Purchase> mPurchaseList;
    private BillingResult mBillingResult;

    public PurchasesResult(BillingResult mBillingResult, List<Purchase> purchasesList) {
      this.mPurchaseList = purchasesList;
      this.mBillingResult = mBillingResult;
    }

    /** Returns the {@link BillingResult} of the operation. */
    public BillingResult getBillingResult() {
      return mBillingResult;
    }

    /** Returns the response code of In-app Billing API calls. */
    public @BillingResponseCode int getResponseCode() {
      return getBillingResult().getResponseCode();
    }

    /** Returns the list of {@link Purchase}. */
    public List<Purchase> getPurchasesList() {
      return mPurchaseList;
    }
  }
}
