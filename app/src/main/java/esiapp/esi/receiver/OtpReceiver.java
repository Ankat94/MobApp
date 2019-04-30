package esiapp.esi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Randhir Kumar on 2/13/2017.
 */

public class OtpReceiver extends BroadcastReceiver {
    private static final String TAG = OtpReceiver.class.getName();
    final SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();//.split(":")[1];
                    message = message.substring(0, message.length() - 1);
                    Log.d(TAG, "Sender number: " + senderNum + "; message: " + message);

                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Sms Exception " + e);
        }
    }
}
