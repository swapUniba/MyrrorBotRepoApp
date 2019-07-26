package com.uiresource.messenger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
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
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.uiresource.messenger.recylcerchat.ChatData;
import com.uiresource.messenger.recylcerchat.ConversationRecyclerView;
import com.uiresource.messenger.recylcerchat.HolderNews;
import com.uiresource.messenger.recylcerchat.Meteo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class ChatGuest extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private ConversationRecyclerView mAdapter;
    private EditText text;
    private Button send;

    private TextView nomeUtente;
    private ImageView imageLogoUtente;

    public String email;

    private TextView txtContestoGuest;//per il cambia



    //MAPS
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private LatLng position;
    private String city;

    //SPOTIFY
    private static final String CLIENT_ID = "7a17bf0bdb234ef2870a58f22f26e8bd";
    private static final String REDIRECT_URI = "com.uiresource.messenger://callback"; //Application ID della app

    SpotifyAppRemote spotifyAppRemote = null;

    public SpotifyAppRemote getSpotifyAppRemote() {
        return spotifyAppRemote;
    }

    public static String accessToken = "";

    public void setSpotifyAppRemote(SpotifyAppRemote spotifyAppRemote) {
        this.spotifyAppRemote = spotifyAppRemote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_guest);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbarWithUpNav(R.id.toolbar, "MyrrorBot", R.drawable.ic_action_back);//Nome Toolbar

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(getIntent().hasExtra("email")){
            email = getIntent().getStringExtra("email");

        }

        //controllo permessi
        getDeviceLocation();

        txtContestoGuest = (TextView)findViewById(R.id.txtContestoGuest);
        txtContestoGuest.setText("");

        //Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationViewBottom = (NavigationView) findViewById(R.id.nav_view_bottom);
        navigationViewBottom.setNavigationItemSelectedListener(this);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationRecyclerView(this,setData());
        mRecyclerView.setAdapter(mAdapter);

        /*mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 1000);*/

        text = (EditText) findViewById(R.id.et_message);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        }catch (IllegalArgumentException e){
                            e.getMessage();
                        }
                    }
                }, 500);
            }
        });
        send = (Button) findViewById(R.id.bt_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.getText().equals("")) {
                    List<ChatData> data = new ArrayList<ChatData>();
                    ChatData item = new ChatData();
                    Date currentTime = Calendar.getInstance().getTime();
                    item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                    item.setType("2");//Imposto il layout della risposta, ovvero YOU
                    String mess = text.getText().toString(); //Domanda dell'utente
                    item.setText(mess);
                    data.add(item);
                    mAdapter.addItem(data);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    text.setText("");


                    //INGLESE
                    if (LoginActivity.lingua.equalsIgnoreCase("English (United States)")) {
                        if (mess.equalsIgnoreCase("change")|| mess.equalsIgnoreCase("change song") ||mess.equalsIgnoreCase("change track") ||
                                mess.equalsIgnoreCase("another") ||mess.equalsIgnoreCase("another song") ||mess.equalsIgnoreCase("give me another song") ||
                                mess.equalsIgnoreCase("give me") ||mess.equalsIgnoreCase("give me another") ||mess.equalsIgnoreCase("give me another track") ||
                                mess.equalsIgnoreCase("give me a track") ||mess.equalsIgnoreCase("give me a song") ||mess.equalsIgnoreCase("give me a news") ||
                                mess.equalsIgnoreCase("another news") ||mess.equalsIgnoreCase("another video") ||mess.equalsIgnoreCase("give me a video") ||
                                mess.equalsIgnoreCase("another track") ){

                            mess =  txtContestoGuest.getText().toString(); //Prendo l'ultimo domanda

                            if (mess != ""){
                                Log.i("DOMANDA RECUPERATA",mess);

                                Background b = new Background();
                                b.execute(mess);
                            }else {
                                data = new ArrayList<ChatData>();
                                item = new ChatData();
                                currentTime = Calendar.getInstance().getTime();
                                item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                                item.setType("1");

                                item.setText("The 'change' function is only available if a previous request has been selected!");
                                data.add(item);
                                mAdapter.addItem(data);
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                            }


                        }else{
                            txtContestoGuest.setText(mess);//Imposto la domanda
                            Log.i("DOMANDA DA SALVARE",txtContestoGuest.getText().toString());
                            Background b = new Background();
                            b.execute(mess);
                        }
                    }else {//ITALIANO
                        if (mess.equalsIgnoreCase("cambia")|| mess.equalsIgnoreCase("cambio") ||mess.equalsIgnoreCase("dammi un'altra") ||
                                mess.equalsIgnoreCase("leggi un'altra") ||mess.equalsIgnoreCase("dammene un'altra") ||mess.equalsIgnoreCase("leggine un'altra") ||
                                mess.equalsIgnoreCase("leggi altra news") ||mess.equalsIgnoreCase("altra canzone") ||mess.equalsIgnoreCase("dimmi un'altra") ||
                                mess.equalsIgnoreCase("riproducine un'altra") ||mess.equalsIgnoreCase("cambia video") ||mess.equalsIgnoreCase("fammi vedere un altro") ||
                                mess.equalsIgnoreCase("dammi un altro") ||mess.equalsIgnoreCase("altro video") ||mess.equalsIgnoreCase("altra canzone") ||
                                mess.equalsIgnoreCase("altra news") ){

                            mess =  txtContestoGuest.getText().toString(); //Prendo l'ultimo domanda

                            if (mess != ""){
                                Log.i("DOMANDA RECUPERATA",mess);

                                Background b = new Background();
                                b.execute(mess);
                            }else {
                                data = new ArrayList<ChatData>();
                                item = new ChatData();
                                currentTime = Calendar.getInstance().getTime();
                                item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                                item.setType("1");

                                item.setText("La funzione 'cambia' è disponibile solo se è stata effettuata una richiesta precedente!");
                                data.add(item);
                                mAdapter.addItem(data);
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                            }


                        }else{
                            txtContestoGuest.setText(mess);//Imposto la domanda
                            Log.i("DOMANDA DA SALVARE",txtContestoGuest.getText().toString());
                            Background b = new Background();
                            b.execute(mess);
                        }
                    }



                }
            }
        });

        //Quando l'utente preme fuori dalla tastiera, quest'ultima viene nascosta
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });

        //Imposto il messaggio di benvenuto
        setMessaggioBenvenuto();

        //Imposto il nome dell'utente nel menu
        View headerView = navigationView.getHeaderView(0);
        nomeUtente = (TextView) headerView.findViewById(R.id.txtNomeUtente);
        setNomeUtente();

        //Imposto il logo dell'utente nel menu
        imageLogoUtente = (ImageView) headerView.findViewById(R.id.imageLogoUtente);
        setLogoUtente();


        //CODICE PER OTTENERE ACCESS TOKEN SPOTIFY------- UTILIZZI FUTURI
            /*AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{"streaming"});
            AuthenticationRequest request = builder.build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);*/



    }


    public List<ChatData> setData(){
        List<ChatData> data = new ArrayList<>();

        return data;
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

    //OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_userphoto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            Intent intent = new Intent(ChatGuest.this,ChatGuest.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            //Elimino le credenziali dalle shared preferences
            PreferenceData.setUserLoggedInStatus(this,false);   // Imposto il login status
            PreferenceData.clearLoggedInEmailAddress(this);


            Intent intent = new Intent(ChatGuest.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class Background extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
        }

        /**
         * Operazioni effettuate dopo il Post
         *
         * @param s
         */
        @Override
        protected void onPostExecute(ArrayList<String> s) {

            try {

                //Esempio valore JSON {"intentName":"Identita utente","confidence":1,"answer":"Ti chiami Cataldo Musto"}

                String result = s.get(0);
                String mess = s.get(1);
                Log.i("RESULT",result);


                JSONObject arr = new JSONObject(result);

                String answer = arr.getString("answer");
                String intentName = arr.getString("intentName");
                double confidence = Double.parseDouble(arr.getString("confidence"));


                //YOUTUBE --> Valori soglia diversi
                if(intentName.equalsIgnoreCase("Ricerca Video")){


                    List<ChatData> data = new ArrayList<ChatData>();
                    ChatData item = new ChatData();
                    Date currentTime = Calendar.getInstance().getTime();
                    item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                    item.setType("6");//Imposto il layout della risposta, ovvero YOUTUBE

                    //Url da visualizzare
                    String url = answer;

                    item.setText(url);
                    data.add(item);
                    mAdapter.addItem(data);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);


                }else if (intentName.equalsIgnoreCase("Musica")){
                    Log.w("ANSWER",answer);

                    JSONObject answerOb = arr.getJSONObject("answer");
                    String urlO = answerOb.getString("url");

                    if (!urlO.isEmpty()){
                        if (answer.contains("track")){
                            Log.w("ANSWER","brano");

                             answerOb = arr.getJSONObject("answer");
                             urlO = answerOb.getString("url");
                            //String explainO = answerOb.getString("explain");
                            Log.i("urlO",urlO);
                            //Log.i("explainO",explainO);


                            List<ChatData> data = new ArrayList<ChatData>();
                            ChatData item = new ChatData();
                            Date currentTime = Calendar.getInstance().getTime();
                            item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                            item.setType("7");

                            item.setText(urlO);
                            item.setFlag(0);//Indico che è una canzone
                            data.add(item);
                            mAdapter.addItem(data);
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                            spotifyAppRemote = item.getmSpotifyAppRemote();



                        }else {
                            Log.w("ANSWER","playlist");

                             answerOb = arr.getJSONObject("answer");
                             urlO = answerOb.getString("url");
                            String explainO = answerOb.getString("explain");
                            Log.i("urlO",urlO);
                            Log.i("explainO",explainO);


                            List<ChatData> data = new ArrayList<ChatData>();
                            ChatData item = new ChatData();
                            Date currentTime = Calendar.getInstance().getTime();
                            item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                            item.setType("5");

                            item.setText(urlO);
                            item.setFlag(1);//Indico che è una playlist
                            item.setSpiegazione("");//Spiegazione
                            data.add(item);
                            mAdapter.addItem(data);
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                            spotifyAppRemote = item.getmSpotifyAppRemote();



                        }
                    }else {
                        List<ChatData> data = new ArrayList<ChatData>();
                        ChatData item = new ChatData();
                        Date currentTime = Calendar.getInstance().getTime();
                        item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                        item.setType("1");

                        item.setText(getResources().getString(R.string.loginMyrrorStr));
                        data.add(item);
                        mAdapter.addItem(data);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }



                } else if(intentName.equals("News")){
                    JSONObject news = new JSONObject(answer);
                    String url = news.getString("url");
                    String img = news.getString("image");
                    String title = news.getString("title");

                    if (!url.isEmpty()){
                        answer = title;
                        List<ChatData> data = new ArrayList<ChatData>();
                        ChatData item = new ChatData();
                        Date currentTime = Calendar.getInstance().getTime();
                        //item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));

                        item.setImg(img);
                        item.setType("3");

                        item.setText(url);
                        item.setSpiegazioneNews("");

                        data.add(item);
                        mAdapter.addItem(data);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }else {
                        List<ChatData> data = new ArrayList<ChatData>();
                        ChatData item = new ChatData();
                        Date currentTime = Calendar.getInstance().getTime();
                        item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                        item.setType("1");

                        item.setText(getResources().getString(R.string.loginMyrrorStr));
                        data.add(item);
                        mAdapter.addItem(data);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }




                }else if(intentName.equals("Meteo")){

                    JSONObject obj = new JSONObject(answer);
                    String meteo = obj.getString("res");
                    String city = obj.getString("city");
                    List<ChatData> data = new ArrayList<ChatData>();
                    ChatData item = new ChatData();

                    item.answerLog = result;


                    item.list = new ArrayList<Meteo>();

                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.user4);
                    String ora = ""  ;
                    int temp ;

                    if(answer.equals("")){
                        item.setType("1");
                        String messaggio = getResources().getString(R.string.datiNonPresentiStr);
                        item.setText(messaggio);

                        data.add(item);

                        mAdapter.addItem(data);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }
                    String[] array = meteo.split("<br>");
                    String dataM = getResources().getString(R.string.datiMeteo1Str) + " " + city + " "
                            + getResources().getString(R.string.datiMeteo2Str) + " ";

                    for (int i = 0;i< array.length;i++){
                        String record[] = array[i].split(";");

                        if(i == 0)
                            dataM += record[0].trim();

                        ora = record[1].trim();
                        temp =(int) Float.parseFloat(record[2].trim());
                        String condition = record[3].trim();
                        switch (condition){
                            case "cielo sereno":
                                drawable = res.getDrawable(R.drawable.icon2);
                                break;

                            case "poco nuvoloso":
                                drawable = res.getDrawable(R.drawable.icon1);
                                break;

                            case "parzialmente nuvoloso":
                                drawable = res.getDrawable(R.drawable.icon7);
                                break;

                            case "nubi sparse":
                                drawable = res.getDrawable(R.drawable.icon6);
                                break;

                            case "pioggia leggera":
                                drawable = res.getDrawable(R.drawable.icon4);
                                break;

                            case "nuvoloso":
                                drawable = res.getDrawable(R.drawable.icon5);
                                break;

                            case "piogge modeste":
                                drawable = res.getDrawable(R.drawable.icon9);
                                break;

                            case "pioggia pesante":
                                drawable = res.getDrawable(R.drawable.icon11);
                                break;

                            case "neve leggera":
                                drawable = res.getDrawable(R.drawable.icon13);
                                break;

                            case "neve":
                                drawable = res.getDrawable(R.drawable.icon14);
                                break;
                        }


                        if(!ora.equals("")){
                            if(Integer.parseInt(ora.substring(0,2)) > 6){
                                Meteo m = new Meteo(temp,ora,drawable);
                                item.list.add(m);
                            }

                        }

                    }

                    ChatData item2 = new ChatData();
                    item2.setType("1");
                    item2.setText(dataM);
                    data.add(item2);



                    item.setType("4");


                    data.add(item);
                    mAdapter.addItem(data);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 2);


                } else {
                    Log.w("ANSWER",answer);
                    List<ChatData> data = new ArrayList<ChatData>();
                    ChatData item = new ChatData();
                    Date currentTime = Calendar.getInstance().getTime();
                    item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
                    item.setType("1");

                    item.setText(getResources().getString(R.string.loginMyrrorStr));
                    data.add(item);
                    mAdapter.addItem(data);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * Operazioni da effettuare in background
         *
         * @param voids
         * @return
         */
        @Override
        protected ArrayList<String> doInBackground(String... voids) {

            String mess = voids[0];//Domanda dell'utente

            String result = "";
            String urlString = "http://90.147.102.243:8080/php/intentGuest.php";

            try {

                //Imposto parametri per la connessione
                URL url = new URL(urlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);


                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));

                String data = "";
                if (city == null){
                    data = URLEncoder.encode("testo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(voids[0]), "UTF-8");
                }else {
                    //Stringa di output
                    data = URLEncoder.encode("testo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(voids[0]), "UTF-8")
                            +"&&city="+URLEncoder.encode(city,"UTF-8");
                }

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));

                String line = "";

                while ((line = reader.readLine()) != null) {
                    result += line;


                    reader.close();
                    ips.close();
                    http.disconnect();

                }

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(result);
                arrayList.add(mess);

                return arrayList;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(result);
            arrayList.add(mess);

            return arrayList;
        }
    }

    //Conta le parole in una frase
    public static int countWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(sentence);
        return tokens.countTokens();
    }

    //Inserisce stringa in un'altra
    public static String insertString(String originalString, String stringToBeInserted, int index) {

        // Create a new string
        String newString = new String();

        for (int i = 0; i < originalString.length(); i++) {

            // Insert the original string character into the new string
            newString += originalString.charAt(i);

            if (i == index) {

                // Insert the string to be inserted into the new string
                newString += stringToBeInserted;
            }
        }

        //Return the modified String
        return newString;
    }

    @Override
    protected void onStop() {
        super.onStop();


        if (spotifyAppRemote != null){
            Log.i("stato player","DISCONESSO");
            SpotifyAppRemote.disconnect(getSpotifyAppRemote());
        }

    }

    //CODICE PER OTTENERE ACCESS TOKEN ------- UTILIZZI FUTURI
    /*protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    setAccessToken(response.getAccessToken());
                    Log.i("success",getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.i("error","nooo");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.i("default","default");

            }
        }
    }*/

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    //metodo per prendere la posizione del dispositivo
    private void getDeviceLocation() {

        try {

            //trovo l'ultima posizione
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //se la posizione ottenuta è corrette e non nulla
                    if(location != null){

                        double lat = location.getLatitude();
                        double longitude = location.getLongitude();

                        LatLng myLocation = new LatLng(lat,longitude);
                        position = myLocation;
                        getCityBackground b = new getCityBackground();
                        b.execute(myLocation);


                    }
                }


            });
        } catch (SecurityException e) {
            Log.e("Chat", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    public class getCityBackground extends AsyncTask<LatLng,Void,String>{


        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray arr = new JSONObject(s).getJSONArray("results");
                for (int i= 0;i< arr.length();i++) {
                    JSONArray o = arr.getJSONObject(i).getJSONArray("locations");

                    for (int j=0;j<o.length();j++){
                        JSONObject  temp =  o.getJSONObject(i);
                        if(temp.get("adminArea5") != null){
                            city = temp.getString("adminArea5");
                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            LatLng coords = latLngs[0];
            String coordinate = coords.latitude+","+coords.longitude;
            String result = "";
            String urlString = "http://www.mapquestapi.com/geocoding/v1/reverse?key=pJ4Mo5GOTpHMvuCRlIyiFomZqsSAeAHM&location="+coordinate+"&includeRoadMetadata=true&includeNearestIntersection=true";

            try {
                //Imposto parametri per la connessione
                URL url = new URL(urlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setDoInput(true);
                http.setDoOutput(true);
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));

                String line;

                while ((line = reader.readLine()) != null) {
                    result += line;
                    reader.close();
                    ips.close();
                    http.disconnect();

                }

                return result;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


    }


    public void setNomeUtente(){
        /*BackgroundUtente b = new BackgroundUtente();
        b.execute();*/
        nomeUtente.setText("Nome utente");

    }

    public void setLogoUtente(){
        /*BackgroundLogoUtente b = new BackgroundLogoUtente();
        b.execute();*/
    }

    public void setMessaggioBenvenuto(){

        List<ChatData> data = new ArrayList<ChatData>();
        ChatData item = new ChatData();
        Date currentTime = Calendar.getInstance().getTime();
        item.setTime(String.valueOf(currentTime.getHours()) + ":" + String.valueOf(currentTime.getMinutes()));
        item.setType("3");//Imposto il layout della risposta, ovvero YOU
        item.setText(getResources().getString(R.string.botBenvenutoStr));
        item.setSpiegazioneNews("");
        data.add(item);
        mAdapter.addItem(data);
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        text.setText("");

    }



    public class BackgroundUtente extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
        }

        /**
         * Operazioni effettuate dopo il Post
         *
         * @param s
         */
        @Override
        protected void onPostExecute(ArrayList<String> s) {

            String result = s.get(0);
            String mess = s.get(1);

            //Imposto il nome sul menu
            nomeUtente.setText(result);

        }

        /**
         * Operazioni da effettuare in background
         *
         * @param voids
         * @return
         */
        @Override
        protected ArrayList<String> doInBackground(String... voids) {



            String mess = "";//Domanda dell'utente

            String result = "";
            String urlString = "http://90.147.102.243:8080/php/setNominativo.php";

            try {

                //Imposto parametri per la connessione
                URL url = new URL(urlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);


                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));


                String data = "mail=" + URLEncoder.encode(email,"UTF-8");


                Log.i("data",data);


                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));

                String line = "";

                while ((line = reader.readLine()) != null) {
                    result += line;


                    reader.close();
                    ips.close();
                    http.disconnect();

                }

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(result);
                arrayList.add(mess);

                return arrayList;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(result);
            arrayList.add(mess);

            return arrayList;
        }
    }

    //Ottengo l'url dell'immagine dell'utente
    public class BackgroundLogoUtente extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
        }

        /**
         * Operazioni effettuate dopo il Post
         *
         * @param s
         */
        @Override
        protected void onPostExecute(ArrayList<String> s) {

            String result = s.get(0);
            String mess = s.get(1);

            //Imposto l'url del logo dell'utente
            GetImageFromURL load = new GetImageFromURL();
            load.execute(result);

        }

        /**
         * Operazioni da effettuare in background
         *
         * @param voids
         * @return
         */
        @Override
        protected ArrayList<String> doInBackground(String... voids) {

            String mess = "";//Domanda dell'utente

            String result = "";
            String urlString = "http://90.147.102.243:8080/php/getProfileImage.php";

            try {

                //Imposto parametri per la connessione
                URL url = new URL(urlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);


                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));


                String data = "mail=" + URLEncoder.encode(email,"UTF-8");


                Log.i("data",data);


                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));

                String line = "";

                while ((line = reader.readLine()) != null) {
                    result += line;


                    reader.close();
                    ips.close();
                    http.disconnect();

                }

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(result);
                arrayList.add(mess);

                return arrayList;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(result);
            arrayList.add(mess);

            return arrayList;
        }
    }

    //Ottengo l'immagine dall'url
    public class GetImageFromURL extends AsyncTask<String,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageLogoUtente.setImageBitmap(bitmap);
        }

    }

}
