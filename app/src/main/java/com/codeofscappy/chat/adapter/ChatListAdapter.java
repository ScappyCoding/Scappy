package com.codeofscappy.chat.adapter;


import static com.bumptech.glide.Glide.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codeofscappy.chat.R;
import com.codeofscappy.chat.model.Chatlist;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<Chatlist> list;
    private Context context;

    public ChatListAdapter(List<Chatlist> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list,parent, false);
    return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Chatlist chatlist = list.get(position);

        holder.tvName.setText(chatlist.getUserName());
        holder.tvDesc.setText(chatlist.getDescription());
        holder.tvDate.setText(chatlist.getDate());

        // for image i need API-library....
        Glide.with(context).load(chatlist.getUrlProfile()).into(holder.profile);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDesc, tvDate;
        private CircularImageView profile;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_Date);
            tvDesc = itemView.findViewById(R.id.tv_Desc);
            tvName = itemView.findViewById(R.id.tv_Name);
            profile = itemView.findViewById(R.id.image_profile);
        }
    }
}
