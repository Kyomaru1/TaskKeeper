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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
                showCreateDialog();
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
            return true;
        }
        if(id == R.id.action_edit_category){

        }
        if(id == R.id.action_save){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);

                for(int i = 0; i <= taskList.size() - 1; i++){
                    String[] holder = (String[]) taskList.get(i);
                    String taskText = holder[0].trim();
                    String colorText = holder[1].trim();
                    String done = holder[2].trim();
                    String outputLine = taskText + csvDeliminator + colorText + csvDeliminator + done + "\n";
                    byte[] buffer = outputLine.getBytes();
                    fileOutputStream.write(buffer);
                    fileOutputStream.flush();
                }

                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    public void showEditDialog() {
        EditDialogFragment edf = new EditDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        edf.show(fm, "fragment_edit");
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

    public void writeToFile(List data) {
        try {
            //This line isn't working. Figure it out. Copying Error output to file.
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(file, Context.MODE_PRIVATE));
            for (int i = 0; i <= data.size() - 1; i++) {
                outputStreamWriter.write(data.get(i).toString() + "\r\n");
            }

            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
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
    }

    public void deleteChecked(List<TaskHolder> taskList){
        for(int i = 0; i <= taskAdapter.getItemCount() - 1; i++){
            boolean done = taskList.get(i).getDone();
            if(done){
                taskList.remove(i);
                taskAdapter.notifyItemRemoved(i);
            }
            else{

            }
        }
    }
}
