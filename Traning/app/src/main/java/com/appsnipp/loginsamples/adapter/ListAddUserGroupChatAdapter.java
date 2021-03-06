package com.appsnipp.loginsamples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.appsnipp.loginsamples.R.drawable.avtadmin;
import static com.appsnipp.loginsamples.R.drawable.avtchat;
import static com.appsnipp.loginsamples.R.drawable.avtnam;
import static com.appsnipp.loginsamples.R.drawable.avtnupng;
import static com.appsnipp.loginsamples.R.drawable.avttre;
import static com.appsnipp.loginsamples.R.drawable.nvnu;

public class ListAddUserGroupChatAdapter extends RecyclerView.Adapter<ListAddUserGroupChatAdapter.MyViewHolder> {
    public ArrayList<UserModelDetail> userModelList;
    public ManagementUserItemClicked itemClicked;
    private Context context;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_emp_name;
        AppCompatImageView iv_avatar_user;
        RelativeLayout mLayoutContainer;


        RelativeTimeTextView end_date;

        MyViewHolder(final View view) {
            super(view);

            tv_emp_name = view.findViewById(R.id.tv_emp_name_chat);
            iv_avatar_user = view.findViewById(R.id.iv_avatar_chat);
            mLayoutContainer = view.findViewById(R.id.rl_item_add_chat);
        }

        void initData(final UserModelDetail m) {

            if (m.getSex() == 0) {
                int namsinh = Integer.parseInt(m.getDob().substring(0, 4));
                if (m.getRole() == 0) {

                    iv_avatar_user.setImageResource(avtadmin);

                } else if (m.getRole() != 0 && namsinh < 2000) {

                    iv_avatar_user.setImageResource(avtchat);

                } else if (m.getRole() != 0 && namsinh < 1998) {

                    iv_avatar_user.setImageResource(avtnam);

                } else {

                    iv_avatar_user.setImageResource(avttre);
                }
            } else {
                if (m.getRole() == 0) {
                    iv_avatar_user.setImageResource(avtnupng);
                } else {
                    iv_avatar_user.setImageResource(nvnu);
                }

            }
            tv_emp_name.setText(m.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = userModelList.get(getAdapterPosition()).isChecked();
                    userModelList.get(getAdapterPosition()).setChecked(!isChecked);
                    itemClicked.onItemClickeduser(getAdapterPosition(), m);
                }
            });

            setCheckedItem(userModelList.get(getAdapterPosition()).isChecked());
        }

        private void setCheckedItem(boolean isChecked){
            if (isChecked) {
                mLayoutContainer.setBackgroundColor(context.getColor(R.color.blue));

            } else {
                mLayoutContainer.setBackgroundColor(context.getColor(R.color.whiteCardColor));
            }
        }

    }

    public ListAddUserGroupChatAdapter(ArrayList<UserModelDetail> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @NonNull
    @Override
    public ListAddUserGroupChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_user_chat, parent, false);

        return new ListAddUserGroupChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListAddUserGroupChatAdapter.MyViewHolder holder, final int position) {

        UserModelDetail userModel = userModelList.get(position);
        holder.initData(userModel);


    }


    public Calendar setendate(String enddate) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date date1 = formater.parse(enddate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }


}
