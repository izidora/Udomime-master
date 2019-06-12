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

public class Register extends BaseActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GRAD= "grad";
    private static final String KEY_ZUPANIJA = "zupanija";
    private static final String KEY_ID="id";
    private static final String KEY_ULOGA="uloga";
    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etFullName;
    private EditText etZupanija;
    private EditText etGrad;
    private EditText etEmail;

    private String username;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String zupanija;
    private String grad;
    private String email;
    private ProgressDialog pDialog;
    private String register_url = "http://192.168.1.4/mobilne/register.php";
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);

        //novo dodano
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_register, null, false);
        drawer.addView(contentView, 0);
        //novo
        navigationView.setCheckedItem(R.id.nav_activity2);


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        etEmail=findViewById(R.id.etEmail);
        etGrad=findViewById(R.id.etGrad);
        etZupanija=findViewById(R.id.etZupanija);

        //debug=findViewById(R.id.parse);

        Button register = findViewById(R.id.btnRegister);

        //Launch Login screen when Login Button is clicked
        session = new SessionHandler(getApplicationContext());


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();
                fullName = etFullName.getText().toString().trim();
                grad=etGrad.getText().toString().trim();
                zupanija=etZupanija.getText().toString().trim();
                email=etEmail.getText().toString().trim();
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
        pDialog = new ProgressDialog(Register.this);
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
            request.put(KEY_USERNAME, username);
            request.put(KEY_FULL_NAME, fullName);
            request.put(KEY_EMAIL,email);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_GRAD,grad);
            request.put(KEY_ZUPANIJA,zupanija);
            Log.d("tagconvertstr", "[" + request + "]");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //System.out.println("********" + request.toString());



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
                                System.out.println("Evo Korisnika"+username);
                                System.out.println("Evo Imena"+fullName);

                                session.loginUser(username,fullName,response.getInt(KEY_ID),response.getInt(KEY_ULOGA),email,grad,zupanija);
                                //Log.d("******","[" + response + "]");
                                Toast.makeText(getApplicationContext(),
                                        "Uspjeli smo", Toast.LENGTH_SHORT).show();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                etUsername.setError("Username already taken!");
                                etUsername.requestFocus();
                                Log.d("******","[" + response + "]");

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
        if (KEY_EMPTY.equals(fullName)) {
            etFullName.setError("Ime i prezime ne može biti prazno");
            etFullName.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(grad)) {
            etFullName.setError("Grad ne može biti prazan");
            etFullName.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(zupanija)) {
            etFullName.setError("Županija ne može biti prazna");
            etFullName.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(email)) {
            etFullName.setError("Email ne može biti prazan");
            etFullName.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(username)) {
            etUsername.setError("Korisničko ime ne može biti prazno");
            etUsername.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Lozinka ne može biti prazna");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etConfirmPassword.setError("Potvrda lozinke ne može biti prazna");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Lozinka i potvrda lozinke nisu isti");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                super.onBackPressed();
            }
        }
    }
}
