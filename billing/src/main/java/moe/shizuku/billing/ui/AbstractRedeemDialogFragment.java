package moe.shizuku.billing.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import moe.shizuku.billing.R;


public abstract class AbstractRedeemDialogFragment extends DialogFragment implements
        DialogInterface.OnClickListener {

    private EditText mEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.billing_redeem)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this);

        View contentView = onCreateDialogView(context);
        if (contentView != null) {
            mEditText = contentView.findViewById(android.R.id.edit);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getDialog() != null) {
                        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
                                .setEnabled(onCheckCodeValidity(s.toString()));
                    }
                }
            });
            onBindDialogView(contentView, mEditText);
            builder.setView(contentView);
        }

        onPrepareDialogBuilder(builder);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                button.setEnabled(false);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AbstractRedeemDialogFragment.this.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
        });
        return dialog;
    }

    /**
     * Prepares the dialog builder to be shown.
     * Use this to set custom properties on the dialog.
     * <p>
     * Do not {@link AlertDialog.Builder#create()} or
     * {@link AlertDialog.Builder#show()}.
     */
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {}

    /**
     * Creates the content view for the dialog (if a custom content view is
     * required). By default, it inflates the dialog layout resource if it is
     * set.
     *
     * @return The content View for the dialog.
     */
    protected View onCreateDialogView(Context context) {
        final int resId = R.layout.billingclient_dialog_redeem;
        if (resId == 0) {
            return null;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(resId, null);
    }

    /**
     * Binds views in the content View of the dialog to data.
     * <p>
     * Make sure to call through to the superclass implementation.
     *
     * @param view The content View of the dialog.
     * @param edit The EditText.
     */
    protected abstract void onBindDialogView(View view, EditText edit);

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            onSubmit(mEditText.getText().toString(), (AlertDialog) getDialog());
        }
    }

    /**
     * Called when 'OK' is clicked, implement your redeem logic here.
     *
     * @param code Code in EditText
     * @param dialog Dialog
     */
    public abstract void onSubmit(String code, AlertDialog dialog);

    /**
     * Check if the code is valid. If valid, the 'OK' button of the dialog will be enabled.
     * <p>
     * Make sure you only implemented local logic here.
     *
     * @param code Code in EditText
     * @return is the code valid
     */
    public abstract boolean onCheckCodeValidity(String code);

    /**
     * Start Google play redeem activity.
     *
     * @param code Redeem code
     * @param showFailedToast show 'Google play not available' if failed
     * @return activity is successfully started
     */
    public boolean startRedeemGooglePlay(String code, boolean showFailedToast) {
        if (getContext() == null) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/redeem?code=" + code));
        if (intent.resolveActivity(getContext().getPackageManager()) == null) {
            if (showFailedToast) {
                Toast.makeText(getContext(), R.string.billing_google_play_unavailable, Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        try {
            getContext().startActivity(intent);
            return true;
        } catch (Throwable tr) {
            Toast.makeText(getContext(), R.string.billing_google_play_unavailable, Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
