package com.kyostudios.taskkeeper;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String mFileUrl = "/taskkeeper/default.csv";
    String file = Environment.getExternalStorageDirectory().getPath() + mFileUrl;
    File mFile = new File(Environment.getExternalStorageDirectory().getPath() + mFileUrl);
    String directory = Environment.getExternalStorageDirectory().getPath() + "/taskkeeper/";

    List taskList;
    List<TaskHolder> listTaskHolder;
    List<List> listCollection;
    List<TaskAdapter> listTaskCollections;
    List<TaskHolder> newListTaskHolder;
    TaskAdapter taskAdapter;

    RecyclerView mRecyclerViewList;

    TaskHolder tempTask;
    int currentPosition = 0;

    DrawerLayout mDrawerLayout;
    ListView mListView;
    List<String> categories;
    ActionBarDrawerToggle mDrawerToggle;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewTask cnt = new CreateNewTask(taskAdapter, listTaskHolder);
                cnt.show(getSupportFragmentManager(), "fragment_create");

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
            CSVHandler csvHandler = new CSVHandler(file, directory ,getApplicationContext(), getLayoutInflater());
            csvHandler.csvRead();
            int countFiles = csvHandler.countFiles(directory);
            if(countFiles == 0){
                try {
                    csvHandler.copyFileFromAssets(getApplicationContext(), "sample.csv", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                csvHandler.csvRead();
                categories = csvHandler.categories;//retrieve the categories list from the CSVHandler
                listCollection = csvHandler.collection;//retrieve collection list from the CSVHandler

                for(int i = 0; i<= listCollection.size() - 1; i++) {
                    taskList = listCollection.get(i);//Assign taskList to taskList at position i in listCollection
                    List<TaskAdapter> tempListTaskCollections = new ArrayList<>();
                    newListTaskHolder = new ArrayList<>();
                    for (int j = 0; j <= taskList.size() - 1; j++) {
                        String[] holder = (String[]) taskList.get(j);//Assign holder to String array item at position j in taskList
                        String task = holder[0].trim();
                        String color = holder[1].trim();
                        boolean done = Boolean.valueOf(holder[2].trim());

                        TaskHolder newTask = new TaskHolder(getApplicationContext(), getLayoutInflater(), task, color, done, getSupportFragmentManager());
                       //make new temporary ArrayList for taskHolders
                        newListTaskHolder.add(newTask);//add new TaskHolder to array

                    }
                    listTaskHolder = newListTaskHolder;
                    TaskAdapter newTaskAdapter = new TaskAdapter(newListTaskHolder);//create new TaskAdapter using newListTaskHolder
                    tempListTaskCollections.add(newTaskAdapter);//add newTaskAdapter to listTaskCollection
                    listTaskCollections = tempListTaskCollections;
                    /*
                        so far, if everything is done correctly, the structure of listTaskCollections
                         should look like this, given 3 files with 3 tasks in each file.

                         listTaskCollections
                            |_TaskAdapter
                            |    |_TaskHolder
                            |    |_TaskHolder
                            |    |_TaskHolder
                            |_TaskAdapter
                            |    |_TaskHolder
                            |    |_TaskHolder
                            |    |_TaskHolder
                            |_TaskAdapter
                                 |_TaskHolder
                                 |_TaskHolder
                                 |_TaskHolder

                            The categories list should also contain the same ammount of entries as there
                            are TaskAdapters in the listTaskCollections.

                            When a navigationDrawerItem is selected, the position selected will be used
                            to get the TaskAdapter from the appropriate position in the taskCollection.
                            This adapter will then be set to the RecyclerView, thus changing the DataSet.
                            RecyclerView will be notified of this, and the View will be refreshed.

                            When the app is started, the app will open the first TaskAdapter in the list

                     */

                }
                taskAdapter = listTaskCollections.get(0);
                mRecyclerViewList.setAdapter(taskAdapter);
                mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));

            }

        } else {
            Log.d("Abandon all hope, ye who enter", "ABANDON");
        }
//NavigationDrawer code below:
//--------------------------------------------------------------------------------------------------
        String[] navigationCategories;
        navigationCategories = categories.toArray(new String[0]);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navigationCategories));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                taskAdapter = listTaskCollections.get(position);
                mRecyclerViewList.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();
                currentPosition = position;
                drawerLayout.closeDrawer(drawerList);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    }

    @Override
    public void onStop() {
        super.onStop();


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
