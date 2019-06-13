package com.example.udomime;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterSkloniste extends BaseActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_IME = "ime";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GRAD= "grad";
    private static final String KEY_ZUPANIJA = "zupanija";
    private static final String KEY_BROJ_TELEFONA="broj_telefona";
    private static final String KEY_USERNAME="username";
    private static final String KEY_ADRESA="adresa";
    private static final String KEY_ID="id";
    private static final String KEY_ULOGA="uloga";
    private static final String KEY_EMPTY = "";

    private EditText etImeSklonista;
    private EditText etUsernameSkloniste;
    private EditText etPasswordSkloniste;
    private EditText etConfirmPasswordSkloniste;
    private EditText etZupanijaSkloniste;
    private EditText etGradSkloniste;
    private EditText etEmailSkloniste;
    private EditText etBrojTelefona;
    private EditText etAdresaSkloniste;

    private String usernameS;
    private String imeS;
    private String passwordS;
    private String confirmPasswordS;
    private String zupanijaS;
    private String gradS;
    private String emailS;
    private String broj_telefonaS;
    private String adresaS;

    private ProgressDialog pDialog;
    private String register_url;
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        register_url = getString(R.string.localhost_url).concat("/register_skloniste.php");

        //setContentView(R.layout.activity_register_skloniste);
        //novo dodano
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_register_skloniste, null, false);
        drawer.addView(contentView, 0);
        //novo
        navigationView.setCheckedItem(R.id.nav_activity3);


        etImeSklonista = (EditText)findViewById(R.id.etImeSklonista);
        etUsernameSkloniste= (EditText)findViewById(R.id.etUsernameSkloniste);
        etPasswordSkloniste = findViewById(R.id.etPasswordSkloniste);
        etConfirmPasswordSkloniste = findViewById(R.id.etConfirmPasswordSkloniste);
        etBrojTelefona = findViewById(R.id.etBrojTelefona);
        etEmailSkloniste=findViewById(R.id.etEmailSkloniste);
        etGradSkloniste=findViewById(R.id.etGradSkloniste);
        etZupanijaSkloniste=findViewById(R.id.etZupanijaSkloniste);
        etAdresaSkloniste=findViewById(R.id.etAdresaSkloniste);

        session = new SessionHandler(getApplicationContext());

        Button registerSkl = findViewById(R.id.btnRegisterskloniste);

        registerSkl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                usernameS = etUsernameSkloniste.getText().toString().toLowerCase().trim();
                passwordS = etPasswordSkloniste.getText().toString().trim();
                confirmPasswordS = etConfirmPasswordSkloniste.getText().toString().trim();
                broj_telefonaS = etBrojTelefona.getText().toString().trim();
                gradS=etGradSkloniste.getText().toString().trim();
                zupanijaS=etZupanijaSkloniste.getText().toString().trim();
                emailS=etEmailSkloniste.getText().toString().trim();
                imeS=etImeSklonista.getText().toString().trim();
                adresaS=etAdresaSkloniste.getText().toString().trim();

                if (validateInputs()) {
                    registerUser();
                }

            }
        });

    }
    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterSkloniste.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
/*    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();

    }*/

    private void registerUser() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, usernameS);
            request.put(KEY_EMAIL,emailS);
            request.put(KEY_PASSWORD, passwordS);
            request.put(KEY_GRAD,gradS);
            request.put(KEY_ZUPANIJA,zupanijaS);
            request.put(KEY_BROJ_TELEFONA,broj_telefonaS);
            request.put(KEY_IME,imeS);
            request.put(KEY_ADRESA,adresaS);
            //Log.d("tagconvertstr", "[" + request + "]");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //System.out.println("********" + request.toString());


        Log.d("tagconvertstr", "[" + request + "]");
        //Log.i("tagconvertstr", "["+request+"]");
        final JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        //System.out.println("BOK"+response.toString());
                        try {

                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                //System.out.println("Evo Korisnika"+username);
                               // System.out.println("Evo Imena"+fullName);

                                session.loginUser(usernameS,imeS,response.getInt(KEY_ID),response.getInt(KEY_ULOGA),emailS,gradS,zupanijaS);
                                //Log.d("******","[" + response + "]");
                                Toast.makeText(getApplicationContext(),
                                        "Uspjeli smo", Toast.LENGTH_SHORT).show();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                etUsernameSkloniste.setError("Username already taken!");
                                etUsernameSkloniste.requestFocus();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        System.out.println("Hej"+error.toString());
                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(imeS)) {
            etImeSklonista.setError("Ime i prezime ne može biti prazno");
            etImeSklonista.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(gradS)) {
            etGradSkloniste.setError("Grad ne može biti prazan");
            etGradSkloniste.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(zupanijaS)) {
            etZupanijaSkloniste.setError("Županija ne može biti prazna");
            etZupanijaSkloniste.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(emailS)) {
            etEmailSkloniste.setError("Email ne može biti prazan");
            etEmailSkloniste.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(usernameS)) {
            etUsernameSkloniste.setError("Ime skloništa ne može biti prazno");
            etUsernameSkloniste.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(passwordS)) {
            etPasswordSkloniste.setError("Lozinka ne može biti prazna");
            etPasswordSkloniste.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPasswordS)) {
            etConfirmPasswordSkloniste.setError("Potvrda lozinke ne može biti prazna");
            etConfirmPasswordSkloniste.requestFocus();
            return false;
        }
        if (!passwordS.equals(confirmPasswordS)) {
            etConfirmPasswordSkloniste.setError("Lozinka i potvrda lozinke nisu isti");
            etConfirmPasswordSkloniste.requestFocus();
            return false;
        }

        return true;
    }


}
