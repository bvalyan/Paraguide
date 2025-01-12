package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Brandon on 1/30/17.
 */
public class DeckDelete extends AsyncTask<String,Void,Integer>{
    Context mcontext;
    FileManager fileManager;
    HashMap<String,HeroData> heroDataMap;
    String deckID;
    String deckName;
    Intent i;


    public DeckDelete(Context context) {
        this.mcontext = context;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        deckID = strings[0];
        deckName = strings[1];
        String authToken;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        authToken = prefs.getString("TOKEN","NULL");
        String accountID = prefs.getString("ACCOUNT_ID", "NULL");
        try {
            URL url = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/deck/" + deckID);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection.addRequestProperty(Constants.AUTH_VAR, "Bearer " + authToken);
            urlConnection.setRequestMethod("DELETE");
            System.out.println(urlConnection.getResponseCode());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }


    protected void onPostExecute(Integer result){
        if (result == 1){
            heroDataMap = new HashMap<>();
            fileManager = new FileManager(this.mcontext);
            try {
                heroDataMap = fileManager.readHeroFromStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
