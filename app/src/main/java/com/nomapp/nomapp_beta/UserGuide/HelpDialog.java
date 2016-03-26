package com.nomapp.nomapp_beta.UserGuide;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nomapp.nomapp_beta.R;

/**
 * Created by Илья on 18.03.2016.
 */
public class HelpDialog extends DialogFragment implements DialogInterface.OnClickListener {


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.main_activity_msg_title)).setPositiveButton("OK", this)
                .setMessage(getActivity().getResources().getString(R.string.main_activity_help_msg));
        return adb.create();
    }
}
