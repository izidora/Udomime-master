package com.example.udomime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimalProfile extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private TextView size;
    private TextView shelter;
    private TextView breed;
    private TextView description;
    private ImageView imageView;
    private TextView lastChange;
    private TextView sex;
    private Button edit;

    ViewPager viewPager;
    CustomAdapter adapter;
    private ArrayList<String> imageModelArrayList;

    private String upload_URL2;

    private RequestQueue rQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_profile);

        upload_URL2 = getString(R.string.localhost_url).concat("/fetchanimalprofile.php?");

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("animalId");
        final String idAnimal;
        idAnimal = ""+value;
        final int val = value;


        imageModelArrayList = new ArrayList<>();
        edit = (Button) findViewById(R.id.editAnimal);
        imageView = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById((R.id.nameInput));
        breed = (TextView) findViewById((R.id.breedInput));
        description = (TextView) findViewById((R.id.descriptionInput));
        age = (TextView) findViewById((R.id.input_age));
        size = (TextView) findViewById((R.id.input_size));
        shelter = (TextView) findViewById((R.id.shelterInput));
        lastChange = (TextView) findViewById(R.id.lastChange) ;
        sex =(TextView) findViewById(R.id.sexAnimalView);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new CustomAdapter(this, imageModelArrayList);
        viewPager.setAdapter(adapter);


        uploadInfo(idAnimal);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AnimalProfile.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("animalId", val); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

    }

    private void uploadInfo(final String value){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AnimalProfile.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(AnimalProfile.this,response,Toast.LENGTH_LONG).show();
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AnimalProfile.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", value);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

            try {

                JSONObject jsonObject = new JSONObject(response);
                jsonObject.toString().replace("\\\\","");
                if (jsonObject.getString("status").equals("true")) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    String ime = "";
                    String opis = "";
                    String pasmin = "";
                    String skloniste="";
                    String starost="";
                    String tezina="";
                    String datumIzmjene="";
                    String spol="";
                    JSONObject dataobj = dataArray.getJSONObject(0);
                    ime = dataobj.optString("ime_zivotinja");
                    pasmin = dataobj.optString("pasmina");
                    opis = dataobj.optString("opis_zivotinja");
                    skloniste = dataobj.optString("id_skloniste");
                    starost = dataobj.optString("starost");
                    tezina = dataobj.optString("velicina");
                    datumIzmjene = dataobj.optString("datum_izmjene");
                    spol = dataobj.optString("spol_zivotinja");
                    for (int i = 1; i < dataArray.length(); i++) {
                        JSONObject dataobj1 = dataArray.getJSONObject(i);
                        if(dataobj1.has("url_slike")) {
                            String url = dataobj1.optString("url_slike");
                            url = getString(R.string.localhost_url).concat(url);
                            imageModelArrayList.add(url);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    name.setText(ime);
                    breed.setText(pasmin);
                    description.setText(opis);
                    age.setText(starost);
                    size.setText(tezina);
                    lastChange.setText(datumIzmjene);
                    if(spol.equals("0")) {
                        sex.setText("(ženka)");
                    }
                    else {
                        sex.setText("(mužjak)");
                    }
                }

                // Once we added the string to the array, we notify the arrayAdapter
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
}
