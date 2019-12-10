package com.appsnipp.loginsamples.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Message;

import java.util.ArrayList;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private ArrayList<Message> MessageList;
    private int RIGHT = 0;
    private int LEFT = 1;
    private String mNickname;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView message;

        MyViewHolder(View view) {
            super(view);

            nickname = view.findViewById(R.id.nickname);
            message = view.findViewById(R.id.message);
        }
    }

    // in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity
    public ChatBoxAdapter(ArrayList<Message> MessagesList, String nickname) {
        this.MessageList = MessagesList;
        this.mNickname = nickname;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    @NonNull
    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == RIGHT) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_message, parent, false);
        }

        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position) {

        Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());
        holder.message.setText(m.getMessage());
        if (MessageList.get(position).getNickname().equals(mNickname)) {
            holder.nickname.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (MessageList.get(position).getNickname().equals(mNickname))
            return RIGHT;
        return LEFT;
    }
}