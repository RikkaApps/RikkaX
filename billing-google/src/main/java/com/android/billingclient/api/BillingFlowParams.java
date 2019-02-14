/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.SkuType;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.IntDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Parameters to initiate a purchase flow. (See {@link BillingClient#launchBillingFlow}).
 */
public class BillingFlowParams {

    static final String EXTRA_PARAM_KEY_ACCOUNT_ID = "accountId";
    static final String EXTRA_PARAM_KEY_REPLACE_SKUS_PRORATION_MODE = "prorationMode";
    static final String EXTRA_PARAM_KEY_VR = "vr";
    static final String EXTRA_PARAM_KEY_RSKU = "rewardToken";
    static final String EXTRA_PARAM_CHILD_DIRECTED = "childDirected";
    static final String EXTRA_PARAM_KEY_OLD_SKUS = "skusToReplace";

    /**
     * Replace SKU ProrationMode.
     */
    @IntDef({
            ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY,
            ProrationMode.IMMEDIATE_WITH_TIME_PRORATION,
            ProrationMode.IMMEDIATE_AND_CHARGE_PRORATED_PRICE,
            ProrationMode.IMMEDIATE_WITHOUT_PRORATION,
            ProrationMode.DEFERRED
    })
    @Retention(SOURCE)
    public @interface ProrationMode {

        int UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY = 0;

        /**
         * Replacement takes effect immediately, and the remaining time will be prorated and credited to
         * the user. This is the current default behavior.
         */
        int IMMEDIATE_WITH_TIME_PRORATION = 1;

        /**
         * Replacement takes effect immediately, and the billing cycle remains the same. The price for
         * the remaining period will be charged. This option is only available for subscription upgrade.
         */
        int IMMEDIATE_AND_CHARGE_PRORATED_PRICE = 2;

        /**
         * Replacement takes effect immediately, and the new price will be charged on next recurrence
         * time. The billing cycle stays the same.
         */
        int IMMEDIATE_WITHOUT_PRORATION = 3;

        /**
         * Replacement takes effect when the old plan expires, and the new price will be charged at the
         * same time.
         */
        int DEFERRED = 4;
    }

    private String mSku;
    @SkuType
    private String mSkuType;
    private SkuDetails mSkuDetails;
    private String mOldSku;
    private String mAccountId;
    private boolean mVrPurchaseFlow;
    @ProrationMode
    private int mReplaceSkusProrationMode =
            ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY;

    /**
     * Returns the SKU that is being purchased or upgraded/downgraded to as published in the Google
     * Developer console.
     */
    public String getSku() {
        if (mSkuDetails != null) {
            return mSkuDetails.getSku();
        }
        return mSku;
    }

    /**
     * Returns the billing type {@link SkuType} of the item being purchased.
     */
    @SkuType
    public String getSkuType() {
        if (mSkuDetails != null) {
            return mSkuDetails.getType();
        }
        return mSkuType;
    }

    /**
     * Returns the full sku details for this purchase.
     */
    public SkuDetails getSkuDetails() {
        return mSkuDetails;
    }

    /**
     * Returns the SKU(s) that the user is upgrading or downgrading from.
     *
     * @deprecated Use {@link BillingFlowParams#getOldSku} instead.
     */
    @Deprecated
    public ArrayList<String> getOldSkus() {
        return new ArrayList<>(Arrays.asList(mOldSku));
    }

    /**
     * Returns the SKU that the user is upgrading or downgrading from.
     */
    public String getOldSku() {
        return mOldSku;
    }

    /**
     * Returns an optional obfuscated string that is uniquely associated with the user's account.
     */
    public String getAccountId() {
        return mAccountId;
    }

    /**
     * Returns an optional flag indicating whether you wish to launch a VR purchase flow.
     */
    public boolean getVrPurchaseFlow() {
        return mVrPurchaseFlow;
    }

    /**
     * Returns an optional integer that indicates the Replace SKU ProrationMode.
     */
    @ProrationMode
    public int getReplaceSkusProrationMode() {
        return mReplaceSkusProrationMode;
    }

    /**
     * Returns whether it has an optional params for a custom purchase flow.
     */
    public boolean hasExtraParams() {
        return mVrPurchaseFlow
                || mAccountId != null
                || (mReplaceSkusProrationMode
                != ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY);
    }

    /**
     * Constructs a new {@link Builder} instance.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Helps to construct {@link BillingFlowParams} that are used to initiate a purchase flow.
     */
    public static class Builder {
        private String mSku;
        @SkuType
        private String mSkuType;
        private SkuDetails mSkuDetails;
        private String mOldSku;
        private String mAccountId;
        private boolean mVrPurchaseFlow;
        @ProrationMode
        private int mReplaceSkusProrationMode =
                ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY;

        private Builder() {
        }

        /**
         * Specify the SKU that is being purchased or upgraded/downgraded to as published in the Google
         * Developer console.
         *
         * @deprecated Use {@link #setSkuDetails(SkuDetails)} instead
         */
        @Deprecated
        public Builder setSku(String sku) {
            if (mSkuDetails != null) {
                throw new RuntimeException("Sku details already set");
            }
            this.mSku = sku;
            return this;
        }

        /**
         * Specify the billing type {@link SkuType} of the item being purchased.
         *
         * @deprecated Use {@link #setSkuDetails(SkuDetails)} instead
         */
        @Deprecated
        public Builder setType(@SkuType String type) {
            if (mSkuDetails != null) {
                throw new RuntimeException("Sku details already set");
            }
            this.mSkuType = type;
            return this;
        }

        /**
         * Specify the SkuDetails {@link SkuDetails} of the item being purchase.
         *
         * <p>Mandatory:
         *
         * <ul>
         * <li>To buy in-app item
         * <li>To create a new subscription
         * <li>To replace an old subscription
         * </ul>
         */
        public Builder setSkuDetails(SkuDetails skuDetails) {
            if (mSku != null || mSkuType != null) {
                throw new RuntimeException("Sku or type already set");
            }
            this.mSkuDetails = skuDetails;
            return this;
        }

        /**
         * Specify the SKU(s) that the user is upgrading or downgrading from.
         *
         * <p>Mandatory:
         *
         * <ul>
         * <li>To replace an old subscription
         * </ul>
         *
         * @deprecated Use {@link Builder#setOldSku} instead.
         */
        @Deprecated
        public Builder setOldSkus(ArrayList<String> oldSkus) {
            if (oldSkus != null && oldSkus.size() > 0) {
                this.mOldSku = oldSkus.get(0);
            }
            return this;
        }

        /**
         * Specify the SKU that the user is upgrading or downgrading from.
         *
         * <p>Mandatory:
         *
         * <ul>
         * <li>To replace an old subscription
         * </ul>
         */
        public Builder setOldSku(String oldSku) {
            this.mOldSku = oldSku;
            return this;
        }

        /**
         * Add the SKU that the user is upgrading or downgrading from.
         *
         * <p>Mandatory:
         *
         * <ul>
         * <li>To replace an old subscription
         * </ul>
         */
        @Deprecated
        public Builder addOldSku(String oldSku) {
            this.mOldSku = oldSku;
            return this;
        }

        /**
         * Specifies the mode of proration during subscription upgrade/downgrade. This value will only
         * be effective if oldSkus is set.
         *
         * <p> If you set this to NO_PRORATION, the user does not receive credit or charge, and the
         * recurrence date does not change.
         *
         * <p>If you set this to PRORATE_BY_TIME, Google Play swaps out the old SKUs and credits the
         * user with the unused value of their subscription time on a pro-rated basis. Google Play
         * applies this credit to the new subscription, and does not begin billing the user for the new
         * subscription until after the credit is used up.
         *
         * <p>If you set this to PRORATE_BY_PRICE, Google Play swaps out the old SKUs and keeps the
         * recurrence date not changed. User will be charged for the price differences to cover the
         * time till next recurrence date.
         *
         * <p>Optional:
         *
         * <ul>
         * <li>To buy in-app item
         * <li>To create a new subscription
         * <li>To replace an old subscription
         * </ul>
         */
        public Builder setReplaceSkusProrationMode(@ProrationMode int replaceSkusProrationMode) {
            this.mReplaceSkusProrationMode = replaceSkusProrationMode;
            return this;
        }

        /**
         * Specify an optional obfuscated string that is uniquely associated with the user's account in
         * your app.
         *
         * <p>If you pass this value, Google Play can use it to detect irregular activity, such as many
         * devices making purchases on the same account in a short period of time. Do not use the
         * developer ID or the user's Google ID for this field. In addition, this field should not
         * contain the user's ID in cleartext. We recommend that you use a one-way hash to generate a
         * string from the user's ID and store the hashed string in this field.
         *
         * <p>Optional:
         *
         * <ul>
         * <li>To buy in-app item
         * <li>To create a new subscription
         * <li>To replace an old subscription
         * </ul>
         */
        public Builder setAccountId(String accountId) {
            this.mAccountId = accountId;
            return this;
        }

        /**
         * Specify an optional flag indicating whether you wish to launch a VR purchase flow.
         *
         * <p>Optional:
         *
         * <ul>
         * <li>To buy in-app item
         * <li>To create a new subscription
         * <li>To replace an old subscription
         * </ul>
         */
        public Builder setVrPurchaseFlow(boolean isVrPurchaseFlow) {
            this.mVrPurchaseFlow = isVrPurchaseFlow;
            return this;
        }

        /**
         * Returns {@link BillingFlowParams} reference to initiate a purchase flow.
         */
        public BillingFlowParams build() {
            BillingFlowParams params = new BillingFlowParams();
            params.mSku = this.mSku;
            params.mSkuType = this.mSkuType;
            params.mSkuDetails = this.mSkuDetails;
            params.mOldSku = this.mOldSku;
            params.mAccountId = this.mAccountId;
            params.mVrPurchaseFlow = this.mVrPurchaseFlow;
            params.mReplaceSkusProrationMode = this.mReplaceSkusProrationMode;
            return params;
        }
    }
}
