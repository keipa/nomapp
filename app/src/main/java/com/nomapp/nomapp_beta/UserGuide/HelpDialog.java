package com.nomapp.nomapp_beta.UserGuide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

/**
 * Created by Илья on 18.03.2016.
 */
public class HelpDialog extends DialogFragment implements OnClickListener {


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("HELP").setPositiveButton("OK", this)
                .setMessage("It is Help");
        return adb.create();
    }
}
