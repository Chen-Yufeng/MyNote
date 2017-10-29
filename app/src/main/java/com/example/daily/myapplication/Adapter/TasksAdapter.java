package com.example.daily.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daily.myapplication.EditPageActivity;
import com.example.daily.myapplication.R;
import com.example.daily.myapplication.EntityClass.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> Tasks;
    private Context context;

    public TasksAdapter(List<Task> Tasks, Context context) {
        this.Tasks = Tasks;
        this.context = context;
    }

    //static Why?
    static class ViewHolder extends RecyclerView.ViewHolder {
        View tasksView;
        TextView title, setTime, deadLineTime, content, priority, doneFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            tasksView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            setTime = (TextView) itemView.findViewById(R.id.setTime);
            deadLineTime = (TextView) itemView.findViewById(R.id.deadLineTime);
            content = (TextView) itemView.findViewById(R.id.content);
            priority = (TextView) itemView.findViewById(R.id.priority);
            doneFlag = (TextView) itemView.findViewById(R.id.doneFlag);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent,
                false);
        final ViewHolder holder = new ViewHolder(view);
        holder.tasksView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Task thisTask = Tasks.get(position);
                Intent intent = new Intent(context, EditPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable("thisTask", thisTask);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task aTask = Tasks.get(position);

        holder.title.setText(aTask.getTitle());
        holder.setTime.setText(aTask.getSetTime());
        holder.deadLineTime.setText(aTask.getdeadLineTime());
        holder.priority.setText(Integer.toString(aTask.getPriority()));
        holder.content.setText(aTask.getContent());
        if (aTask.getDoneFlag() == 1)
            holder.doneFlag.setText("☑");
        else
            holder.doneFlag.setText("☐");

    }

    @Override
    public int getItemCount() {
        return Tasks.size();
    }
}
