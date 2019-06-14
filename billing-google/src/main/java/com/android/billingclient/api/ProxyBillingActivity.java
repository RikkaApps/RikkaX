/**
 * Play Billing Library is licensed to you under the Android Software Development Kit License
 * Agreement - https://developer.android.com/studio/terms ("Agreement").  By using the Play Billing
 * Library, you agree to the terms of this Agreement.
 */

package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.android.billingclient.api.BillingClient.BillingResponseCode;
import com.android.billingclient.util.BillingHelper;

import static com.android.billingclient.util.BillingHelper.RESPONSE_BUY_INTENT_KEY;
import static com.android.billingclient.util.BillingHelper.RESPONSE_SUBS_MANAGEMENT_INTENT_KEY;

/**
 * An invisible activity that launches another billing-related activity and delivers parsed result
 * to the {@link BillingClient} via {@link ResultReceiver}.
 */
public class ProxyBillingActivity extends Activity {
  static final String KEY_RESULT_RECEIVER = "result_receiver";

  private static final String TAG = "ProxyBillingActivity";
  private static final int REQUEST_CODE_LAUNCH_ACTIVITY = 100;

  private ResultReceiver mResultReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      BillingHelper.logVerbose(TAG, "Launching Play Store billing flow");
      mResultReceiver = getIntent().getParcelableExtra(KEY_RESULT_RECEIVER);
      PendingIntent pendingIntent = null;
      if (getIntent().hasExtra(RESPONSE_BUY_INTENT_KEY)) {
        pendingIntent = getIntent().getParcelableExtra(RESPONSE_BUY_INTENT_KEY);
      } else if (getIntent().hasExtra(RESPONSE_SUBS_MANAGEMENT_INTENT_KEY)) {
        pendingIntent = getIntent().getParcelableExtra(RESPONSE_SUBS_MANAGEMENT_INTENT_KEY);
      }

      try {
        startIntentSenderForResult(
            pendingIntent.getIntentSender(), REQUEST_CODE_LAUNCH_ACTIVITY, new Intent(), 0, 0, 0);
      } catch (IntentSender.SendIntentException e) {
        BillingHelper.logWarn(TAG, "Got exception while trying to start a purchase flow: " + e);
        mResultReceiver.send(BillingResponseCode.ERROR, null);
        finish();
      }
    } else {
      BillingHelper.logVerbose(TAG, "Launching Play Store billing flow from savedInstanceState");
      mResultReceiver = savedInstanceState.getParcelable(KEY_RESULT_RECEIVER);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(KEY_RESULT_RECEIVER, mResultReceiver);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_LAUNCH_ACTIVITY) {
      int responseCode = BillingHelper.getResponseCodeFromIntent(data, TAG);
      if (resultCode != RESULT_OK || responseCode != BillingResponseCode.OK) {
        BillingHelper.logWarn(
            TAG,
            "Activity finished with resultCode "
                + resultCode
                + " and billing's responseCode: "
                + responseCode);
      }
      mResultReceiver.send(responseCode, data == null ? null : data.getExtras());
    } else {
      BillingHelper.logWarn(
          TAG, "Got onActivityResult with wrong requestCode: " + requestCode + "; skipping...");
    }
    // Need to finish this invisible activity once we sent back the result
    finish();
  }
}
