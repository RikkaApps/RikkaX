package moe.shizuku.support.design;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialogHelper {

    public static void show(AlertDialog.Builder builder, Drawable icon, CharSequence title, CharSequence version, CharSequence info) {
        Dialog dialog = builder
                .setView(R.layout.dialog_about)
                .show();

        ((ImageView) dialog.findViewById(R.id.design_about_icon)).setImageDrawable(icon);
        ((TextView) dialog.findViewById(R.id.design_about_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.design_about_version)).setText(version);
        ((TextView) dialog.findViewById(R.id.design_about_info)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) dialog.findViewById(R.id.design_about_info)).setText(info);
    }
}
