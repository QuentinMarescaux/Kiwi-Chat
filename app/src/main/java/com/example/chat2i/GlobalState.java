package com.example.chat2i;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GlobalState extends Application {

    @NonNull
    public String cat = "L4-SI-Logs";
    @NonNull
    public String CAT = "L4-SI-Logs";

    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public void log(String s) {
        Log.i(CAT,s);
    }

    public void alerter(String s) {
        Log.i(CAT,s);
        Toast t = Toast.makeText(this,s, Toast.LENGTH_LONG);
        t.show();
    }


    @NonNull
    private String convertStreamToString(@NonNull InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @NonNull
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET})
    public String requete(@Nullable String qs) {
        if (qs != null)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String urlData = prefs.getString("urlData","http://chaire-ecommerce.ec-lille.fr/ime5/data.php");

            try {
                URL url = new URL(urlData + "?" + qs);
                Log.i(CAT,"url utilisée : " + url.toString());
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = null;
                in = new BufferedInputStream(urlConnection.getInputStream());
                String txtReponse = convertStreamToString(in);
                urlConnection.disconnect();
                return txtReponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return "";
    }

    @RequiresPermission(allOf = {
            Manifest.permission.INTERNET})
    public void volleyJsonObjectRequest(String qs, final VolleyCallback callback){

        String REQUEST_TAG = "volleyJsonObjectRequest";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String urlData = prefs.getString("urlData","http://chaire-ecommerce.ec-lille.fr/ime5/data.php");

        final String url = urlData + "?" + qs;

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(CAT, "Error: " + error.getMessage());
                if (error.getMessage() != null)
                    alerter("Error: " + error.getMessage() + ", lors de la requête `" + url + "`");
                else
                    alerter("Error: Un problème est survenu lors de la requête `" + url + "`");
            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

    @NonNull
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET})
    public boolean verifReseau()
    {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {
            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }
            }
        }

        this.alerter(sType);
        return bStatut;
    }
}
