package com.appsnipp.loginsamples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Chat.DataGroup;
import com.appsnipp.loginsamples.model.Chat.DataRoom;
import com.appsnipp.loginsamples.model.Chat.Datum;
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

public class ChatGroupAdaterUserList extends RecyclerView.Adapter<ChatGroupAdaterUserList.MyViewHolder> {
    public ArrayList<DataGroup> userModelList;
    public ManagementGroupItemClicked itemClicked;
    private Context context;

    public ChatGroupAdaterUserList(Context context) {
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_emp_name;
        TextView tv_role;
        AppCompatImageView iv_avatar_user;


        RelativeTimeTextView end_date;

        MyViewHolder(final View view) {
            super(view);

            tv_emp_name = view.findViewById(R.id.tv_emp_name_chat);
            tv_role = view.findViewById(R.id.tv_role_chat);
            iv_avatar_user = view.findViewById(R.id.iv_avatar_chat);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
        }

        void initData(final DataGroup m) {
            tv_emp_name.setText(m.getRoomName());
            tv_role.setText(m.getTinnhancuoi());
            itemView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                    itemClicked.onItemClickeduser(getAdapterPosition(), m);
                }
            });
        }


    }

    public ChatGroupAdaterUserList(ArrayList<DataGroup> userModelList) {
        this.userModelList = userModelList;
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @NonNull
    @Override
    public ChatGroupAdaterUserList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_chat, parent, false);

        return new ChatGroupAdaterUserList.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatGroupAdaterUserList.MyViewHolder holder, final int position) {

        DataGroup userModel = userModelList.get(position);
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
