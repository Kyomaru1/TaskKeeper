package com.kyostudios.taskkeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Drew on 1/12/2016.
 */
public class EditDialogFragment extends DialogFragment {

    private EditText mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.edit_dialog, viewGroup, false);
        mEditText = (EditText) view.findViewById(R.id.taskTextEdit);
        getDialog().setTitle("Edit Task...");
        Button cancelButton = (Button) view.findViewById(R.id.negative_button_create);
        Button okButton = (Button) view.findViewById(R.id.positive_button_create);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = mEditText.getText().toString().trim();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_LONG);
                toast.show();
                dismiss();
            }
        });
        return view;
    }
}
