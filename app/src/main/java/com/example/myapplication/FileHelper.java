package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper
{
    public static final String fileName = "taskFile.dat";

    public static void writeTasks(Context context, ArrayList<Task> taskList)
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(taskList);
            oos.close();
            Log.d("message","Data Saved");
        }
        catch(FileNotFoundException e)
        {
            Log.d("message",e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("message",e.getMessage());
        }
    }

    public static ArrayList<Task> readFile(Context context)
    {
        ArrayList<Task> list = new ArrayList<>();
        try
        {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            list = (ArrayList<Task>) ois.readObject();
            ois.close();
        }
        catch(FileNotFoundException e)
        {
            Log.d("message",e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("message",e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.d("message",e.getMessage());
        }

        return  list;
    }
}
