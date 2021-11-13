package com.example.section2.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section2.R;
import com.example.section2.entity.Msg;

import java.util.List;

public class MsgAdapter extends ListAdapter<Msg,MsgAdapter.ViewHolder> {

    public MsgAdapter(@NonNull DiffUtil.ItemCallback<Msg> diffCallback) {
        super(diffCallback);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg currentItem=getItem(position);
        holder.bind(currentItem);
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View view){
            super(view);
            leftLayout=(LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg=(TextView) view.findViewById(R.id.left_msg);
            rightMsg=(TextView) view.findViewById(R.id.right_msg);
        }
        public void bind(Msg item){
            if(item.getType()==Msg.TYPE_RECEIVED){
                leftLayout.setVisibility(View.VISIBLE);
                rightLayout.setVisibility(View.GONE);
                leftMsg.setText(item.getContent());
            }
            else if(item.getType()==Msg.TYPE_SENT){
                rightLayout.setVisibility(View.VISIBLE);
                leftLayout.setVisibility(View.GONE);
                rightMsg.setText(item.getContent());
            }
        }
        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_item, parent, false);
            return new ViewHolder(view);
        }
    }

    public static class MsgDiff extends DiffUtil.ItemCallback<Msg> {

        @Override
        public boolean areItemsTheSame(@NonNull Msg oldItem, @NonNull Msg newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Msg oldItem, @NonNull Msg newItem) {
            return oldItem.getContent().equals(newItem.getContent());
        }
    }
}
