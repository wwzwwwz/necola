package com.example.necola.fragments.dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.necola.PlayingActivity;
import com.example.necola.R;
import com.example.necola.databinding.FragmentItemRecentSongDialogItemBinding;
import com.example.necola.databinding.FragmentItemRecentSongDialogListLayoutBinding;
import com.example.necola.entity.Music;
import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     RecentPlaySongListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class RecentPlaySongListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String RECENT_PLAYSONG_LIST="recent_play_song";
    private FragmentItemRecentSongDialogListLayoutBinding binding;

    RecyclerView recyclerView;

    // TODO: Customize parameters
    public static RecentPlaySongListDialogFragment newInstance() {
        final RecentPlaySongListDialogFragment fragment = new RecentPlaySongListDialogFragment();
        //final Bundle args = new Bundle();
        //args.putSerializable(RECENT_PLAYSONG_LIST,recentPlayList);
        //fragment.setArguments(args);
        return fragment;
    }

    public void refresh(ArrayList<Music.RecentPlay<Music.Song>> recentPlayArrayList) {
        recyclerView.setAdapter(new RecentPlaySongListDialogFragment.RecentPlaySongAdapter(recentPlayArrayList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentItemRecentSongDialogListLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setAdapter(new RecentPlaySongListDialogFragment.RecentPlaySongAdapter((ArrayList<Music.RecentPlay<Music.Song>>) getArguments().getSerializable(RECENT_PLAYSONG_LIST)));

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final MaterialCardView item;
        final MaterialTextView musicTitle;
        final MaterialTextView musicArtist;

        ViewHolder(FragmentItemRecentSongDialogItemBinding binding) {
            super(binding.getRoot());
            item=binding.item;
            musicArtist=binding.musicItemArtist;
            musicTitle=binding.musicItemTitle;
        }

    }

    private class RecentPlaySongAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<Music.RecentPlay<Music.Song>> recentPlayArrayList;
        RecentPlaySongAdapter(ArrayList<Music.RecentPlay<Music.Song>> recentPlayArrayList) {
           this.recentPlayArrayList=recentPlayArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentItemRecentSongDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Music.RecentPlay<Music.Song> song=recentPlayArrayList.get(position);
            holder.musicTitle.setText(song.getResource().getTitle());
            holder.musicArtist.setText(song.getResource().getArrayListToString());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startIntent=new Intent(getActivity(), SoundService.class);
                    startIntent.putExtra("song",song.getResource());
                    startIntent.setAction(MusicConstants.ACTION.START_ACTION);
                    getActivity().startService(startIntent);

                    Intent intent=new Intent(getActivity(), PlayingActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
            });
        }

        @Override
        public int getItemCount() {
            return recentPlayArrayList.size();
        }

    }
}