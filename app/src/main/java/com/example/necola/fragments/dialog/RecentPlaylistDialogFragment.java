package com.example.necola.fragments.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.necola.databinding.FragmentItemRecentPlaylistDialogItemBinding;
import com.example.necola.databinding.FragmentItemRecentPlaylistDialogListLayoutBinding;
import com.example.necola.entity.Music;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     RecentPlaylistDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class RecentPlaylistDialogFragment extends BottomSheetDialogFragment {

    //Customize parameter argument names
    //private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentItemRecentPlaylistDialogListLayoutBinding binding;
    RecyclerView recyclerView;

    public static RecentPlaylistDialogFragment newInstance() {
        final RecentPlaylistDialogFragment fragment = new RecentPlaylistDialogFragment();
        //final Bundle args = new Bundle();
        //args.putInt(ARG_ITEM_COUNT, itemCount);
        //fragment.setArguments(args);
        return fragment;
    }

    public void refresh(ArrayList<Music.RecentPlay<Music.Playlist>> recentPlaylistArrayList) {
        recyclerView.setAdapter(new RecentPlaylistDialogFragment.RecentPlaylistAdapter(recentPlaylistArrayList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentItemRecentPlaylistDialogListLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final MaterialCardView item;
        final MaterialTextView name;
        final ShapeableImageView image;


        ViewHolder(FragmentItemRecentPlaylistDialogItemBinding binding) {
            super(binding.getRoot());
            item=binding.item;
            name=binding.name;
            image=binding.image;

        }

    }

    private class RecentPlaylistAdapter extends RecyclerView.Adapter<ViewHolder> {

        ArrayList<Music.RecentPlay<Music.Playlist>> recentPlaylist;

        RecentPlaylistAdapter(ArrayList<Music.RecentPlay<Music.Playlist>> recentPlaylist) {
           this.recentPlaylist=recentPlaylist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentItemRecentPlaylistDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Music.RecentPlay<Music.Playlist> playlist=recentPlaylist.get(position);
            holder.name.setText(playlist.getResource().getName());
            Picasso.get().load(playlist.getResource().getCoverImgUrl()).into(holder.image);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return recentPlaylist.size();
        }

    }
}