package com.backand.backandandroidsample;

import android.util.Base64;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackandConnector {
    private String masterToken;
    private String userToken;
    private String appName;

    private final String TAG = "Backand";
    public BackandConnector(String appName, String userToken, String masterToken) {
        this.masterToken = masterToken;
        this.userToken = userToken;
        this.appName = appName;
    }

    public String sendGetRequest(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        String userNamePasswordCombination = this.masterToken + ":" + this.userToken;
        final String basicAuth = "Basic " +  Base64.encodeToString(userNamePasswordCombination.getBytes(), Base64.NO_WRAP);

        try {

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("AppName", this.appName);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                InputStream in = conn.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    stringBuilder.append(current);
                }
            } else {
                Log.d(TAG, "readRemoteJson: " + conn.getResponseMessage());
                return "";
            }
        } catch (Exception e) {
            Log.e(TAG, "readRemoteJson error: " + e.getMessage());
        }
        return  stringBuilder.toString();
    }
}
