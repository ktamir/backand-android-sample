package com.backand.backandandroidsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

// A sample app for fetching data from your Back& app
public class MainActivity extends AppCompatActivity {

    private final String USER_TOKEN = "<< Backand user token here >>";
    private final String MASTER_TOKEN = "<< Backand master token here >>";
    private final String APP_NAME = "<< Backand app name >>";
    private final String BACKAND_URL = "https://api.backand.com/1/objects/items";
    private Handler uiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Standard android activity initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uiHandler = new Handler();

        // Create a thread so there won't be network on ui thread
        Thread newThread = new Thread() {
            public void run() {
                sendRequestToBackand();
            }
        };
        newThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void sendRequestToBackand() {
        BackandConnector connector = new BackandConnector(APP_NAME, USER_TOKEN, MASTER_TOKEN);
        try {
            final String response = connector.sendGetRequest(new URL(BACKAND_URL));
            // Use handler to switch to ui thread
            // UI can be manipulated only from main thread
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView content = (TextView)findViewById(R.id.backand_content);
                    if (content != null) {
                        content.setText(response);
                    }
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
