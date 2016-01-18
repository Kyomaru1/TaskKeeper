package com.kyostudios.taskkeeper;

import android.content.Context;
import android.view.LayoutInflater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class CSVHandler {
    String file;
    String URL;
    Context context;
    LayoutInflater inflater;
    List<String> categories;
    List taskList;
    List<List> collection;

    public CSVHandler(String file,String URL, Context applicationContext, LayoutInflater layoutInflater) {
        this.file = file;
        this.URL = URL;
        context = applicationContext;
        inflater = layoutInflater;
    }

    public void csvRead() {
        //takes file from constructor, and stores it into File f.
        File f = new File(String.valueOf(URL));
        //counts the number of files in the directory that houses file f
        int countFiles = countFiles(URL);
        if (countFiles > 0) {
            //generates a list of files in the directory
            File[] list = f.listFiles();
            List<String> tempcategories = new ArrayList<>();
            for(File file : list){//for each file in the list, get their name,
                String name = file.getName();//make a substring without the extension,
                String nameSansExtension = name.substring(0, name.lastIndexOf("."));

                tempcategories.add(nameSansExtension);//add to categories

                //categories will be picked up by the main activity to be the list that holds the
                //values for the navigationDrawer
            }
            categories = tempcategories;
        }
        else { //no files in directory
            try {
                copyFileFromAssets(context, "sample.csv", file);//copy the sample file 'sample.csv' to the file location
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            List<List> tempCollection = new ArrayList<>();
            File[] list = f.listFiles();
            for(File file : list) {//for each file in the list
                InputStream inputStream = new FileInputStream(file);
                CSVFile csvFile = new CSVFile(inputStream);//make a new csvFile object
                taskList = csvFile.read();//read the data from the file into a list
                tempCollection.add(taskList);//and store that list into the collection
            }
            collection = tempCollection;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void copyFileFromAssets(Context context, String file, String dest) throws Exception
    {
        InputStream in = null;
        OutputStream fout = null;
        int count;

        try
        {
            in = context.getAssets().open(file);
            fout = new FileOutputStream(new File(dest));

            byte data[] = new byte[1024];
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try {
                    in.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fout != null)
            {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int countFiles(String file){
        File f = new File(String.valueOf(file));
        int count = 0;
        for (File fileEntry :f.listFiles()) {
            String name = fileEntry.getName();
            if (name.endsWith(".csv")){
                count = count + 1;
            }

        }
        return count;
    }

    public File[] returnFilesInDirectory(File f){
        File[] list = f.listFiles();
        return list;
    }
}
