package com.example.todo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.DBToDoModel;
import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.databinding.TaskcardBinding;
import com.example.todo.utils.DBHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<DBToDoModel> list;
    private MainActivity activity;
    private DBHelper myDB;


    public ToDoAdapter(DBHelper myDB, MainActivity activity) {
        this.activity= activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskcard,parent,false);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TaskcardBinding taskcardBinding = DataBindingUtil.inflate(inflater, R.layout.taskcard, parent, false);
        return new ViewHolder(taskcardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DBToDoModel item = list.get(position);
        holder.taskcardBinding.TaskName.setText(item.getTask());
        holder.taskcardBinding.TaskDescription.setText(item.getDescription());
        holder.taskcardBinding.startTime.setText(item.getStartTime());
        holder.taskcardBinding.StartDate.setText(item.getStartDate());
        holder.taskcardBinding.EndTime.setText(item.getEndTime());
        holder.taskcardBinding.EndDate.setText(item.getEndDate());
//        Toast.makeText(activity, ""+item.getStartTime()+" : " +item.getStartDate(), Toast.LENGTH_SHORT).show();

        if (list.get(position).getPriority().equalsIgnoreCase("1")) {
            holder.taskcardBinding.priorityColor.setBackgroundColor(Color.parseColor("#FFCF2626"));
        } else if (list.get(position).getPriority().equalsIgnoreCase("2")) {
            holder.taskcardBinding.priorityColor.setBackgroundColor(Color.parseColor("#FFFFEB3B"));
        } else {
            holder.taskcardBinding.priorityColor.setBackgroundColor(Color.parseColor("#24a19c"));
        }
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<DBToDoModel> list){
        this.list =list;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        DBToDoModel item = list.get(position);
        myDB.deleteTask(item.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        DBToDoModel model = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",model.getId());
        bundle.putString("task",model.getTask());
        bundle.putString("description",model.getDescription());
        bundle.putString("starttime",model.getStartTime());
        bundle.putString("startdate",model.getStartDate());
        bundle.putString("endtime",model.getEndTime());
        bundle.putString("enddate",model.getEndDate());
        bundle.putString("priority",model.getPriority());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TaskcardBinding taskcardBinding;

        public ViewHolder(@NonNull TaskcardBinding taskcardBinding) {
            super(taskcardBinding.getRoot());
            this.taskcardBinding = taskcardBinding;
        }
    }
}
