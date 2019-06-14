package com.example.udomime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class DisplayAnimals extends BaseActivity {

    ListView listView;
    private String upload_URL2;
    ArrayList <Animal> values;
    Button addAnimal;
    SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_animals);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_display_animals, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
       // navigationView.setCheckedItem(R.id.nav_activity1);

        upload_URL2 = getString(R.string.localhost_url).concat("/fetchanimals.php?");
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("shelterId");
        final String id = ""+value;
        final int val = value;


        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        addAnimal = (Button) findViewById(R.id.button5);


        if(value==-1) {
            addAnimal.setVisibility(View.GONE);
        }
        else {
            addAnimal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DisplayAnimals.this, EditAnimal.class);
                    Bundle b = new Bundle();
                    b.putInt("shelterId", val); //Your id
                    b.putInt("animalId", -1);
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    finish();
                }
            });
        }

        // Defined Array values to show in ListView
        values = new ArrayList<Animal>();

        final AnimalAdapter adapter = new AnimalAdapter(this, values);




        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Animal itemValue = (Animal) listView.getItemAtPosition(itemPosition);

                Intent intent = new Intent(DisplayAnimals.this, AnimalProfile.class);
                Bundle b = new Bundle();
                b.putInt("animalId", itemValue.getIdAnimal()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();

            }

        });

        sendRequest(adapter, id);
    }

    private void sendRequest(final AnimalAdapter adapter, final String id) {
        //Toast.makeText(DisplayAnimals.this,id,Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(DisplayAnimals.this,response,Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                String ime = "";
                                int id;
                                String pasmina = "";
                                String skloniste = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    String url ="";
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    ime = dataobj.optString("ime_zivotinja");
                                    id = dataobj.optInt("id_zivotinja");
                                    pasmina = dataobj.optString("pasmina");
                                    skloniste = dataobj.optString("ime_skloniste");
                                    if(dataobj.has("url_slike")) {
                                        url = dataobj.optString("url_slike");
                                        url = getString(R.string.localhost_url).concat(url);
                                    }
                                    values.add(new Animal(id, ime, pasmina, url, skloniste));
                                }
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
