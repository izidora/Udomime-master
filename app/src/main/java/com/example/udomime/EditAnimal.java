package com.example.udomime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

public class EditAnimal extends BaseActivity {

    private TextInputEditText name;
    private AutoCompleteTextView breed;
    private TextInputEditText description;
    private TextInputEditText size;
    private TextInputEditText age;
    private Button save;
    private RadioGroup radioKindGroup;
    private RadioGroup radioSexGroup;
    private ImageView imageView;
    private final int GALLERY = 1;

    private String upload_URL;
    private String upload_URL2;
    private String upload_URL3;


    //private String upload_URL = "http://192.168.5.15/projekt/uploadfile.php?";
    //private String upload_URL2 = "http://192.168.5.15/projekt/uploadinfo.php?";
    //private String upload_URL3 = "http://192.168.5.15/projekt/fetchanimalprofile.php?";
    private RequestQueue rQueue;
    String id;
    String idShelter;
    String getUrl="";

    private ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_animal);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_view_animal, null, false);
        drawer.addView(contentView, 0);
        //novo
        //prikazuje navigation bar
       // navigationView.setCheckedItem(R.id.nav_activity1);

        upload_URL = getString(R.string.localhost_url).concat("/uploadfile.php?");
        upload_URL2 = getString(R.string.localhost_url).concat("/uploadinfo.php?");
        upload_URL3 = getString(R.string.localhost_url).concat("/fetchanimalprofile.php?");

        requestMultiplePermissions();

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        int valueShelter = -1;
        if(b != null) {
            value = b.getInt("animalId");
            valueShelter = b.getInt("shelterId");
        }
        id = ""+value;
        id="-1";
        idShelter = ""+valueShelter;

        imageView = (ImageView) findViewById(R.id.iv);
        name = (TextInputEditText) findViewById((R.id.nameInput));
        breed = (AutoCompleteTextView) findViewById((R.id.breedInput));
        description = (TextInputEditText) findViewById((R.id.descriptionInput));
        size = (TextInputEditText) findViewById(R.id.input_size);
        age = (TextInputEditText) findViewById(R.id.input_age);
        save = (Button) findViewById(R.id.saveAnimal);
        radioKindGroup = (RadioGroup) findViewById(R.id.radioKind);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        if(value!=-1) {
            fetchInfo(id);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo();
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
                    Toast.makeText(EditAnimal.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImage(final Bitmap bitmap, String id){

        final String id1 = id;
        if (id.equals("-1")) {
            upload_URL=getString(R.string.localhost_url).concat("/uploadfilenew.php?");
        }
        else {
            upload_URL=getString(R.string.localhost_url).concat("/uploadfile.php?");
        }
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override

                    public void onResponse(NetworkResponse response) {
                        Log.d("ressssssoo",new String(response.data));

                        rQueue.getCache().clear();
                        try {
                            String json = new String(response.data);
                            Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                            //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            jsonObject.toString().replace("\\\\","");

                            if (jsonObject.getString("status").equals("true")) {
                                arraylist = new ArrayList<HashMap<String, String>>();
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                String url = "";
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    url = dataobj.optString("url_slike");
                                    System.out.println(url);
                                }
                                if(url!="") {
                                    Picasso.get().load(getString(R.string.localhost_url).concat(url)).into(imageView);
                                    getUrl = url;
                                }
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
                params2.put("idAnimal", id1);
                Log.d("HEJ",params2.toString());
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
                Log.d("SLIKAAA",params.toString());
                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(EditAnimal.this);
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
        if (id.equals("-1")) {
            upload_URL2=getString(R.string.localhost_url).concat("/uploadinfonew.php?");
        }

        final String nameAnimal = name.getText().toString().trim();
        final String breedAnimal = breed.getText().toString().trim();
        final String ageAnimal = age.getText().toString().trim();
        final String sizeAnimal = size.getText().toString().trim();
        final String descriptionAnimal = description.getText().toString().trim();

        int selectedSex = radioSexGroup.getCheckedRadioButtonId();
        int selectedKind = radioKindGroup.getCheckedRadioButtonId();
        String sex="1";
        String kind="1";

        if(selectedKind==R.id.radioCat) {
            kind ="0";
        }

        if(selectedSex==R.id.radioFemale) {
            sex ="0";
        }
        final String sexAnimal = sex;
        final String kindAnimal = kind;

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ressssssoo",response);
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditAnimal.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("urlAnimal", getUrl);
                params.put("idAnimal",id);
                params.put("nameAnimal",nameAnimal);
                params.put("breedAnimal",breedAnimal);
                params.put("ageAnimal",ageAnimal);
                params.put("sizeAnimal",sizeAnimal);
                params.put("descriptionAnimal",descriptionAnimal);
                params.put("kind",kindAnimal);
                params.put("sex",sexAnimal);
                params.put("idShelter", idShelter);
                Log.d("fjlsky", "["+params+"]");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

        try {
            Log.d("ressssssoo",response);
            JSONObject jsonObject = new JSONObject(response);
            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            Toast.makeText(EditAnimal.this,response,Toast.LENGTH_LONG).show();
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fetchInfo(final String value){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, upload_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Toast.makeText(EditAnimal.this, response, Toast.LENGTH_LONG).show();
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
                                String url = "";
                                String vrsta1 = "";
                                String spol1 = "";
                                //for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(0);
                                    ime = dataobj.optString("ime_zivotinja");
                                    pasmin = dataobj.optString("pasmina");
                                    opis = dataobj.optString("opis_zivotinja");
                                    skloniste = dataobj.optString("id_skloniste");
                                    starost = dataobj.optString("starost");
                                    tezina = dataobj.optString("velicina");
                                    vrsta1 = dataobj.optString("vrsta");
                                    spol1 = dataobj.optString("spol_zivotinja");

                                    JSONObject dataobj1 = dataArray.getJSONObject(1);
                                    if(dataobj1.has("url_slike")) {
                                        url = dataobj1.optString("url_slike");
                                    }
                                Toast.makeText(EditAnimal.this, url, Toast.LENGTH_LONG).show();

                                name.setText(ime);
                                breed.setText(pasmin);
                                description.setText(opis);
                                age.setText(starost);
                                size.setText(tezina);
                                if(vrsta1=="0"){
                                    radioKindGroup.check(R.id.radioCat);
                                }
                                if(spol1=="0"){
                                    radioKindGroup.check(R.id.radioFemale);
                                }
                                //lastChange.setText(datumIzmjene);
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
                        Toast.makeText(EditAnimal.this,error.toString(),Toast.LENGTH_LONG).show();
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

}
