package com.uiresource.messenger;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView chats;
    TextView logout;
    TextView nomeUtente;
    NavigationView navigationView, navigationViewBottom;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1024);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1024);


        }

        //Avvio la schermata della chat con il bot
        startActivity(new Intent(getApplicationContext(), Chat.class));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
        navigationViewBottom.setNavigationItemSelectedListener(this);

        nomeUtente = (TextView) findViewById(R.id.txtNomeUtente);//Nome utente nel menu

        chats =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_chats));

        logout = (TextView) MenuItemCompat.getActionView(navigationViewBottom.getMenu().
                findItem(R.id.nav_logout));

        initializeCountDrawer();

    }

    private void initializeCountDrawer(){
        chats.setGravity(Gravity.CENTER);
        chats.setTypeface(null, Typeface.BOLD);
        chats.setTextColor(getResources().getColor(R.color.colorAccent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            chats.setTextAppearance(R.style.LightNav);
            chats.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        chats.setText("99+");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chats) {
            Intent intent = new Intent(MainActivity.this,Chat.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            //Elimino le credenziali dalle shared preferences
            PreferenceData.setUserLoggedInStatus(this,false);   // Imposto il login status

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }




}
