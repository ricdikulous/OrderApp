package com.dikulous.ric.orderapp.order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.dikulous.ric.orderapp.R;

/**
 * Created by ric on 30/04/16.
 */
public class OrderSubmitFailedDialog extends DialogFragment {
    private static final String TAG = "SubmitFailed Dialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setTitle(R.string.dialog_order_submit_failed_title);
        builder.setMessage(R.string.dialog_order_submit_failed_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "clicked!");
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "cancelled");
            }
        });
        return builder.create();
    }
}
