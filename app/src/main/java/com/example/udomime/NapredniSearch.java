
package com.example.udomime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class NapredniSearch extends BaseActivity {

    EditText name;
    EditText age;
    EditText county;
    EditText size;
    RadioGroup sex;
    RadioGroup kind;
    RadioButton male;
    RadioButton female;
    RadioButton cat;
    RadioButton dog;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_napredni_search);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_napredni_search, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
        //navigationView.setCheckedItem(R.id.nav_activity1);


        name = findViewById(R.id.TraziIme);
        age= findViewById(R.id.godine);
        county=findViewById(R.id.zupanija);
        size=findViewById(R.id.tezina);
        sex=findViewById(R.id.spol);
        kind=findViewById(R.id.vrsta);
        male=findViewById(R.id.musko);
        female=findViewById(R.id.zensko);
        cat=findViewById(R.id.macka);
        dog=findViewById(R.id.pas);
        search=findViewById(R.id.pretrazi);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ime = name.getText().toString();
                String starost = age.getText().toString();
                String zupanija = county.getText().toString();
                String velicina = size.getText().toString();
                int selectedSex = sex.getCheckedRadioButtonId();
                int selectedKind = kind.getCheckedRadioButtonId();
                String spol="1";
                String vrsta="1";

                if(selectedKind==R.id.macka) {
                    vrsta ="0";
                }

                if(selectedSex==R.id.zensko) {
                    spol ="0";
                }

                if((isNumeric(starost)&&isNumeric(velicina))|| (starost.equals("")&&velicina.equals("")) || (starost.equals("")&&isNumeric(velicina)) || (isNumeric(starost)&&velicina.equals(""))) {
               /*     Intent intent = new Intent(NapredniSearch.this, DisplayAnimals2.class);
                    Bundle b = new Bundle();
                    b.putString("name",ime);
                    b.putString("address",zupanija);
                    b.putString("age",starost);
                    b.putString("size",velicina);
                    b.putString("sex", spol);
                    b.putString("kind", vrsta);
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    finish();*/
                }
                else {
                    Toast.makeText(NapredniSearch.this, "Starost i veličina moraju biti numeričke vrijednosti", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
