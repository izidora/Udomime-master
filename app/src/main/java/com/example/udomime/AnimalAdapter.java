package com.example.udomime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnimalAdapter extends ArrayAdapter<Animal> {


    private SessionHandler session;
    private Context mContext;
    private List<Animal> animalList = new ArrayList<>();
    private RequestQueue rQueue;

    public AnimalAdapter(@NonNull Context context, ArrayList<Animal> list) {
        super(context, 0, list);
        mContext = context;
        animalList = list;
        session = new SessionHandler(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        final User user = session.getUserDetails();
        final int a = user.getUloga();
        Log.d("id", a+"");
        final String b=a+"";

        Button erase = (Button) listItem.findViewById(R.id.button3);
        Button edit = (Button) listItem.findViewById(R.id.button4);
        ImageView favourite = (ImageView) listItem.findViewById(R.id.imageView4);

        final Animal currentAnimal = animalList.get(position);
        ImageView image = (ImageView) listItem.findViewById(R.id.imageView_poster);
        String url = currentAnimal.getUrl();
        if (url != "") {
            Picasso.get().load(url).into(image);
        }

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentAnimal.getName());

        TextView breed = (TextView) listItem.findViewById(R.id.textView_breed);
        breed.setText(currentAnimal.getBreed());

        TextView shelter = (TextView) listItem.findViewById(R.id.textView_shelter);
        shelter.setText(currentAnimal.getShelter());

        if (a == 0) {
            erase.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else if(a == 1){
            favourite.setVisibility(View.GONE);

        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAnimal.class);
                Bundle b = new Bundle();
                b.putInt("animalId", currentAnimal.getIdAnimal()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                mContext.startActivity(intent);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String URL = mContext.getString(R.string.localhost_url).concat("/adFavourite.php?");

                v.setBackgroundColor(R.color.blueMain);
                RequestQueue queue = Volley.newRequestQueue(mContext);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("resso", response);

                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        Toast.makeText(mContext, "Dodano u favorite", Toast.LENGTH_SHORT).show();
                                    }

                                    // Once we added the string to the array, we notify the arrayAdapter
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("idUser", user.getId()+"");
                        params.put("idAnimal", currentAnimal.getIdAnimal()+"");
                        Log.d("params", params.toString());
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });


        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext){}
                        .setTitle("Update picture")
                        .setMessage("Obrisat čete životinju")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                obrisi(currentAnimal);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return listItem;
    }
    public void obrisi(final Animal currentAnimal){
        String URL = mContext.getString(R.string.localhost_url).concat("/deleteAnimal.php?");
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("resso", response);

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                Toast.makeText(mContext, "Životinja je obrisana", Toast.LENGTH_SHORT);
                            }

                            // Once we added the string to the array, we notify the arrayAdapter
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idAnimal", currentAnimal.getIdAnimal() + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}