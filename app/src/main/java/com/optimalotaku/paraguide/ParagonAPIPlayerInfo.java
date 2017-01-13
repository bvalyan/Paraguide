package com.optimalotaku.paraguide;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bvaly on 1/8/2017.
 */

public class ParagonAPIPlayerInfo extends AsyncTask<Void, Void, String> {

    private String authCode;
    public PlayerInfoResponse delegate = null;
    private PlayerData pData;
    private String playerName;
    private ProgressBar pBar;

    public ParagonAPIPlayerInfo(ProgressBar pb, String pName, PlayerData pData){
        /*
            This constructor takes 3 parameters:
             - Progress bar object from the main class
             - The name entered by the user from the UI
             - A PlayerData object that the data will be loaded into
         */
        this.pBar = pb;
        this.playerName = pName;
        this.pData = pData;
    }

    @Override
    protected String doInBackground(Void... voids) {
        URL url2 = null;
        URL url3 = null;
        HttpURLConnection urlConnection2 = null;
        HttpURLConnection urlConnection3 = null;
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();

        try {
            url2 = new URL("https://developer-paragon.epicgames.com/v1/accounts/find/" + playerName);
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);

            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                stringBuilder2.append(line2);
            }
            bufferedReader2.close();
            String foundAccountID = stringBuilder2.toString();
            JSONObject obj2 = null;

            obj2 = new JSONObject(foundAccountID);
            foundAccountID = obj2.getString("accountId");
            url3 = new URL("https://developer-paragon.epicgames.com/v1/account/" + foundAccountID + "/stats");
            urlConnection3 = (HttpURLConnection) url3.openConnection();
            urlConnection3.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);

            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));

            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                stringBuilder3.append(line3).append("\n");
            }
            bufferedReader3.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuilder3.toString();
}

    protected void onPostExecute(String response){
        if (response == null) {
            Log.i("INFO", "PLAYER LOOKUP ERROR");
        }
        else{
            Log.i("INFO", response);
            JSONObject playerStats = null;
            try {

                playerStats = new JSONObject(response);

                pData.setMatches(playerStats.getString("matchesPlayed"));
                pData.setWins(playerStats.getString("matchWins"));
                pData.setAssists(playerStats.getString("heroAssists"));
                pData.setDeaths(playerStats.getString("deathsToHero"));
                pData.setHeroKills(playerStats.getString("heroKills"));
                pData.setCoreKills(playerStats.getString("coreKills"));
                pData.setTowerKills(playerStats.getString("towerKills"));
                pData.setHeroHealing(playerStats.getString("heroHealing"));
                pData.setSelfHealing(playerStats.getString("selfHealing"));
                pData.setPlayerName(playerName);
                }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        delegate.processPlayerInfoFinish(pData);
    }
}