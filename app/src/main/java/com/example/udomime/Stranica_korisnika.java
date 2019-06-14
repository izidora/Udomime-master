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
import android.widget.TextView;

public class Stranica_korisnika extends BaseActivity {
    Button izmjeni;

    private TextView etIzImena;
    private TextView etIzEmail;
    private TextView etIzGrad;
    private TextView etIzZupanija;
    private TextView etIzUsername;
    private String username;

    private String ime;
    private String email;
    private String grad;
    private String zupanija;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_stranica_korisnika, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
        //navigationView.setCheckedItem(R.id.nav_activity1);

        //incijaliziraj edite textove
        etIzImena=findViewById(R.id.etPrikazImena);
        etIzEmail=findViewById(R.id.etPrikazEmail);
        etIzGrad=findViewById(R.id.etPrikazGrad);
        etIzZupanija=findViewById(R.id.etPrikazZupanija);
        etIzUsername=findViewById(R.id.etPrikazUsername);
        //
        session = new SessionHandler(getApplicationContext());
        if(session.isLoggedIn()) {
            final User user = session.getUserDetails();
            etIzImena.setText(user.getFullName());
            etIzEmail.setText(user.getEmail());
            etIzGrad.setText(user.getGrad());
            etIzZupanija.setText(user.getZupanija());
            etIzUsername.setText(user.getUsername());
        }else {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        izmjeni=findViewById(R.id.button_korisnik_prikaz);
        izmjeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Izmjena_Stranica_Korisnika.class);
                startActivity(intent);
            }
        });
    }



}
