package com.appsnipp.loginsamples.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.LOGIN.RegisterActivity;
import com.appsnipp.loginsamples.model.TaskModel;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    public ArrayList<TaskModel> taskModelList;
    public ProjectItemClicked itemClicked;
    private Context context;

    public TaskAdapter(Context context) {
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nametask;
        TextView detailtask;
        TextView nguoiphutrach;
        TextView trangthai,chitietcongviec;

        RelativeTimeTextView end_date;
        Boolean isItemClicked = false;
        MyViewHolder(final View view) {
            super(view);

            nametask = view.findViewById(R.id.tv_tentask);
            detailtask = view.findViewById(R.id.tv_detailtask);
            nguoiphutrach = view.findViewById(R.id.tv_nguoiphutrach);
            end_date = view.findViewById(R.id.tv_time_stamp_task);
            chitietcongviec=view.findViewById(R.id.tx_chitietcongviet);
            trangthai=view.findViewById(R.id.tv_task_state);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isItemClicked){
                        isItemClicked = true;
                        itemView.setBackgroundColor(itemView.getContext().getColor(R.color.whiteCardColor));
                    }else {
                        isItemClicked = false;
                        itemView.setBackgroundColor(itemView.getContext().getColor(R.color.whiteCardColor));
                    }
                   // itemClicked.onItemClicked(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                    view.getContext().startActivity(myIntent);
                    return true;
                }
            });
        }


    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public TaskAdapter(ArrayList<TaskModel> taskModelList) {
        this.taskModelList = taskModelList;
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.MyViewHolder holder, final int position) {

        TaskModel m = taskModelList.get(position);
        holder.detailtask.setText(m.getDetail());
        holder.nametask.setText(m.getName());
       holder.nguoiphutrach.setText(m.getUserCreate());
       holder.trangthai.setText((m.getStatus()));
        holder.chitietcongviec.setText(m.getDetail());
        try {
            holder.end_date.setReferenceTime(setendate(m.getDeadline()).getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public Calendar setendate(String enddate) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date1 = formater.parse(enddate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }


}
