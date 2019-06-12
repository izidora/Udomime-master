package com.example.udomime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AnimalAdapter extends ArrayAdapter<Animal> {

    private Context mContext;
    private List<Animal> animalList = new ArrayList<>();

    public AnimalAdapter(@NonNull Context context, ArrayList<Animal> list) {
        super(context, 0 , list);
        mContext = context;
        animalList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Animal currentAnimal = animalList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
        String url = currentAnimal.getUrl();
        if(url!="") {
            Picasso.get().load(url).into(image);
        }

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentAnimal.getName());

        TextView release = (TextView) listItem.findViewById(R.id.textView_breed);
        release.setText(currentAnimal.getBreed());

        return listItem;
    }
}