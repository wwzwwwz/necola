package com.example.necola.fragments;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.ChatActivity;
import com.example.necola.FragmentTabActivity;
import com.example.necola.MyApplication;
import com.example.necola.R;
import com.example.necola.RoomDBs.model.MsgAdapter;
import com.example.necola.entity.Contacts;
import com.example.necola.entity.Msg;
import com.example.necola.RoomDBs.repository.MsgRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class ContactFragment extends Fragment {


        private String[] data={"apple","Banana","Pear","Orange"};
        public List<Contacts> contactList=new ArrayList<>();
        ContactAdapter contactAdapter;
        RecyclerView recyclerView;
        ContactViewModel contactViewModel;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_contact, container, false);


            //initContacts();
            recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
            recyclerView.setLayoutManager(layoutManager);
            contactAdapter=new ContactAdapter(new MsgAdapter.MsgDiff());
            recyclerView.setAdapter(contactAdapter);

            contactViewModel=new ContactViewModel(getActivity().getApplication());
            contactViewModel.getmAllRecentcontacts().observe(getViewLifecycleOwner(),contact->contactAdapter.submitList(contact));

            FloatingActionButton edit=view.findViewById(R.id.choose_edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(getActivity())
                            .setTitle("Please Input Username");
                    // Set up the input

                    final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    materialAlertDialogBuilder.setView(input);

// Set up the buttons
                    materialAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text = input.getText().toString();
                            Intent intent=new Intent(getActivity(), ChatActivity.class);
                          ;
                            intent.putExtra("currentUsername",((MyApplication) getActivity().getApplication()).getCurrentUsername());
                            intent.putExtra("currentContact",m_Text);
                            startActivity(intent);


                        }
                    });
                    materialAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    materialAlertDialogBuilder.show();
                }
            });

            return view;
        }
        public void refresh(){

            recyclerView.setAdapter(contactAdapter);
        }
        private void initContacts(){

            Contacts me=new Contacts("test",R.drawable.apple);
            contactList.add(me);

            Contacts he=new Contacts("test1",R.drawable.blackberry);
            contactList.add(he);


            }


    public static class ContactViewModel extends AndroidViewModel {
        private MsgRepository msgRepository;
        private final LiveData<List<Msg>> mAllRecentcontacts;


        public ContactViewModel(Application application) {
            super(application);
            msgRepository=new MsgRepository(application);
            mAllRecentcontacts=msgRepository.getAllRecentContacts();

        }

        public LiveData<List<Msg>> getmAllRecentcontacts(){return mAllRecentcontacts; }
    }

    public static class ContactAdapter extends ListAdapter<Msg, ContactAdapter.ViewHolder> {

        public ContactAdapter(@NonNull DiffUtil.ItemCallback<Msg> diffCallback) {

            super(diffCallback);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            Msg msg=getItem(position);

            holder.fruitImage.setImageResource(R.drawable.apple);
            holder.name.setText(msg.contact);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentTabActivity) v.getContext()).onClickCalled(msg.contact);
                }
            });

        }



        static class ViewHolder extends RecyclerView.ViewHolder{
            ImageView fruitImage;
            TextView name;

            public ViewHolder(View view){
                super(view);
                fruitImage=(ImageView) view.findViewById(R.id.fruit_image);
                name=(TextView) view.findViewById(R.id.fruit_name);

            }
        }


    }
}
