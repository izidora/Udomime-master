package com.example.udomime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe2,container,false);

        LinearLayout l1 = (LinearLayout) v.findViewById(R.id.animalContainer1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AnimalProfile.class);
                Bundle b = new Bundle();
                b.putInt("animalId", images.get(position).getIdAnimal()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                ctx.startActivity(intent);
            }
        });
        LinearLayout l2 = (LinearLayout) v.findViewById(R.id.animalContainer2);
        ImageView img =(ImageView)v.findViewById(R.id.imageView2);
        TextView tv1 = (TextView) v.findViewById(R.id.textView10);
        tv1.setText(images.get(position).getName());
        Picasso.get().load(images.get(position).getUrl()).into(img);

        ImageView img2 = (ImageView)v.findViewById(R.id.imageView3);
        TextView tv2 = (TextView) v.findViewById(R.id.textView11);
        if(position+1 < images.size()) {
            Picasso.get().load(images.get(position+1).getUrl()).into(img2);
            tv2.setText(images.get(position+1).getName());
            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, AnimalProfile.class);
                    Bundle b = new Bundle();
                    b.putInt("animalId", images.get(position).getIdAnimal()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    ctx.startActivity(intent);
                }
            });
        }
        else {
            l2.setVisibility(View.GONE);
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}