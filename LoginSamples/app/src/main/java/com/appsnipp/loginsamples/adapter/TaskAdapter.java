package com.appsnipp.loginsamples.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Message;
import com.appsnipp.loginsamples.model.ProjectModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private ArrayList<ProjectModel> ProjectModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameproject;
        TextView detailproject;
        TextView enddate;
        TextView card_name_project;

        MyViewHolder(View view) {
            super(view);
            nameproject = view.findViewById(R.id.tv_ten);
            detailproject = view.findViewById(R.id.tv_detail);
            card_name_project = view.findViewById(R.id.card_name_project);

        }
    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public TaskAdapter(ArrayList<ProjectModel> ProjectModelList) {
        this.ProjectModelList = ProjectModelList;
    }

    @Override
    public int getItemCount() {
        return ProjectModelList.size();
    }

    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_project, parent, false);

        return new TaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.MyViewHolder holder, final int position) {

        ProjectModel m = ProjectModelList.get(position);
        holder.detailproject.setText(m.getId());
        holder.nameproject.setText(m.getName());
        String fisrt_name= m.getName().substring(0,1);
        holder.card_name_project.setText(fisrt_name);
    }

}