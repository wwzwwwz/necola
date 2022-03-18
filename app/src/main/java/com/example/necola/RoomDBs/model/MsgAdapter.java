package com.example.necola.RoomDBs.model;

import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.R;
import com.example.necola.entity.Msg;
import com.example.necola.service.background.MessageService;
import com.google.android.material.card.MaterialCardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

///////////////////////////////Message ListAdapter/////////////
public class MsgAdapter extends ListAdapter<Msg, MsgAdapter.ViewHolder> {

    public Boolean isHyperLink(String link) {
        final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(link);//replace with string to compare

        if (m.find()) {
            // Log.d("group1",m.group(0));
            //Log.d("group1",m.group(1));
            return true;
        } else return false;

    }

    public MsgAdapter(@NonNull DiffUtil.ItemCallback<Msg> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg currentItem = getItem(position);
        holder.bind(currentItem);

        if (isHyperLink(holder.leftMsg.getText().toString())) {
            holder.leftMsg.setMovementMethod(LinkMovementMethod.getInstance());
            holder.leftMsg.setLinkTextColor(Color.YELLOW);
        }
        ;

        if (isHyperLink(holder.rightMsg.getText().toString())) {
            holder.rightMsg.setText(Html.fromHtml("<a href=\"https://" + holder.rightMsg.getText().toString() + "\">" + holder.rightMsg.getText().toString() + "</a>"));
            holder.rightMsg.setMovementMethod(LinkMovementMethod.getInstance());
            holder.rightMsg.setLinkTextColor(Color.YELLOW);
        }
        ;

        holder.leftMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("left", "left");
                //model.setSelected(!model.isSelected());
                //holder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

        holder.rightMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("right", "right");

                //model.setSelected(!model.isSelected());
                //holder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView leftLayout;
        MaterialCardView rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);


        }

        public void bind(Msg item) {
            if (item.getType() == Msg.TYPE_RECEIVED) {
                leftLayout.setVisibility(View.VISIBLE);
                rightLayout.setVisibility(View.GONE);
                leftMsg.setText(item.getContent());
            } else if (item.getType() == Msg.TYPE_SENT) {
                rightLayout.setVisibility(View.VISIBLE);
                leftLayout.setVisibility(View.GONE);
                rightMsg.setText(item.getContent());
            }
        }

        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg, parent, false);
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
