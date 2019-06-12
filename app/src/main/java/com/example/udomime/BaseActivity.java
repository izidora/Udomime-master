package com.example.udomime;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.w3c.dom.Text;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    Button log_out;
    DrawerLayout drawer;
    //FloatingActionButton fab;
    NavigationView navigationView;
    SessionHandler session;
    TextView username_prikaz;
    MaterialSearchBar searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //search

        searchBar = findViewById(R.id.action_search);
        searchBar.setOnSearchActionListener(this);
        //searchBar.inflateMenu(R.menu.main2);
        //searchBar.setText("Ime životinje");
//        Button napredno=(Button)navigationView.getHeaderView(0).findViewById(R.id.advance_search);

        //search
/*        napredno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NapredniSearch.class);
                startActivity(intent);
            }
        });*/
        session = new SessionHandler(getApplicationContext());
        final User userr = session.getUserDetails();
        Button izmjena=(Button)navigationView.getHeaderView(0).findViewById(R.id.Izmjenibuttonkorisnika);
        username_prikaz = (TextView) navigationView.getHeaderView(0).findViewById(R.id.prikaz_username);

        if(session.isLoggedIn()) {
            log_out=(Button)navigationView.getHeaderView(0).findViewById(R.id.Odjavabutton);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),
                        "Welcome "+userr.getFullName()+", your session will expire on "+userr.getSessionExpiryDate()+"Tvoj id:"+userr.getId()+",a tvoja uloga je:"+userr.getUloga(), Toast.LENGTH_SHORT).show();
*/
                session.logoutUser();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }


        });

        izmjena.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Izmjena_Stranica_Korisnika.class);
                startActivity(intent);
            }
        });

            username_prikaz.setText(userr.getUsername());
        }else{
            log_out=(Button)navigationView.getHeaderView(0).findViewById(R.id.Odjavabutton);
            log_out.setVisibility(View.GONE);
            izmjena.setVisibility(View.GONE);
            username_prikaz.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_activity1) {
            startAnimatedActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_activity2) {
            startAnimatedActivity(new Intent(getApplicationContext(), Register.class));
        }else if (id==R.id.nav_activity3){
            startAnimatedActivity(new Intent(getApplicationContext(), RegisterSkloniste.class));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Intent intent = new Intent(getApplicationContext(),SearchIspis.class);
        Bundle b=new Bundle();
        b.putString("Vrijednost",text.toString());
        intent.putExtras(b);
        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();

        //intent.putExtra("Vrijednost",text);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }
    }
}