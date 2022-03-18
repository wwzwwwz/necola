package com.example.necola.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.ContentActivity;
import com.example.necola.PlayingActivity;
import com.example.necola.R;
import com.example.necola.entity.Music;
import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;

import java.util.ArrayList;


public class SearchTitleFragment extends Fragment {
    public boolean isTwoPane;
    RecyclerView musicTitleRecyclerView;
    LinearLayoutManager layoutManager;
    public ArrayList<Music.Song> getDefaultSongs(){

        return new ArrayList<Music.Song>();
    }

    public void refresh(ArrayList<Music.Song> songs){
        layoutManager=new LinearLayoutManager((getActivity()));
        musicTitleRecyclerView.setLayoutManager(layoutManager);
        SearchResultsAdapter adapter=new SearchResultsAdapter(songs);
        musicTitleRecyclerView.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedInstanceState){

       View view=inflater.inflate(R.layout.fragment_search_title,containter,false);
       musicTitleRecyclerView =(RecyclerView) view.findViewById(
               R.id.music_title_recycler_view);
        layoutManager=new LinearLayoutManager((getActivity()));
        musicTitleRecyclerView.setLayoutManager(layoutManager);
        SearchResultsAdapter adapter=new SearchResultsAdapter(getDefaultSongs());
        musicTitleRecyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.music_content_layout)!=null){
            isTwoPane=true; //找到music_content则为双页模式
        }
        else{
            isTwoPane=false;
        }
    }


    class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
        private ArrayList<Music.Song> mSongsList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView songTitleText;
            TextView songArtist;
            public ViewHolder(View view){
                super(view);
                songTitleText=view.findViewById(R.id.music_item_title);
                songArtist=view.findViewById(R.id.music_item_artist);

            }
        }
        public SearchResultsAdapter(ArrayList<Music.Song> songsList){
            mSongsList=songsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Music.Song song =mSongsList.get(holder.getAdapterPosition());


                    Intent startIntent=new Intent(getActivity(), SoundService.class);
                    startIntent.putExtra("song",song);
                    startIntent.setAction(MusicConstants.ACTION.START_ACTION);
                    getActivity().startService(startIntent);

                    if(isTwoPane){
                        SearchContentFragment searchContentFragment =(SearchContentFragment)
                                getFragmentManager().findFragmentById(R.id.music_content_fragment);
                        searchContentFragment.refresh(song);
                    }
                    else{

                        //ContentActivity.actionStart(getActivity(),song);
                        Intent intent=new Intent(getActivity(), PlayingActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                        //context.startActivity(intent);

                    }
                }
            });
                        return holder;}


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Music.Song song =mSongsList.get(position);
            holder.songTitleText.setText(song.getTitle());
            holder.songArtist.setText(song.getArrayListToString());
        }



        @Override
        public int getItemCount() {
            return mSongsList.size();
        }


    }
}