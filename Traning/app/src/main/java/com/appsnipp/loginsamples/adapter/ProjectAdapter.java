package com.appsnipp.loginsamples.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.appsnipp.loginsamples.R.color.BlackColor;
import static com.appsnipp.loginsamples.R.color.colorStatus;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    public ArrayList<ProjectModel> projectModelList;
    public ArrayList<UserModelDetail> userModelDetailList;
    public ProjectItemClicked itemClicked;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameproject;
        TextView card_name_project;
        TextView userCreate, tv_project_state;
        RelativeTimeTextView end_date;
        Boolean isItemClicked = false;
        CardView card_avatar;

        MyViewHolder(View view) {
            super(view);
            card_avatar=view.findViewById(R.id.card_avatar);
            nameproject = view.findViewById(R.id.tv_ten);
            card_name_project = view.findViewById(R.id.card_name_project);
            end_date = view.findViewById(R.id.tv_time_stamp);
            userCreate = view.findViewById(R.id.tv_userCreate);
            tv_project_state = view.findViewById(R.id.tv_project_state);
        }


        @SuppressLint("ResourceAsColor")
        public void initData(final ProjectModel projectModel) {
            if (projectModel.getStatus() == 0) {
                tv_project_state.setText("OPEN");
            } else {
                tv_project_state.setText("DONE");

            }
            nameproject.setText(projectModel.getName());
            String fisrt_name = projectModel.getName().substring(0, 1);
            card_name_project.setText(fisrt_name);
            userCreate.setText(projectModel.getUserDetail());
            int mau=(int)(Math.random() * ((5 - 1) + 1)) + 1;
            if(mau==1){
                card_avatar.setCardBackgroundColor(Color.parseColor("#b70505"));
            }
            if(mau==2){
                card_avatar.setCardBackgroundColor(Color.GRAY);
            }
            if(mau==3){
                card_avatar.setCardBackgroundColor(Color.YELLOW);
            }
            if(mau==4){
                card_avatar.setCardBackgroundColor(Color.CYAN);
            }
            if(mau==5){
                card_avatar.setCardBackgroundColor(Color.BLUE);
            }
           String deadline= projectModel.getEndDate().substring(0, 10);
            deadline=deadline.replace("-","/");

            try {
                end_date.setReferenceTime(setendate(deadline).getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isItemClicked) {
                        isItemClicked = true;
//                        itemView.setBackgroundColor(itemView.getContext().getColor(R.color.green));

                    } else {
                        isItemClicked = false;
                        itemView.setBackgroundColor(itemView.getContext().getColor(R.color.backgroundNull));
                    }
                    itemClicked.onItemClickedProject(getAdapterPosition(), projectModel);
                }
            });
        }
    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public ProjectAdapter(ArrayList<ProjectModel> ProjectModelList) {
        this.projectModelList = ProjectModelList;
    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    @NonNull
    @Override
    public ProjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);

        return new ProjectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProjectAdapter.MyViewHolder holder, final int position) {

        ProjectModel projectModel = projectModelList.get(position);
        holder.initData(projectModel);


    }

    public Calendar setendate(String enddate) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date date1 = formater.parse(enddate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }

}