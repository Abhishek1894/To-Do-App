package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

class MyThread extends Thread
{
    SplashScreen context;
    Intent intent;
    MyThread(SplashScreen context)
    {
        this.context = context;
    }

    public void run()
    {
        try
        {
            Thread.sleep(3000);
        }
        catch(InterruptedException e)
        {
            Log.d("message",e.getMessage());
        }

        intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }
}

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new MyThread(this).start();
    }
}