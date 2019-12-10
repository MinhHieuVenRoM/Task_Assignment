package com.appsnipp.loginsamples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskbyUserAdapter extends RecyclerView.Adapter<TaskbyUserAdapter.MyViewHolder> {
    public ArrayList<TaskModel> taskModelList;
    public TaskItemClicked itemClicked;
    private Context context;
    private  String Username="";

    public TaskbyUserAdapter(Context context) {
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nametask;
        TextView detailtask;
        TextView nguoiphutrach;
        TextView trangthai;

        RelativeTimeTextView end_date;
        MyViewHolder(final View view) {
            super(view);

            nametask = view.findViewById(R.id.tv_tentask);
            detailtask = view.findViewById(R.id.tv_detailtask);
            nguoiphutrach = view.findViewById(R.id.tv_nguoiphutrach);
            end_date = view.findViewById(R.id.tv_time_stamp_task);
            trangthai=view.findViewById(R.id.tv_task_state);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent myIntent = new Intent(view.getContext(), DetailTaskActivity.class);
//                    view.getContext().startActivity(myIntent);
                    // itemClicked.onItemClickedTask(getAdapterPosition(),m);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
        }
        void initData(final TaskModel m) {

            detailtask.setText(m.getContent());
            nametask.setText(m.getName());
            nguoiphutrach.setText(Username);
            String status="";
            if(m.getStatus()==0){
                status="OPEN";
            }else  if(m.getStatus()==1)
            {
                status="FIXING";
            }else if(m.getStatus()==2){
                status="FIXED";
            }else{
                status="DONE";

            }
            trangthai.setText(status);
            String deadline= m.getEndDate().substring(0, 10);
            deadline=deadline.replace("-","/");

            try {
                end_date.setReferenceTime(setendate(deadline).getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onItemClickedTask(getAdapterPosition(),m);
                }
            });

        }


    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public TaskbyUserAdapter(ArrayList<TaskModel> taskModelList,String username) {
        this.taskModelList = taskModelList;
        this.Username=username;
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    @NonNull
    @Override
    public TaskbyUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskbyUserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskbyUserAdapter.MyViewHolder holder, final int position) {

        TaskModel taskModel = taskModelList.get(position);
        holder.initData(taskModel);
    }


    public Calendar setendate(String enddate) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date date1 = formater.parse(enddate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }


}
