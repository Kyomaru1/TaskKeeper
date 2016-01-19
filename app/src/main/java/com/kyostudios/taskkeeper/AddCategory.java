package com.kyostudios.taskkeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by Drew on 1/18/2016.
 */
public class AddCategory extends DialogFragment {

    private ArrayAdapter transferAdapter;
    private ListView transferedLayout;
    public AddCategory(ArrayAdapter arrayAdapter, ListView layout){
        transferAdapter = arrayAdapter;
        transferedLayout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_category_dialog, viewGroup, false);
        getDialog().setTitle("Add Category");
        final EditText editText = (EditText) view.findViewById(R.id.taskTextAdd);
        final Button okButton, cancelButton;
        okButton = (Button) view.findViewById(R.id.positive_button_add);
        cancelButton = (Button) view.findViewById(R.id.negative_button_add);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().length() > 0){
                    MainActivity main = new MainActivity();
                    String fileName = editText.getText().toString().trim();
                    String directory = main.directory + "/" + fileName + main.fileExtension;

                    CSVHandler csv = new CSVHandler(directory, main.directory, getActivity().getApplicationContext(), getActivity().getLayoutInflater());
                    try {
                        csv.makeNewFile(directory, fileName, transferAdapter, transferedLayout);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    dismiss();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Nothing entered, no category created", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}

