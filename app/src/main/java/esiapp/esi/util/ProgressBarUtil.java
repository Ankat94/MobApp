package esiapp.esi.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;
import android.view.WindowManager.BadTokenException;

import esiapp.esi.R;


/**
 * Created by Randhir Kumar on 1/23/2017.
 */

public class ProgressBarUtil {
    private static ProgressDialog progressDialog;
    private static TextView mTextViewMessage;

    /**
     * To show the progress dialog with the title as application name and message as "Loading" text.
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            try {
                progressDialog.show();
            } catch (BadTokenException e) {

            }
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.progress_dialog_layout);
//            mTextViewMessage = (TextView) progressDialog.findViewById(R.id.progress_message_text_view);
//            mTextViewMessage.setText(message);
        }
    }

    /**
     * To dismiss the progress dialog
     */
    public static void dismissProgressDialog() {
        // try catch block for handling java.lang.IllegalArgumentException
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
        progressDialog = null;
    }

}
