package com.kyostudios.taskkeeper;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Drew on 1/12/2016.
 */
public class TaskHolder extends LinearLayout implements View.OnClickListener{
    public TextView taskText;
    public CheckBox checkBox;
    public LinearLayout mainLayout;
    public String textForTask;
    public String colorForTask;
    final public FragmentManager fm;
    public boolean done;

    public TaskHolder(Context context, LayoutInflater inflater, String textForTask, String colorForTask, boolean done, final FragmentManager fm) {
        super(context);
        inflater.inflate(R.layout.task_holder, this);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        taskText = (TextView) findViewById(R.id.taskText);
        checkBox= (CheckBox) findViewById(R.id.doneCheckBox);
        taskText.setText(textForTask);
        checkBox.setChecked(done);
        this.fm = fm;
        this.textForTask = textForTask;
        this.colorForTask = colorForTask;
        this.done = done;

        Button menuButton = (Button) findViewById(R.id.menuButton);

    }

    public String getTaskText(){
        return textForTask;
    }

    public String getColorForTask(){
        return colorForTask;
    }

    public boolean getDone(){
        return done;
    }
    public void setDone(Boolean done){
        this.done = done;
    }

    public FragmentManager getFragmentManager(){
        return fm;
    }


    @Override
    public void onClick(View view) {
        CreateChoiceDialogFragment cdf = new CreateChoiceDialogFragment();
        cdf.show(fm, "fragment_create");
    }
}
