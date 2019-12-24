package com.appsnipp.loginsamples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Task_model.DataTasknotify;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class TaskNotifiAdapter extends RecyclerView.Adapter<TaskNotifiAdapter.MyViewHolder> {
    public ArrayList<DataTasknotify> taskModelList;
    public TaskItemnotifiClicked itemClicked;
    private Context context;

    public TaskNotifiAdapter(Context context) {
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

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
        }
        void initData(final DataTasknotify m) {

            detailtask.setText(m.getContent());
            nametask.setText(m.getName());
            User userModel = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
            nguoiphutrach.setText(userModel.getName());
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
                    itemClicked.onItemClickedTasknotify(getAdapterPosition(),m);
                }
            });

        }


    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public TaskNotifiAdapter(ArrayList<DataTasknotify> taskModelList) {
        this.taskModelList = taskModelList;
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    @NonNull
    @Override
    public TaskNotifiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskNotifiAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskNotifiAdapter.MyViewHolder holder, final int position) {

        DataTasknotify taskModel = taskModelList.get(position);
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
