package com.example.darre.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        final Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {

                                  public void onClick(View view) {
                                      Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                                      startActivityForResult(intent, 10);
                                  }
                              }
        );
        final Button b2 = (Button) findViewById(R.id.button3);
        b2.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                                      startActivityForResult(intent, 10);
                                  }
                              }
        );
        final Button b3 = (Button) findViewById(R.id.weather);
        b3.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View W) {
                                      Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                                      startActivityForResult(intent, 10);
                                  }
                              }
        );
        final Button b4 = (Button) findViewById(R.id.toolbarButton);
        b4.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View W) {
                                      Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                                      startActivityForResult(intent, 10);
                                  }
                              }
        );
    }






    protected void onActivityResult(int requestCode, int responseCode, Intent data)  {
        if (requestCode == 10)  {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }

        if(responseCode==Activity.RESULT_OK){
            String result = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this , result, Toast.LENGTH_LONG);
            toast.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
