package com.kyostudios.taskkeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Drew on 1/14/2016.
 */
public class CreateChoiceDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.create_choice_dialog, viewGroup, false);

        Button cancel;
        cancel = (Button) view.findViewById(R.id.negative_button_edit);
        getDialog().setTitle("Create...");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
