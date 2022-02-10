package com.example.necola.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.FragmentTabActivity;
import com.example.necola.R;
import com.example.necola.entity.Contacts;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contacts> mContactList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fruitImage;
        TextView name;

        public ViewHolder(View view){
            super(view);
            fruitImage=(ImageView) view.findViewById(R.id.fruit_image);
            name=(TextView) view.findViewById(R.id.fruit_name);

        }
    }

    public ContactAdapter(List<Contacts> contactList){

        mContactList=contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Contacts contact=mContactList.get(position);
        holder.fruitImage.setImageResource(contact.getImageId());
        holder.name.setText(contact.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentTabActivity) v.getContext()).onClickCalled(contact.getName());
            }
        });

    }

    @Override
    public int getItemCount(){
        return mContactList.size();
    }


}
