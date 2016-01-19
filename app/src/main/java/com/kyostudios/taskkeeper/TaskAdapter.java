package com.kyostudios.taskkeeper;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Drew on 1/12/2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private FragmentManager fm;
    public  List<TaskHolder> mTaskHolder;
    private Context context;

    public TaskAdapter(List<TaskHolder> list){
        mTaskHolder = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskTextView;
        public CheckBox taskCheckBox;
        public Button taskMenuButton;
        public LinearLayout taskMainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            taskTextView = (TextView) itemView.findViewById(R.id.taskText);
            taskCheckBox = (CheckBox) itemView.findViewById(R.id.doneCheckBox);
            taskMainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            taskMenuButton = (Button) itemView.findViewById(R.id.menuButton);

        }

    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.task_holder, parent, false);
        return new ViewHolder(taskView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TaskHolder taskHolder = mTaskHolder.get(position);

        final TextView textView = holder.taskTextView;
        textView.setText(taskHolder.getTaskText());

        final LinearLayout mainLayout = holder.taskMainLayout;
        fm = taskHolder.getFragmentManager();
        final CheckBox checkBox = holder.taskCheckBox;

        final Button menuButton = holder.taskMenuButton;

        final String color = taskHolder.getColorForTask();
        final boolean[] done = {taskHolder.getDone()};
        checkBox.setChecked(done[0]);

        if(done[0]){
            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGray));
        }
        else{
            switch(color){
                case "Red":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialRed));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mainLayout.setElevation(8);
                    }
                    break;
                case "Yellow":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialYellow));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mainLayout.setElevation(8);
                    }
                    break;
                case "Green":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGreen));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mainLayout.setElevation(8);
                    }
                    break;
                case "none":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialPlain));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mainLayout.setElevation(8);
                    }
                    break;
            }
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!checkBox.isChecked()) {
                    switch (color) {
                        case "Red":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialRed));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mainLayout.setElevation(8);
                            }
                            break;
                        case "Yellow":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialYellow));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mainLayout.setElevation(8);
                            }
                            break;
                        case "Green":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGreen));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mainLayout.setElevation(8);
                            }
                            break;
                        case "none":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialPlain));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mainLayout.setElevation(8);
                            }
                            break;
                    }
                } else {
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mainLayout.setElevation(0);
                    }
                }
                if(!mTaskHolder.get(position).getDone()){
                    mTaskHolder.get(position).setDone(true);
                }
                else if(mTaskHolder.get(position).getDone()){
                    mTaskHolder.get(position).setDone(false);
                }
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialogFragment edf = new EditDialogFragment(mTaskHolder.get(position).getTaskText(), mTaskHolder.get(position).getColorForTask(), mTaskHolder, position);
                edf.show(fm, "fragment_edit");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskHolder.size();
    }

}
