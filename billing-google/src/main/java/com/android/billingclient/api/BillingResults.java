/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import com.android.billingclient.api.BillingClient.BillingResponseCode;

// This must be package private so that its not exposed to developers.
final class BillingResults {
  static final BillingResult API_VERSION_NOT_V3 =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.BILLING_UNAVAILABLE)
          .setDebugMessage(DebugMessages.API_VERSION_NOT_V3)
          .build();

  static final BillingResult API_VERSION_NOT_V9 =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.BILLING_UNAVAILABLE)
          .setDebugMessage(DebugMessages.API_VERSION_NOT_V9)
          .build();

  static final BillingResult BILLING_UNAVAILABLE =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.BILLING_UNAVAILABLE)
          .setDebugMessage(DebugMessages.BILLING_UNAVAILABLE)
          .build();

  static final BillingResult CLIENT_CONNECTING =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.CLIENT_CONNECTING)
          .build();

  static final BillingResult EMPTY_SKU_LIST =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.EMPTY_SKU_LIST)
          .build();

  static final BillingResult EMPTY_SKU_TYPE =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.EMPTY_SKU_TYPE)
          .build();

  static final BillingResult EXTRA_PARAMS_NOT_SUPPORTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.FEATURE_NOT_SUPPORTED)
          .setDebugMessage(DebugMessages.EXTRA_PARAMS_NOT_SUPPORTED)
          .build();

  static final BillingResult FEATURE_NOT_SUPPORTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.FEATURE_NOT_SUPPORTED)
          .setDebugMessage(DebugMessages.FEATURE_NOT_SUPPORTED)
          .build();

  static final BillingResult GET_PURCHASE_HISTORY_NOT_SUPPORTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.FEATURE_NOT_SUPPORTED)
          .setDebugMessage(DebugMessages.GET_PURCHASE_HISTORY_NOT_SUPPORTED)
          .build();

  static final BillingResult INVALID_PURCHASE_TOKEN =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.INVALID_PURCHASE_TOKEN)
          .build();

  static final BillingResult INTERNAL_ERROR =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.ERROR)
          .setDebugMessage(DebugMessages.INTERNAL_ERROR)
          .build();

  static final BillingResult ITEM_UNAVAILABLE =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.ITEM_UNAVAILABLE)
          .setDebugMessage(DebugMessages.ITEM_UNAVAILABLE)
          .build();

  static final BillingResult NULL_SKU =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.NULL_SKU)
          .build();

  static final BillingResult NULL_SKU_TYPE =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.NULL_SKU_TYPE)
          .build();

  static final BillingResult OK =
      BillingResult.newBuilder().setResponseCode(BillingResponseCode.OK).build();

  static final BillingResult SERVICE_DISCONNECTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_DISCONNECTED)
          .setDebugMessage(DebugMessages.SERVICE_DISCONNECTED)
          .build();

  static final BillingResult SERVICE_TIMEOUT =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_TIMEOUT)
          .setDebugMessage(DebugMessages.SERVICE_TIMEOUT)
          .build();

  static final BillingResult SUBSCRIPTIONS_NOT_SUPPORTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.FEATURE_NOT_SUPPORTED)
          .setDebugMessage(DebugMessages.SUBSCRIPTIONS_NOT_SUPPORTED)
          .build();

  static final BillingResult SUBSCRIPTIONS_UPDATE_NOT_SUPPORTED =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.FEATURE_NOT_SUPPORTED)
          .setDebugMessage(DebugMessages.SUBSCRIPTIONS_UPDATE_NOT_SUPPORTED)
          .build();

  static final BillingResult UNKNOWN_FEATURE =
      BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .setDebugMessage(DebugMessages.UNKNOWN_FEATURE)
          .build();
}
