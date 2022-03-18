package com.example.necola.utils.httpAPI.resource.netease;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.R;
import com.example.necola.entity.Music;
import com.example.necola.utils.httpAPI.ResourceUtil;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArtistFilter {
    String url;
    public ArtistFilter(Builder builder){
        this.url=builder.url;
    }

    public String getUrl() {
        return url;
    }

    static public class Builder{
        Boolean noSet;
        String url;
        public Builder(){
            this.url= ResourceUtil.MUSIC_LIBRARY_HOST+"artist/list";
            this.noSet=true;
        }

        public Builder setType(int type){
            if (noSet)
            this.url= this.url+"?type="+type;
            else
                this.url= this.url+"&type="+type;
            noSet=false;
            return this;
        }

        public Builder setArea(int area){

            if (noSet)
            {
                this.url= this.url+"?area="+area;
                noSet=false;
            }
            else
                this.url= this.url+"&area="+area;

            return this;
        }

        public Builder setLimit(int limit){
            if (noSet)
            {
                this.url= this.url+"?limit="+limit;
                noSet=false;
            }
            else
                this.url= this.url+"&limit="+limit;

            return this;
        }
        public Builder setOffset(int offset){

            if (noSet) {
                this.url = this.url + "?offset=";
                noSet = false;
            }
            else
                this.url= this.url+"&offset=";

            return this;
        }
        public Builder setInitial(String c){

            if (noSet)
            {
                this.url= this.url+"?initial="+c;
                noSet=false;
            }
            else
                this.url= this.url+"&initial="+c;

            return this;
        }
        public ArtistFilter build(){
            noSet=true;
            return new ArtistFilter(this);
        }

    }


    static public class ArtistViewAdapter extends RecyclerView.Adapter<ArtistViewAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            ShapeableImageView imageView;
            MaterialTextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.artist_img);
                textView=itemView.findViewById(R.id.artist_name);
            }
        }

        List<Music.Artist> artistList;
        public ArtistViewAdapter(List<Music.Artist> artistList){
            this.artistList=artistList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_card,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistViewAdapter.ViewHolder holder, int position) {
            Music.Artist artist=artistList.get(position);
            holder.textView.setText(artist.getName());
            Picasso.get().load(artist.getPicUrl()).fit().into(holder.imageView);
            //new DownloadImageTask(holder.imageView).execute(recommendPlaylist.getPicUrl());

        }

        @Override
        public int getItemCount() {
            return artistList.size();
        }


    }
}
