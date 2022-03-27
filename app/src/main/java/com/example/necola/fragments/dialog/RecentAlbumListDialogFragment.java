package com.example.necola.fragments.dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.necola.PlayingActivity;
import com.example.necola.R;
import com.example.necola.databinding.FragmentItemRecentAlbumDialogItemBinding;
import com.example.necola.databinding.FragmentItemRecentAlbumDialogListLayoutBinding;
import com.example.necola.entity.Music;
import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
 *     RecentAlbumListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class RecentAlbumListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentItemRecentAlbumDialogListLayoutBinding binding;

    RecyclerView recyclerView;

    public static RecentAlbumListDialogFragment newInstance() {
        final RecentAlbumListDialogFragment fragment = new RecentAlbumListDialogFragment();
        //final Bundle args = new Bundle();
        //args.putInt(ARG_ITEM_COUNT, itemCount);
        //fragment.setArguments(args);
        return fragment;
    }
    public void refresh(ArrayList<Music.RecentPlay<Music.Album>> recentAlbumArrayList) {
        recyclerView.setAdapter(new RecentAlbumListDialogFragment.RecentAlbumAdapter(recentAlbumArrayList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentItemRecentAlbumDialogListLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        //recyclerView.setAdapter(new RecentAlbumAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final MaterialCardView item;
        final ShapeableImageView image;
        final MaterialTextView text;

        ViewHolder(FragmentItemRecentAlbumDialogItemBinding binding) {
            super(binding.getRoot());
            item=binding.item;
            image=binding.image;
            text=binding.text;
        }

    }

    private class RecentAlbumAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<Music.RecentPlay<Music.Album>> recentAlbumArrayList;
        RecentAlbumAdapter(ArrayList<Music.RecentPlay<Music.Album>> recentAlbumArrayList) {
           this.recentAlbumArrayList=recentAlbumArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentItemRecentAlbumDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Music.RecentPlay<Music.Album> album=recentAlbumArrayList.get(position);
            holder.text.setText(album.getResource().getName());
            Picasso.get().load(album.getResource().getPicUrl()).into(holder.image);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return recentAlbumArrayList.size();
        }

    }
}