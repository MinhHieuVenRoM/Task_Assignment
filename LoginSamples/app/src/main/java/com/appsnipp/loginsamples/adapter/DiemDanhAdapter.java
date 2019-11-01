package com.appsnipp.loginsamples.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.DIEMDANH;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiemDanhAdapter extends RecyclerView.Adapter<DiemDanhAdapter.MyViewHolder>{
    public ArrayList<DIEMDANH> diemdanhArrayList;
    public ProjectItemClicked itemClicked;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameuser;
        TextView ngaydiemdanh;

        MyViewHolder(View view) {
            super(view);
            nameuser = view.findViewById(R.id.tv_nguoidiemdanh);
            ngaydiemdanh = view.findViewById(R.id.tv_ngaygio_diemdanhlich);
        }

        public void setOnItemClicked(ProjectModel model) {

        }

        public void initData(final DIEMDANH diemdanhmode) {
            nameuser.setText(diemdanhmode.getNAME());
            ngaydiemdanh.setText(diemdanhmode.getNGAY());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    itemClicked.onItemClicked(getAdapterPosition(),diemdanhmode );
                }
            });
        }
    }

    public DiemDanhAdapter(ArrayList<DIEMDANH> diemdanhs) {
        this.diemdanhArrayList = diemdanhs;
    }

    @Override
    public int getItemCount() {
        return diemdanhArrayList.size();
    }

    @NonNull
    @Override
    public DiemDanhAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lichdiemdanh_admin, parent, false);
        return new DiemDanhAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DiemDanhAdapter.MyViewHolder holder, final int position) {

        DIEMDANH projectModel = diemdanhArrayList.get(position);
        holder.initData(projectModel);

        //  holder.setOnItemClicked();
    }

    public Calendar setendate(String enddate) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date1 = formater.parse(enddate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }
}
