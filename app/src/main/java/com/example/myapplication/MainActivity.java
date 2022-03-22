package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.backup.FileBackupHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> taskList = new ArrayList<>();
    RecyclerView recyclerView;
    Button addTaskButton;
    TaskAdapter adapter;

    int hour;
    int minutes;
    String amPm;
    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleView);
        addTaskButton = findViewById(R.id.addTaskButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // very important

//        setSampleValues();
        taskList = FileHelper.readFile(MainActivity.this);

        adapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(adapter);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTimeSet = false;
                Dialog addTaskDialog = new Dialog(MainActivity.this);
                addTaskDialog.setContentView(R.layout.add_task_dialog_layout);
                EditText task = addTaskDialog.findViewById(R.id.multiLineTextAddTask);
                Button setTime = addTaskDialog.findViewById(R.id.buttonSetTime);
                Button saveTask = addTaskDialog.findViewById(R.id.buttonSaveTask);

                // clickListener for setting time
                setTime.setOnClickListener(v ->
                {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                amPm = "AM";
                            } else if (hourOfDay == 12) {
                                amPm = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                amPm = "PM";
                            } else {
                                amPm = "AM";
                            }

                            hour = hourOfDay;
                            minutes = minute;
                            time = new String(String.format(Locale.getDefault(), "%02d:%02d " + amPm, hour, minutes));
                            setTime.setText(time);
                        }
                    }, hour, minutes, false);

                    timePickerDialog.show();
                });

                // clickListener for saving the task
                saveTask.setOnClickListener(v ->
                {
                    String newTask = task.getText().toString();

                    if (newTask.equals("")) {
                        Toast.makeText(MainActivity.this, "Task not added!", Toast.LENGTH_SHORT).show();
                    } else {
                        Task t = new Task(newTask, time);
                        taskList.add(t);
                        adapter.notifyItemInserted(taskList.size() - 1);
                        addTaskDialog.cancel();
                        recyclerView.scrollToPosition(taskList.size() - 1); // automatically scroll to last position

                        // saving newly added task into file;
                        FileHelper.writeTasks(MainActivity.this, taskList);

                        time = ""; // clearing the time
                    }

                });

                addTaskDialog.show();


            }
        });

    }

}