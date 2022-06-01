package com.luis;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luis.pojos.MensajeChat;

import java.util.List;

public class MensajeRecyclerAdapter extends RecyclerView.Adapter {

    private Context c;
    private List<MensajeChat> msgs;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public List<MensajeChat> getMsgs(){
        return msgs;
    }

    public MensajeRecyclerAdapter(Context context, List<MensajeChat> messageList) {
        c = context;
        msgs= messageList;
    }

    private class YoMsgHolder extends RecyclerView.ViewHolder{

        TextView  message_time, message_text;

        public YoMsgHolder(@NonNull View itemView) {
            super(itemView);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);
        }

        void bind(MensajeChat msg){
            message_time.setText(DateUtils.formatDateTime(c,msg.getMessageTime(),DateUtils.FORMAT_SHOW_TIME));
            message_text.setText(msg.getMessageText());
        }
    }

    private class OtherMsgHolder extends RecyclerView.ViewHolder{

        TextView message_user, message_time, message_text;

        public OtherMsgHolder(@NonNull View itemView) {
            super(itemView);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);
            message_user = itemView.findViewById(R.id.message_user);
        }

        void bind(MensajeChat msg){
            message_user.setText(msg.getMessageUser());
            message_time.setText(DateUtils.formatDateTime(c,msg.getMessageTime(),DateUtils.FORMAT_ABBREV_ALL));
            message_text.setText(msg.getMessageText());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_me, parent, false);
            return new YoMsgHolder(v);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_other, parent, false);
            return new OtherMsgHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MensajeChat msg = msgs.get(position);

        switch(holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((YoMsgHolder) holder).bind(msg);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((OtherMsgHolder) holder).bind(msg);
                break;
            default:
        }

    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public int getItemViewType(int position){
        MensajeChat m = msgs.get(position);

        if(m.getMessageUser().equals("Tú")){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return  VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
}
