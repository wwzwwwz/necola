package com.example.necola.utils;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.necola.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GalleryViewPagerAdapter extends PagerAdapter {
    // Context object
    Context context;

    // Array of images
    int[] images;


    LayoutInflater mLayoutInflater;

    final Handler handler = new Handler();
    public Timer swipeTimer ;


    /**
     *  this function swipes pages left to right for every 7 seconds
     * @param myPager its viewpager.
     * @param time as second, for 7 second enter 7
     *
     */
    public void setTimer(final ViewPager myPager, LinearProgressIndicator mIndicator, int time){
        final double size =getCount();


        final Runnable Update = new Runnable() {
            public void run() {
                if (myPager.getCurrentItem() == size-1 ) {
                    myPager.setCurrentItem(0);

                }
                else
                    myPager.setCurrentItem(myPager.getCurrentItem()+1, true);
                mIndicator.setProgress((int) (myPager.getCurrentItem()/(size-1)*100));
            }
        };



        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000 , time*1000);




    }


    // Viewpager Constructor
    public GalleryViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.length;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item_viewpager, container, false);

        // referencing the image view from the item.xml file
        ShapeableImageView imageView = (ShapeableImageView) itemView.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        imageView.setImageResource(images[position]);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}

