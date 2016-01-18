package com.kyostudios.taskkeeper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String csvDeliminator = ",,";

    String mFileUrl = "/taskkeeper/default.csv";
    String file = Environment.getExternalStorageDirectory().getPath() + mFileUrl;
    File mDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/taskkeeper/");
    List taskList;
    List<TaskHolder> listTaskHolder;
    TaskAdapter taskAdapter;
    RecyclerView mRecyclerViewList;
    TaskHolder tempTask;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewTask cnt = new CreateNewTask(taskAdapter, taskList);
                cnt.show(getSupportFragmentManager(), "fragment_create");
                Toast.makeText(MainActivity.this, "Executed after dialog close", Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerViewList = (RecyclerView) findViewById(R.id.ListColection);
        listTaskHolder = new ArrayList<>();

        File folder = new File(Environment.getExternalStorageDirectory() + "/taskkeeper");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            CSVHandler csvHandler = new CSVHandler(file, mFileUrl,getApplicationContext(), getLayoutInflater());
            taskList = csvHandler.csvRead();

            for (int i = 0; i <= taskList.size() - 1; i++) {
                String[] holder = (String[]) taskList.get(i);
                String task = holder[0].trim();
                String color = holder[1].trim();
                boolean done = Boolean.valueOf(holder[2].trim());

                TaskHolder newTask = new TaskHolder(getApplicationContext(), getLayoutInflater(), task, color, done, getSupportFragmentManager());

                listTaskHolder.add(newTask);

            }
            taskAdapter = new TaskAdapter(listTaskHolder);
            mRecyclerViewList.setAdapter(taskAdapter);
            mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.d("Abandon all hope, ye who enter", "ABANDON");
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_done) {
            deleteChecked(listTaskHolder);
            return true;
        }
        if(id == R.id.action_about){
            AboutDialog abd = new AboutDialog();
            abd.show(getSupportFragmentManager(),"fragment_about");
            return true;
        }
        if(id == R.id.action_edit_category){
            return true;
        }
        if(id == R.id.action_save){
            try {
                save(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        if(id == R.id.action_help){
            HelpDialog helpDialog = new HelpDialog();
            helpDialog.show(getSupportFragmentManager(), "fragment_help");
        }


        return super.onOptionsItemSelected(item);
    }

    public void showCreateDialog() {
        CreateChoiceDialogFragment cdf = new CreateChoiceDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        cdf.show(fm, "fragment_create");
    }

    public void showAboutDialog() {

    }

    public void showDirections() {

    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            save(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kyostudios.taskkeeper/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kyostudios.taskkeeper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

        try{
            save(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteChecked(List<TaskHolder> taskList){
        int size = listTaskHolder.size();
        for(int i = size - 1; i >= 0; i--){
            boolean done = taskList.get(i).getDone();
            if(done){
                taskList.remove(i);
                taskAdapter.notifyItemRemoved(i);
                taskAdapter.notifyItemRangeRemoved(i, taskAdapter.getItemCount());
            }

        }
    }

    public void receiveTempTaskHolder(TaskHolder task){
        tempTask = task;
        listTaskHolder.add(task);
        int itemAddPosition = listTaskHolder.size() - 1;
        taskAdapter.notifyItemInserted(itemAddPosition);
    }

    public void modifyAtTaskAtPosition(int position){
        taskAdapter.notifyItemChanged(position);
    }

    public void save(String fileName) throws FileNotFoundException {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            for(TaskHolder task : listTaskHolder){
                String out = task.getTaskText() + ",," + task.getColorForTask() + ",," + Boolean.toString(task.getDone()) +"\n";
                pw.print(out);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
