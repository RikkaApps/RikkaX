/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

/**
 * Debug messages set in the BillingResult. These messages are returned if the in app billing api
 * call fails before a server call is made.
 */
// This must be package private so that its not exposed to developers.
final class DebugMessages {

  static final String API_VERSION_NOT_V3 = "Google Play In-app Billing API version is less than 3";

  static final String API_VERSION_NOT_V9 = "Google Play In-app Billing API version is less than 9";

  static final String BILLING_UNAVAILABLE = "Billing service unavailable on device.";

  static final String CLIENT_CONNECTING =
      "Client is already in the process of connecting to billing service.";

  static final String EMPTY_SKU_TYPE = "SKU type can't be empty.";

  static final String EMPTY_SKU_LIST = "The list of SKUs can't be empty.";

  static final String ERROR_DECODING_SKU_DETAILS = "Error trying to decode SkuDetails.";

  static final String EXTRA_PARAMS_NOT_SUPPORTED = "Client does not support extra params.";

  static final String FEATURE_NOT_SUPPORTED = "Client does not support the feature.";

  static final String GET_PURCHASE_HISTORY_NOT_SUPPORTED =
      "Client does not support get purchase history.";

  static final String INTERNAL_ERROR = "An internal error occurred.";

  static final String INVALID_PURCHASE_TOKEN = "Invalid purchase token.";

  static final String ITEM_UNAVAILABLE = "Item is unavailable for purchase.";

  static final String NULL_SKU = "SKU can't be null.";

  static final String NULL_SKU_DETAILS_LIST = "Null sku details list";

  static final String NULL_SKU_TYPE = "SKU type can't be null.";

  static final String SERVICE_DISCONNECTED = "Service connection is disconnected.";

  static final String SERVICE_TIMEOUT = "Timeout communicating with service.";

  static final String SUBSCRIPTIONS_NOT_SUPPORTED = "Client doesn't support subscriptions.";

  static final String SUBSCRIPTIONS_UPDATE_NOT_SUPPORTED =
      "Client doesn't support subscriptions update.";

  static final String UNKNOWN_FEATURE = "Unknown feature";
}
