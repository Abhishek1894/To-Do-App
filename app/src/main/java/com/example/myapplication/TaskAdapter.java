package com.example.myapplication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    Context context;
    ArrayList<Task> taskList;

    int hour,minutes;
    String amPm;
    String time;

    public TaskAdapter(Context context, ArrayList<Task> taskList)
    {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.task_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position)
    {
        holder.task.setText(taskList.get(position).task);
        holder.time.setText(taskList.get(position).time);
        holder.timeIcon.setImageResource(R.drawable.ic_baseline_access_time_24);
        holder.editIcon.setImageResource(R.drawable.ic_baseline_edit_24);
        holder.deleteIcon.setImageResource(R.drawable.ic_baseline_delete_24);

        holder.task.setChecked(taskList.get(position).isCompleted); // if task is completed it checks the checkBox

        // Sets the value of isCompleted, if checkBox is checked i.e task is completed then isCompleted should be true;
        holder.task.setOnClickListener(v ->
        {
            taskList.get(holder.getAdapterPosition()).isCompleted = holder.task.isChecked();
            FileHelper.writeTasks(context,taskList);
        });

        // deleting a task from recycler view
        holder.deleteIcon.setOnClickListener(v ->
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Delete")
                    .setIcon(R.drawable.ic_baseline_delete_24)
                    .setMessage("Do you want to delete a task")
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            taskList.remove((int)holder.getAdapterPosition());
                            notifyItemRemoved((int)holder.getAdapterPosition());
                            FileHelper.writeTasks(context,taskList);
                        }
                    })
                    .show();
        });

        // updating a task from recycler view
        holder.editIcon.setOnClickListener(v ->
        {
            Dialog updateDialog = new Dialog(context);
            updateDialog.setContentView(R.layout.add_task_dialog_layout);
            TextView header = updateDialog.findViewById(R.id.heading);
            EditText editTask = updateDialog.findViewById(R.id.multiLineTextAddTask);
            Button setTimeButton = updateDialog.findViewById(R.id.buttonSetTime);
            Button saveButton = updateDialog.findViewById(R.id.buttonSaveTask);

            header.setText(R.string.update);
            editTask.setText(taskList.get(holder.getAdapterPosition()).task);
            updateDialog.show();

            setTimeButton.setOnClickListener(view ->
            {
                TimePickerDialog timePickerDialog  = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
                    {
                        if(hourOfDay == 0)
                        {
                            hourOfDay += 12;
                            amPm = "AM";
                        }
                        else if(hourOfDay == 12)
                        {
                            amPm = "PM";
                        }
                        else if(hourOfDay > 12)
                        {
                            hourOfDay -= 12;
                            amPm = "PM";
                        }
                        else
                        {
                            amPm = "AM";
                        }

                        hour = hourOfDay;
                        minutes = minute;
                        time = new String(String.format(Locale.getDefault(),"%02d:%02d " + amPm ,hour,minutes));
                        setTimeButton.setText(time);
                    }
                },hour,minutes,false);

                timePickerDialog.show();
            });

            // clickListener for saving the task
            saveButton.setOnClickListener(vi ->
            {
                String newTask = editTask.getText().toString();

                if(newTask.equals(""))
                {
                    Toast.makeText(context, "Task not added!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Task t = new Task(newTask,time);
                    taskList.set(holder.getAdapterPosition(),t);
                    notifyItemChanged(holder.getAdapterPosition());
                    updateDialog.cancel();

                    // saving newly added task into file;
                    FileHelper.writeTasks(context,taskList);

                    time = ""; // clearing the time
                }

            });
        });
    }

    @Override
    public int getItemCount()
    {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox task;
        TextView time;
        ImageView timeIcon,deleteIcon,editIcon;

        public ViewHolder(View v)
        {
            super(v);
            task = v.findViewById(R.id.taskCheckBox);
            time = v.findViewById(R.id.time);
            timeIcon = v.findViewById(R.id.timeIcon);
            deleteIcon = v.findViewById(R.id.deleteTaskIcon);
            editIcon = v.findViewById(R.id.editTaskIcon);
        }
    }
}
