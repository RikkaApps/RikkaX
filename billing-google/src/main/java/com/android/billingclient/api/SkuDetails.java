/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.text.TextUtils;

import com.android.billingclient.api.BillingClient.BillingResponseCode;
import com.android.billingclient.api.BillingClient.SkuType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/** Represents an in-app product's or subscription's listing details. */
public class SkuDetails {
  private final String mOriginalJson;
  private final JSONObject mParsedJson;

  public SkuDetails(String jsonSkuDetails) throws JSONException {
    mOriginalJson = jsonSkuDetails;
    mParsedJson = new JSONObject(mOriginalJson);
  }

  /** Returns a String in JSON format that contains Sku details. */
  public String getOriginalJson() {
    return mOriginalJson;
  }

  /** Returns the product Id. */
  public String getSku() {
    return mParsedJson.optString("productId");
  }

  /** Returns SKU type. */
  @SuppressWarnings("WrongConstant")
  @SkuType
  public String getType() {
    return mParsedJson.optString("type");
  }

  /**
   * Returns formatted price of the item, including its currency sign. The price does not include
   * tax.
   */
  public String getPrice() {
    return mParsedJson.optString("price");
  }

  /**
   * Returns price in micro-units, where 1,000,000 micro-units equal one unit of the currency.
   *
   * <p>For example, if price is "€7.99", price_amount_micros is "7990000". This value represents
   * the localized, rounded price for a particular currency.
   */
  public long getPriceAmountMicros() {
    return mParsedJson.optLong("price_amount_micros");
  }

  /**
   * Returns ISO 4217 currency code for price and original price.
   *
   * <p>For example, if price is specified in British pounds sterling, price_currency_code is "GBP".
   */
  public String getPriceCurrencyCode() {
    return mParsedJson.optString("price_currency_code");
  }

  /**
   * Returns formatted original price of the item, including its currency sign. The price does not
   * include tax.
   *
   * <p>The original price is the price of the item before any applicable sales have been applied.
   */
  public String getOriginalPrice() {
    if (mParsedJson.has("original_price")) {
      return mParsedJson.optString("original_price");
    } else {
      return getPrice();
    }
  }

  /**
   * Returns the original price in micro-units, where 1,000,000 micro-units equal one unit of the
   * currency.
   *
   * <p>The original price is the price of the item before any applicable sales have been applied.
   *
   * <p>For example, if original price is "€7.99", original_price_amount_micros is "7990000". This
   * value represents the localized, rounded price for a particular currency.
   */
  public long getOriginalPriceAmountMicros() {
    if (mParsedJson.has("original_price_micros")) {
      return mParsedJson.optLong("original_price_micros");
    } else {
      return getPriceAmountMicros();
    }
  }

  /** Returns the title of the product. */
  public String getTitle() {
    return mParsedJson.optString("title");
  }

  /** Returns the description of the product. */
  public String getDescription() {
    return mParsedJson.optString("description");
  }

  /**
   * Subscription period, specified in ISO 8601 format. For example, P1W equates to one week, P1M
   * equates to one month, P3M equates to three months, P6M equates to six months, and P1Y equates
   * to one year.
   *
   * <p>Note: Returned only for subscriptions.
   */
  public String getSubscriptionPeriod() {
    return mParsedJson.optString("subscriptionPeriod");
  }

  /**
   * Trial period configured in Google Play Console, specified in ISO 8601 format. For example, P7D
   * equates to seven days. To learn more about free trial eligibility, see In-app Subscriptions.
   *
   * <p>Note: Returned only for subscriptions which have a trial period configured.
   */
  public String getFreeTrialPeriod() {
    return mParsedJson.optString("freeTrialPeriod");
  }

  /**
   * Formatted introductory price of a subscription, including its currency sign, such as €3.99. The
   * price doesn't include tax.
   *
   * <p>Note: Returned only for subscriptions which have an introductory period configured.
   */
  public String getIntroductoryPrice() {
    return mParsedJson.optString("introductoryPrice");
  }

  /**
   * Introductory price in micro-units. The currency is the same as price_currency_code.
   *
   * <p>Note: Returned only for subscriptions which have an introductory period configured.
   */
  public long getIntroductoryPriceAmountMicros() {
    return mParsedJson.optLong("introductoryPriceAmountMicros");
  }

  /**
   * The billing period of the introductory price, specified in ISO 8601 format.
   *
   * <p>Note: Returned only for subscriptions which have an introductory period configured.
   */
  public String getIntroductoryPricePeriod() {
    return mParsedJson.optString("introductoryPricePeriod");
  }

  /**
   * The number of subscription billing periods for which the user will be given the introductory
   * price, such as 3.
   *
   * <p>Note: Returned only for subscriptions which have an introductory period configured.
   */
  public String getIntroductoryPriceCycles() {
    return mParsedJson.optString("introductoryPriceCycles");
  }

  /**
   * Returns the icon of the product if present.
   */
  public String getIconUrl() {
    return mParsedJson.optString("iconUrl");
  }

  /**
   * Returns true if sku is rewarded instead of paid.  If rewarded, developer should call
   * {@link BillingClient#loadRewardedSku} before attempting to launch purchase for in order to
   * ensure the reward is available to the user.
   */
  public boolean isRewarded() {
    return mParsedJson.has("rewardToken");
  }

  /**
   * Internal billing library getter to pass along SKU details server token to getBuyIntent.
   */
  /* package */ String getSkuDetailsToken() {
    return mParsedJson.optString("skuDetailsToken");
  }

  /** Internal billing library getter to pass along reward information to getBuyIntent. */
  /* package */ String rewardToken() {
    return mParsedJson.optString("rewardToken");
  }

  @Override
  public String toString() {
    return "SkuDetails: " + mOriginalJson;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SkuDetails details = (SkuDetails) o;

    return TextUtils.equals(mOriginalJson, details.mOriginalJson);
  }

  @Override
  public int hashCode() {
    return mOriginalJson.hashCode();
  }

  /**
   * Result list and code for querySkuDetailsAsync method.
   * TODO(b/122656268): move this into BillingClientImpl class.
   */
  public static class SkuDetailsResult {
    private List<SkuDetails> mSkuDetailsList;
    @BillingResponseCode private int mResponseCode;
    private String mDebugMessage;

    public SkuDetailsResult(
        @BillingResponseCode int responseCode,
        String debugMessage,
        List<SkuDetails> skuDetailsList) {
      this.mResponseCode = responseCode;
      this.mDebugMessage = debugMessage;
      this.mSkuDetailsList = skuDetailsList;
    }

    public List<SkuDetails> getSkuDetailsList() {
      return mSkuDetailsList;
    }

    @BillingResponseCode
    public int getResponseCode() {
      return mResponseCode;
    }

    public String getDebugMessage() {
      return mDebugMessage;
    }
  }
}
