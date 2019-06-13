package com.example.udomime;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditShelter extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText adress;
    private TextInputEditText giroAccount;
    private TextInputEditText phone;
    private TextInputEditText mail;
    private TextInputEditText city;
    private TextInputEditText zupanija;
    private Button save;
    private Button editAnimals;
    private ImageView imageView;
    private final int GALLERY = 1;
    private String upload_URL;
    private String upload_URL2;
    private String upload_URL3;
    private RequestQueue rQueue;
    SessionHandler session;
    String id;

    private ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shelter);

        upload_URL = getString(R.string.localhost_url).concat("/uploadfileshelter.php?");
        upload_URL2 = getString(R.string.localhost_url).concat("/uploadinfoshelter.php?");
        upload_URL3 = getString(R.string.localhost_url).concat("/fetchshelterprofile.php?");

        requestMultiplePermissions();

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("shelterId");
        id = ""+value;
        final int val = value;

        imageView = (ImageView) findViewById(R.id.editShelterPic);
        name = (TextInputEditText) findViewById((R.id.editNameShelter));
        adress = (TextInputEditText) findViewById((R.id.editAdressShelter));
        giroAccount = (TextInputEditText) findViewById((R.id.editGiroShelter));
        phone = (TextInputEditText) findViewById(R.id. editPhoneShelter);
        mail = (TextInputEditText) findViewById(R.id.editEmailShelter);
        city = (TextInputEditText) findViewById(R.id.editCityShelter);
        zupanija = (TextInputEditText) findViewById(R.id.ediZupanijaShelter);
        save = (Button) findViewById(R.id.saveShelter);
        editAnimals = (Button) findViewById(R.id.editAnimalsShelter);

        if(value!=-1) {
            fetchInfo(id);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditShelter.this)
                        .setTitle("Update picture")
                        .setMessage("Promijenit ćete sliku skloništa")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, GALLERY);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo();
            }
        });

        editAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session.loginUser("izi","Izidora",1, 1,"idaso","Pula","ksoods");
                Intent intent = new Intent(EditShelter.this, DisplayAnimals.class);
                Bundle b = new Bundle();
                b.putInt("shelterId", val); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(getApplicationContext(), v);
                }
            }
        });

    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // imageView.setImageBitmap(bitmap);
                    uploadImage(bitmap, id);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditShelter.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImage(final Bitmap bitmap, String id){

        final String id1 = id;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override

                    public void onResponse(NetworkResponse response) {
                        Log.d("ressssssoo",new String(response.data));

                        rQueue.getCache().clear();
                        try {
                            String json = new String(response.data);
                            JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                            //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            jsonObject.toString().replace("\\\\","");

                            if (jsonObject.getString("status").equals("true")) {
                                arraylist = new ArrayList<HashMap<String, String>>();
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                String url = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    url = dataobj.optString("url_slika_skloniste");
                                    System.out.println(url);
                                }
                                Picasso.get().load(getString(R.string.localhost_url).concat(url)).into(imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params2 = new HashMap<>();
                params2.put("idShelter", id1);
                return params2;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("filename", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(EditShelter.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void uploadInfo(){

        final String nameShelter = name.getText().toString().trim();
        final String adressShelter = adress.getText().toString().trim();
        final String phoneShelter = phone.getText().toString().trim();
        final String email = mail.getText().toString().trim();
        final String cityShelter = city.getText().toString().trim();
        final String zupanijaShelter = zupanija.getText().toString().trim();
        final String ziroShelter = giroAccount.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(EditShelter.this,response,Toast.LENGTH_LONG).show();
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditShelter.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idShelter",id);
                params.put("nameShelter",nameShelter);
                params.put("adressShelter",adressShelter);
                params.put("phoneShelter",phoneShelter);
                params.put("mailShelter",email);
                params.put("giroShelter",ziroShelter);
                params.put("cityShelter",cityShelter);
                params.put("zupanijaShelter",zupanijaShelter);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Toast.makeText(EditShelter.this,"Podaci su izmijenjeni", Toast.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fetchInfo(final String value){

        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.toString().replace("\\\\","");
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                String ime = "";
                                String ziro = "";
                                String adresa = "";
                                String grad="";
                                String zupanija1="";
                                String telefon="";
                                String emailAdresa = "";
                                String url="";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    ime = dataobj.optString("ime_skloniste");
                                    ziro = dataobj.optString("ziro_racun");
                                    adresa = dataobj.optString("adresa");
                                    grad = dataobj.optString("grad");
                                    zupanija1 = dataobj.optString("zupanija");
                                    telefon = dataobj.optString("broj_telefona");
                                    emailAdresa = dataobj.optString("email");
                                    if(dataobj.has("url_slika_skloniste")) {
                                        url = dataobj.optString("url_slika_skloniste");
                                    }
                                }

                                name.setText(ime);
                                adress.setText(adresa);
                                giroAccount.setText(ziro);
                                city.setText(grad);
                                zupanija.setText(zupanija1);
                                phone.setText(telefon);
                                mail.setText(emailAdresa);
                                if(!url.equals("")) {
                                    Picasso.get().load(getString(R.string.localhost_url).concat(url)).into(imageView);
                                }
                            }

                            // Once we added the string to the array, we notify the arrayAdapter
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditShelter.this,error.toString(),Toast.LENGTH_LONG).show();
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

}
