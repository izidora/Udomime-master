package com.example.udomime;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Izmjena_Stranica_Korisnika extends BaseActivity {
    Button izmjeni;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_GRAD = "grad";
    private static final String KEY_ZUPANIJA = "zupanija";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EMPTY = "";


    private ProgressDialog pDialog;

    private EditText etIzImena;
    private EditText etIzEmail;
    private EditText etIzGrad;
    private EditText etIzZupanija;
    private String ime;
    private String email;
    private String grad;
    private String zupanija;
    private String izmjeni_url = getString(R.string.localhost_url).concat("/izmjeni_korisnika.php");
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_izmjena__stranica__korisnika, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
        navigationView.setCheckedItem(R.id.nav_activity1);

        //incijaliziraj edite textove
        etIzImena=findViewById(R.id.etIzmjenaPrikazImena);
        etIzEmail=findViewById(R.id.etIzmjenaPrikazEmail);
        etIzGrad=findViewById(R.id.etIzmjenaPrikazGrad);
        etIzZupanija=findViewById(R.id.etIzmjenaPrikazZupanija);
        //

        izmjeni=findViewById(R.id.Spremi_button_korisnik_prikaz);
        izmjeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ime=etIzImena.getText().toString().trim();
                email=etIzEmail.getText().toString().trim();
                grad=etIzGrad.getText().toString().trim();
                zupanija=etIzZupanija.getText().toString().trim();
                if (validateInputs()) {
                    spremi();
                }

            }
        });
    }
    private void spremi() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters

            request.put(KEY_FULL_NAME,ime);
            request.put(KEY_EMAIL,email);
            request.put(KEY_GRAD,grad);
            request.put(KEY_ZUPANIJA,zupanija);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("********" + request.toString());

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, izmjeni_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            //System.out.println("Odgovorlogin" + response.toString());

                            if (response.getInt(KEY_STATUS) == 0) {
                                //session.loginUser();
                                //loadDashboard();
                                // Log.d("****","[" + response + "]");
                                //User user = session.getUserDetails();
                                //Log.d("Podaci:",session.getUserDetails());
                                //Toast.makeText(getApplicationContext(),
                                //        "Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate()+"Tvoj id:"+user.getId()+",a tvoja uloga je:"+user.getUloga(), Toast.LENGTH_SHORT).show();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);

                            }else{
                                //final String result = username.toString();
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

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(ime)) {
            etIzImena.setError("Ime i prezime ne može biti prazno");
            etIzImena.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(grad)) {
            etIzGrad.setError("Grad ne može biti prazan");
            etIzGrad.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(zupanija)) {
            etIzZupanija.setError("Županija ne može biti prazna");
            etIzZupanija.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(email)) {
            etIzEmail.setError("Email ne može biti prazan");
            etIzEmail.requestFocus();
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

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(Izmjena_Stranica_Korisnika.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }


}
