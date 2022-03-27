package com.example.necola.fragments;

import static com.example.necola.utils.httpAPI.ResourceUtil.MUSIC_LIBRARY_HOST;
import static com.example.necola.utils.httpAPI.ResourceUtil.asyncGET;
import static com.example.necola.utils.httpAPI.ResourceUtil.handle_response;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.necola.ContentActivity;
import com.example.necola.MyApplication;
import com.example.necola.R;
import com.example.necola.entity.Music;
import com.example.necola.utils.GalleryViewPagerAdapter;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.utils.httpAPI.resource.netease.Recommend;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DiscoverFragment extends Fragment {

    // creating object of ViewPager
    ViewPager mViewPager;
    ViewPager2 pager;

    List<CollectionFragment> fragmentList;
    // images array
    int[] images = {R.drawable.surface,R.drawable.mi_smile,R.drawable.sun};

    // Creating Object of ViewPagerAdapter
    GalleryViewPagerAdapter mViewPagerAdapter;
    LinearProgressIndicator linearProgressIndicator;


    //
    RecyclerView recommendView;
    RecyclerView recommendView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        // Initializing the ViewPager Object

        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerMain);
        linearProgressIndicator = view.findViewById(R.id.gallery_indicator);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new GalleryViewPagerAdapter(getContext(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setTimer(mViewPager, linearProgressIndicator, 2);

        recommendView=(RecyclerView) view.findViewById(R.id.recommend_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        recommendView.setLayoutManager(layoutManager);

        recommendView2=(RecyclerView) view.findViewById(R.id.recommend_list2);
        recommendView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));

        //syncLoad();
        asyncLoad();

        view.findViewById(R.id.leaderboard).setOnClickListener(view1 -> startActivity(new Intent(getActivity(), ContentActivity.class).putExtra("container_layout",R.layout.activity_content_leaderboard)));
        view.findViewById(R.id.artist).setOnClickListener(view12 -> startActivity(new Intent(getActivity(), ContentActivity.class).putExtra("container_layout",R.layout.activity_content_artist)));




        return view;
    }


    void syncLoad(){
        Recommend.Playlists rplaylists=new Recommend.Playlists();
        RecommendAdapter adapter=new RecommendAdapter(rplaylists.get(getContext()));
        recommendView.setAdapter(adapter);
    }
    void asyncLoad() {
        Callback callback = new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() < 300) {
                    JSONObject currentResults = handle_response(response);
                    JSONArray recommendArray = null;
                    List<Music.Recommend> recommends = new ArrayList<>();
                    try {
                        recommendArray = currentResults.getJSONArray("recommend");

                        for (int i = 0; i < recommendArray.length(); i++) {
                            JSONObject object = recommendArray.getJSONObject(i);
                            recommends.add(new Music.Recommend(
                                    object.getLong("id"),
                                    object.getString("name"),
                                    object.getString("copywriter"),
                                    object.getString("picUrl")));
                        };
                        if(getActivity()!=null)
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                int size=recommends.size();
                                RecommendAdapter adapter2=new RecommendAdapter((List<Music.Recommend>) recommends.subList(0,size/2));
                                recommendView2.setAdapter(adapter2);
                                RecommendAdapter adapter=new RecommendAdapter((List<Music.Recommend>) recommends.subList(size/2,size));
                                recommendView.setAdapter(adapter);
                                recommendView.scrollToPosition(adapter.getItemCount()-1);


                                if(getActivity()!=null) {
                                    MyApplication m=((MyApplication) getActivity().getApplication());
                                    final Boolean A_GET_LOCK=false;
                                    final Boolean B_GET_LOCK=true;
                                    m.setFlag(B_GET_LOCK);

                                    recommendView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (m.getFlag() == A_GET_LOCK) {
                                                recommendView2.scrollBy(recommendView2.getScrollX() - dx, recommendView2.getScrollY() - dy);
                                                m.setFlag(B_GET_LOCK);
                                            } else m.setFlag(A_GET_LOCK);

                                        }
                                    });

                                    recommendView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            if (m.getFlag() == B_GET_LOCK) {
                                                recommendView.scrollBy(recommendView.getScrollX() - dx, recommendView.getScrollY() - dy);
                                                m.setFlag(A_GET_LOCK);
                                            } else m.setFlag(B_GET_LOCK);

                                        }
                                    });
                                }




                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        };

        asyncGET(getContext(),callback,MUSIC_LIBRARY_HOST+"recommend/resource");
    }





        private class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
        private class ViewHolder extends RecyclerView.ViewHolder {
            ShapeableImageView imageView;
            MaterialTextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.recommend_cover);
                textView=itemView.findViewById(R.id.recommend_name);
            }
        }

        List<Music.Recommend> recommendPlaylists;
        public RecommendAdapter(List<Music.Recommend> recommendPlaylists){
            this.recommendPlaylists=recommendPlaylists;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_card,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Music.Recommend recommendPlaylist=recommendPlaylists.get(position);
            holder.textView.setText(recommendPlaylist.getName());
            Picasso.get().load(recommendPlaylist.getPicUrl()).into(holder.imageView);


            //new DownloadImageTask(holder.imageView).execute(recommendPlaylist.getPicUrl());

        }

        @Override
        public int getItemCount() {
            return recommendPlaylists.size();
        }


    }
}
