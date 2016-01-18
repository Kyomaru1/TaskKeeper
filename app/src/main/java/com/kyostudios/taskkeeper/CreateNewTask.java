package com.kyostudios.taskkeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Created by Drew on 1/17/2016.
 */
public class CreateNewTask extends DialogFragment {
    private TaskAdapter taskAdapter;
    private Button cancelButton, okButton;
    private EditText editText;
    private RadioGroup radioGroup;
    private List<TaskHolder> taskList;
    private TaskHolder temp;

    public CreateNewTask(TaskAdapter taskAdapter, List<TaskHolder> list){
        this.taskAdapter = taskAdapter;
        taskList = list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, final Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_task_dialog, viewGroup, false);
        getDialog().setTitle("New Task...");
        cancelButton = (Button) view.findViewById(R.id.negative_button_add);
        okButton = (Button) view.findViewById(R.id.positive_button_add);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        editText = (EditText) view.findViewById(R.id.taskTextAdd);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String outText = editText.getText().toString().trim();
                String colorText;

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);

                RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                colorText = (String) r.getText();
                switch (colorText) {
                    case "High":
                        colorText = "Red";
                        break;
                    case "Medium":
                        colorText = "Yellow";
                        break;
                    case "Low":
                        colorText = "Green";
                        break;
                    case "None":
                        colorText = "none";
                        break;

                }

                MainActivity main = (MainActivity) getActivity();
                temp = new TaskHolder(getActivity().getApplicationContext(), getActivity().getLayoutInflater(), outText, colorText, false, getActivity().getSupportFragmentManager());
                main.receiveTempTaskHolder(temp);
                dismiss();
            }
        });
        return view;
    }


}
