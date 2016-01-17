package com.kyostudios.taskkeeper;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Drew on 1/12/2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private FragmentManager fm;
    private List<TaskHolder> mTaskHolder;
    private Context context;

    public TaskAdapter(List<TaskHolder> list){
        mTaskHolder = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskTextView;
        public CheckBox taskCheckBox;
        public Button taskMenuButton;
        public LinearLayout taskMainLayout;

        public MainActivity mainActivity;

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
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, final int position) {
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
                    mainLayout.setElevation(8);
                    break;
                case "Yellow":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialYellow));
                    mainLayout.setElevation(8);
                    break;
                case "Green":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGreen));
                    mainLayout.setElevation(8);
                    break;
                case "none":
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialPlain));
                    mainLayout.setElevation(8);
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
                            mainLayout.setElevation(8);
                            break;
                        case "Yellow":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialYellow));
                            mainLayout.setElevation(8);
                            break;
                        case "Green":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGreen));
                            mainLayout.setElevation(8);
                            break;
                        case "none":
                            mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialPlain));
                            mainLayout.setElevation(8);
                            break;
                    }
                } else {
                    mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.materialGray));
                    mainLayout.setElevation(0);
                }
                if(mTaskHolder.get(position).getDone() == false){
                    mTaskHolder.get(position).setDone(true);
                    Toast.makeText(context, "Item at Position "+ position + ", done = " + mTaskHolder.get(position).getDone(), Toast.LENGTH_SHORT).show();
                }
                else{
                    mTaskHolder.get(position).setDone(false);
                    Toast.makeText(context, "Item at Position "+ position + ", done = " + Boolean.toString(done[0]), Toast.LENGTH_SHORT).show();
                }
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialogFragment edf = new EditDialogFragment();
                edf.show(fm, "fragment_edit");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskHolder.size();
    }
}
