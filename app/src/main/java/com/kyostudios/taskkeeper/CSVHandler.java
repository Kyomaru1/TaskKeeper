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

/**
 * Created by Drew on 1/15/2016.
 */
public class CSVHandler {
    String file;
    String URL;
    Context context;
    LayoutInflater inflater;

    public CSVHandler(String file,String URL, Context applicationContext, LayoutInflater layoutInflater) {
        this.file = file;
        this.URL = URL;
        context = applicationContext;
        inflater = layoutInflater;
    }

    public List csvRead() {
        File f = new File(String.valueOf(file));

        if (f.exists()) {

        }
        else {
            try {
                copyFileFromAssets(context, "sample.csv", file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        List taskList = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            CSVFile csvFile = new CSVFile(inputStream);
            taskList = csvFile.read();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return taskList;
    }

    static public void copyFileFromAssets(Context context, String file, String dest) throws Exception
    {
        InputStream in = null;
        OutputStream fout = null;
        int count = 0;

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

    public int countFiles(File f){

        File[] list = f.listFiles();
        int count = 0;
        for (File file :list) {
            String name = file.getName();
            if (name.endsWith(".csv")){
                count = count + 1;
            }

        }
        return count;
    }
}
