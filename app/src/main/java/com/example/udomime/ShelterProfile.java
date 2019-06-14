package com.example.udomime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShelterProfile extends BaseActivity {

    private TextView name;
    private TextView adress;
    private TextView giro;
    private ImageView imageView;
    private TextView phone;
    private TextView email;
    private TextView city;
    private Button edit;
    private ListView listView;
    private ArrayList<Animal> values;
    private SessionHandler session;

    private String upload_URL2;
    private String upload_URL3;
    private RequestQueue rQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        upload_URL2 = getString(R.string.localhost_url).concat("/fetchshelterprofile.php?");
        upload_URL3 = getString(R.string.localhost_url).concat("/fetchanimals.php?");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_shelter_profile);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_shelter_profile, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
        navigationView.setCheckedItem(R.id.nav_activity1);



        System.out.println(upload_URL2);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("shelterId");
        final String idShelter;
        idShelter = ""+value;
        final int val = value;//value;

        edit = (Button) findViewById(R.id.editShelter);
        imageView = (ImageView) findViewById(R.id.picShelter);
        name = (TextView) findViewById((R.id.nameShelter));
        giro = (TextView) findViewById((R.id.giroShelter));
        adress = (TextView) findViewById((R.id.adressShelter));
        phone = (TextView) findViewById(R.id.phoneShelter) ;
        email = (TextView) findViewById(R.id.emailShelter) ;
        city =(TextView) findViewById(R.id.cityShelterView);
        ViewPager viewPager;
        CustomAdapter2 adapter;
        values = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        if(user.getUloga()==0) {
            edit.setVisibility(View.GONE);
        }

        viewPager = (ViewPager)findViewById(R.id.view_pager2);

        adapter = new CustomAdapter2(this, values);
        sendRequest(adapter, idShelter);
        viewPager.setAdapter(adapter);

        uploadInfo(idShelter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShelterProfile.this, EditShelter.class);
                Bundle b = new Bundle();
                b.putInt("shelterId", val); //Your id
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
                        //Toast.makeText(AnimalProfile.this,response,Toast.LENGTH_LONG).show();
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShelterProfile.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idShelter", value);

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
                String giro2="";
                String adresa="";
                String email2= "";
                String brTelefona="";
                String grad="";
                String url = "";
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    ime = dataobj.optString("ime_skloniste");
                    giro2 = dataobj.optString("ziro_racun");
                    adresa = dataobj.optString("adresa");
                    brTelefona = dataobj.optString("broj_telefona");
                    grad = dataobj.optString("grad");
                    if(dataobj.has("url_slika_skloniste")) {
                        url = dataobj.optString("url_slika_skloniste");
                    }
                }

                name.setText(ime);
                giro.setText(giro2);
                adress.setText(adresa);
                phone.setText(brTelefona);
                email.setText(email2);
                city.setText(grad);
                if(url!="") {
                    Picasso.get().load(getString(R.string.localhost_url).concat(url)).into(imageView);
                }
            }

            // Once we added the string to the array, we notify the arrayAdapter
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(final CustomAdapter2 adapter, final String id) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(ShelterProfile.this,response,Toast.LENGTH_LONG).show();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                String ime = "";
                                int id;
                                String pasmina = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    String url ="";
                                    String skloniste ="";
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    ime = dataobj.optString("ime_zivotinja");
                                    id = dataobj.optInt("id_zivotinja");
                                    pasmina = dataobj.optString("pasmina");
                                    skloniste = dataobj.optString("ime_skloniste");
                                    if(dataobj.has("url_slike")) {
                                        url = dataobj.optString("url_slike");
                                        url = getString(R.string.localhost_url).concat(url);
                                        values.add(new Animal(id, ime, pasmina, url, skloniste));
                                    }
                                }
                                Toast.makeText(ShelterProfile.this, values.get(0).getUrl(), Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
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
                params.put("idShelter", id);

                return params;
            }
        };
        queue.add(stringRequest);
    }

}
