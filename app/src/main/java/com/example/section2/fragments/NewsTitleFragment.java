package com.example.section2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section2.NewsContentActivity;
import com.example.section2.R;
import com.example.section2.entity.News;

import java.util.ArrayList;
import java.util.List;

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedInstanceState){

       View view=inflater.inflate(R.layout.news_title_frag,containter,false);
       RecyclerView newsTitleRecyclerView =(RecyclerView) view.findViewById(
               R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter=new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);

        return view;
    }
    private  List<News> getNews(){
        List <News> newsList=new ArrayList<>();
        for(int i=1;i<=50;i++){
            News news=new News();
            news.setTitle("This is news title"+i);
            news.setContent(""+i*10);
            newsList.add(news);
        };
        return  newsList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content)!=null){
            isTwoPane=true; //找到news_content_layout布局则为双页模式
        }
        else{
            isTwoPane=false;
        }
    }


    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<News> mNewsList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public ViewHolder(View view){
                super(view);
                newsTitleText=(TextView) view.findViewById(R.id.news_title);

            }
        }
        public NewsAdapter(List<News>newsList){
            mNewsList=newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news=mNewsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        NewsContentFragment newsContentFragment=(NewsContentFragment)
                                getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }
                    else{
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());
                    }
                }
            });
                        return holder;}


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news =mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }


        @Override
        public int getItemCount() {
            return mNewsList.size();
        }


    }
}