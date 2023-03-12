package com.dev.android.sit_chat.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.android.sit_chat.Models.MessageModel;
import com.dev.android.sit_chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{


    ArrayList<MessageModel>messageModels;
    Context context;
    String rcvId;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String rcvId) {
        this.messageModels = messageModels;
        this.context = context;
        this.rcvId = rcvId;
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

           holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {

                   new AlertDialog.Builder(context)
                           .setTitle("Delete")
                           .setMessage("Are you sure you want to delete this message?")
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   FirebaseDatabase database=FirebaseDatabase.getInstance();
                                   String senderRoom=FirebaseAuth.getInstance().getUid()+rcvId;
                                   String receiveRoom=rcvId+FirebaseAuth.getInstance().getUid();
                                   database.getReference().child("chats").child(senderRoom)
                                           .child(messageModel.getMessageId())
                                           .setValue(null);
//                                   database.getReference().child("chats").child(receiveRoom)
//                                           .child(messageModel.getMessageId())
//                                           .setValue(null);
                               }
                           }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           }).show();

                   return false;
               }
           });

           if(holder.getClass()==SenderViewHolder.class){
               ((SenderViewHolder)holder).sendMsg.setText(messageModel.getMessage());

               Date date =new Date(messageModel.getTimestamp());
               SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
               String strDate=simpleDateFormat.format(date);
               ((SenderViewHolder)holder).sendTime.setText(strDate.toString());
           }else{
               ((ReceiverViewHolder)holder).receiveMsg.setText(messageModel.getMessage());
               Date date =new Date(messageModel.getTimestamp());
               SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
               String strDate=simpleDateFormat.format(date);
               ((ReceiverViewHolder)holder).receiveTime.setText(strDate.toString());
           }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


}
