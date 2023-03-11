package com.dev.android.sit_chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.android.sit_chat.ChatDetailsActivity;
import com.dev.android.sit_chat.Models.Users;
import com.dev.android.sit_chat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

   private Context context;
   ArrayList<Users> list;

    public UsersAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView userName,lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.userNameList);
            lastMessage=itemView.findViewById(R.id.lastMessage);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_show,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Users users=list.get(position);
          Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar3).into(holder.image);
          holder.userName.setText(users.getUserName());


          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent=new Intent(context, ChatDetailsActivity.class);
                  intent.putExtra("userId",users.getUserId());
                  intent.putExtra("profilePic",users.getProfilePic());
                  intent.putExtra("userName",users.getUserName());
                  context.startActivity(intent);
              }
          });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
