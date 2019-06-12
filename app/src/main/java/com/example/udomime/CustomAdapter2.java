package com.example.udomime;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter2 extends PagerAdapter {
    //private int[] images = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5};
    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<Animal> images;

    public CustomAdapter2(Context ctx, ArrayList<Animal> picList){
        this.ctx = ctx;
        this.images = picList;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe2,container,false);
        ImageView img =(ImageView)v.findViewById(R.id.imageView2);
        ImageView img2 = (ImageView)v.findViewById(R.id.imageView3);
        Picasso.get().load(images.get(position).getUrl()).into(img);
        Picasso.get().load(images.get(position+1).getUrl()).into(img2);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}