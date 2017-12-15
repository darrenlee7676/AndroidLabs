package com.example.darre.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {
    private static final String URL="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    private TextView tempNowText;
    private TextView tempMinText;
    private TextView tempMaxText;
    private ImageView picTempView;
    private ProgressBar progressBar;
    String iconName;

    private static final String ACTIVITY_NAME="WeatherForecast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_forecast);

        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        tempNowText=(TextView)findViewById(R.id.temp_now);

        tempMinText=(TextView)findViewById(R.id.temp_min);

        tempMaxText=(TextView)findViewById(R.id.temp_max);

        picTempView=(ImageView)findViewById(R.id.image_tempNow);

        //final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        ForecastQuery forecastQuery=new ForecastQuery();
        forecastQuery.execute(URL);
    }

    public boolean fileExistance(String fname){

        Log.i(ACTIVITY_NAME,getBaseContext().getFileStreamPath(fname).toString());

        File file=getBaseContext().getFileStreamPath(fname);

        return file.exists();

    }


    public static Bitmap getImage(URL url){

        HttpURLConnection connection = null;

        try{

            connection=(HttpURLConnection)url.openConnection();

            connection.connect();

            int responseCode=connection.getResponseCode();

            if(responseCode==200){

                return BitmapFactory.decodeStream(connection.getInputStream());

            }else

                return null;

        }catch (Exception e){

            return null;

        }finally{

            if(connection !=null){

                connection.disconnect();
            }
        }
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        String tempMin;
        String tempMax;
        String tempNow;
        Bitmap picTemp;
        String iconName;

        @Override

        protected String doInBackground(String ... urls){

            try{//set up a connection

                URL url=new URL(urls[0]);

                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();

                urlConnection.setReadTimeout(10000);

                urlConnection.setConnectTimeout(15000);

                urlConnection.setRequestMethod("GET");

                urlConnection.setDoInput(true);

                urlConnection.connect();

                //Instantiate the parser
/*XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser parser = factory.newPullParser();

                InputStream stream = urlConnection.getInputStream();

                parser.setInput(stream, "UTF-8");*/

                XmlPullParser parser= Xml.newPullParser();

                InputStream stream = urlConnection.getInputStream();

                parser.setInput(stream, null);


                while(parser.next()!=XmlPullParser.END_DOCUMENT){

                    if(parser.getEventType()!=XmlPullParser.START_TAG){
                        continue;
                    }

                    String name = parser.getName();
                    //Starts by looking for the entry tag
                    if(name.equals("temperature")){

                        tempNow=parser.getAttributeValue(null, "value");

                        publishProgress(25);

                        tempMin=parser.getAttributeValue(null, "min");

                        publishProgress(50);

                        tempMax=parser.getAttributeValue(null, "max");

                        publishProgress(75);

                    }

                    if(name.equals("weather")) {

                        iconName = parser.getAttributeValue(null, "icon");

                        String iconFileName = iconName + ".png";

                        if (fileExistance(iconFileName)) {

                            FileInputStream inputStream=null;

                            try {

                                //inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFileName));

                                inputStream=openFileInput(iconFileName);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            picTemp = BitmapFactory.decodeStream(inputStream);

                            Log.i(ACTIVITY_NAME+iconFileName, "Image is found locally" );

                        }

                        else{
                            URL urlIcon=new URL("http://openweathermap.org/img/w/"+iconName+".png");

                            picTemp=getImage(urlIcon);

                            FileOutputStream outputStream=openFileOutput(iconName+".png", Context.MODE_PRIVATE);

                            picTemp.compress(Bitmap.CompressFormat.PNG, 80 , outputStream);

                            outputStream.flush();

                            outputStream.close();

                            Log.i(ACTIVITY_NAME+iconFileName, "Image is downloaded");
                        }
                        publishProgress(100);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override

        protected void onProgressUpdate(Integer ...value){

            Log.i(ACTIVITY_NAME, "in onProgressUpdate");
            progressBar.setProgress(value[0]);
        }



        @Override

        protected void onPostExecute(String result){
            Log.i(ACTIVITY_NAME, "In onPostExecute");

            tempNowText.setText(tempNowText.getText()+" "+tempNow+" \u2103");

            tempMinText.setText(tempMinText.getText()+" "+tempMin+" \u2103");

            tempMaxText.setText(tempMaxText.getText()+" "+tempMax+" \u2103");

            picTempView.setImageBitmap(picTemp);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}