package com.dev.android.sit_chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.android.sit_chat.Models.MessageModel;
import com.dev.android.sit_chat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{


    ArrayList<MessageModel>messageModels;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiveMsg,receiveTime;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveMsg=itemView.findViewById(R.id.receiverTxt);
            receiveTime=itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends  RecyclerView.ViewHolder{
        TextView sendMsg,sendTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMsg=itemView.findViewById(R.id.senderTxt);
            sendTime=itemView.findViewById(R.id.senderTime);
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return  new SenderViewHolder(view);
        }else{
            View view=LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
          if(messageModels.get(position).getuserId().equals(FirebaseAuth.getInstance().getUid())){
              return SENDER_VIEW_TYPE;
          }else{
              return RECEIVER_VIEW_TYPE;
          }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel=messageModels.get(position);
           if(holder.getClass()==SenderViewHolder.class){
               ((SenderViewHolder)holder).sendMsg.setText(messageModel.getMessage());
           }else{
               ((ReceiverViewHolder)holder).receiveMsg.setText(messageModel.getMessage());
           }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


}
