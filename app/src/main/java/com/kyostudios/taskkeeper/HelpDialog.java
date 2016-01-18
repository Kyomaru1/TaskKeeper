package com.kyostudios.taskkeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Drew on 1/17/2016.
 */
public class HelpDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.help_dialog, viewGroup, false);

    }
}
