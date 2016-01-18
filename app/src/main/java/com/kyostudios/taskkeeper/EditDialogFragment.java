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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Drew on 1/12/2016.
 */
public class EditDialogFragment extends DialogFragment {

    private EditText mEditText;
    private String taskText, colorChoice;
    private List<TaskHolder> taskHolder;
    private int position;
    private String color;

    public EditDialogFragment(String taskText, String colorChoice, List<TaskHolder> holder, int position){
        this.taskText = taskText;
        this.colorChoice = colorChoice;
        taskHolder = holder;
        this.position = position;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){

        //Declarations
        View view = inflater.inflate(R.layout.edit_dialog, viewGroup, false);
        mEditText = (EditText) view.findViewById(R.id.taskTextEdit);
        RadioButton radio1, radio2, radio3, radio4;
        radio1 = (RadioButton) view.findViewById(R.id.radioButton);
        radio2 = (RadioButton) view.findViewById(R.id.radioButton2);
        radio3 = (RadioButton) view.findViewById(R.id.radioButton3);
        radio4 = (RadioButton) view.findViewById(R.id.radioButton4);
        getDialog().setTitle("Edit Task...");
        Button cancelButton = (Button) view.findViewById(R.id.negative_button_edit);
        Button okButton = (Button) view.findViewById(R.id.positive_button_create);

        mEditText.setText(taskText);

        switch(colorChoice){
            case "Red":
                radio1.setChecked(true);
                color = "Red";
                break;
            case "Yellow":
                radio2.setChecked(true);
                color = "Yellow";
                break;
            case "Green":
                radio3.setChecked(true);
                color = "Green";
                break;
            case "none":
                radio4.setChecked(true);
                color = "none";
                break;
        }
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
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
                taskHolder.get(position).setTaskText(string);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);

                RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                String checkedText = (String) r.getText();
                switch(checkedText){
                    case "High":
                        color = "Red";
                        taskHolder.get(position).setColor(color);
                        break;
                    case "Medium":
                        color = "Yellow";
                        taskHolder.get(position).setColor(color);
                        break;
                    case "Low":
                        color = "Green";
                        taskHolder.get(position).setColor(color);
                        break;
                    case "None":
                        color = "none";
                        taskHolder.get(position).setColor(color);
                        break;
                }
                boolean done = taskHolder.get(position).getDone();
                taskHolder.get(position).setDone(done);
                MainActivity main = (MainActivity) getActivity();
                main.modifyAtTaskAtPosition(position);
                dismiss();

            }
        });
        return view;
    }
}
