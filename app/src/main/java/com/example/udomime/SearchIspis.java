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
import android.widget.TextView;

public class SearchIspis extends BaseActivity {
    TextView ispis;
    //String hej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search_ispis);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_search_ispis, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
        //navigationView.setCheckedItem(R.id.nav_activity1);

        Bundle k= getIntent().getExtras();
        String value="";
        if (k!=null){
            value=k.getString("Vrijednost");
        }
        //Intent intent = getIntent();
        //String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        ispis=findViewById(R.id.textView2);
        ispis.setText(value);
        //ispis.setText(hej);

    }



}
