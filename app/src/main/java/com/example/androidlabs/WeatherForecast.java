package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar processStatus;
    TextView ct;
    TextView minTemp;
    TextView maxTemp;
    TextView uvRating;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        // Find all GUI components
        processStatus = (ProgressBar) findViewById(R.id.status);
        ct = (TextView) findViewById(R.id.currentTemp);
        minTemp = (TextView) findViewById(R.id.minTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        uvRating = (TextView) findViewById(R.id.uvRating);
        img = (ImageView) findViewById(R.id.weatherImage);

        processStatus.setVisibility(View.VISIBLE);

        this.runQuery();
    }

    private void runQuery()
    {
        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute( );
    }

    // Inner class that extends AsyncTask
    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        String windUV;
        String min;
        String max;
        String currentTemp;
        Bitmap image = null;

        String value;
        double uvVal;

        @Override
        protected String doInBackground(String... strings) {

            String retVal = null;
            String queryURL1 = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String queryURL2 = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

            try{
                // For query URL1 -> XML
                URL url = new URL(queryURL1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                // For query URL2 -> JSON
                URL url2 = new URL(queryURL2);
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                InputStream inStream2 = urlConnection2.getInputStream();

                //Set up the XML parser for queryURL1
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                // Set up the JSON object parser for queryURL2
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream2, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                uvVal = jObject.getDouble("value");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {
                                value = xpp.getAttributeValue(null, "value"); //What is the String associated with message?
                                publishProgress(25);

                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(50);

                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            else if(tagName.equals("weather"))
                            {
                                String iconName = xpp.getAttributeValue(null, "icon");

                                // Image URL to download
                                String imgURL = "http://openweathermap.org/img/w/" + iconName + ".png";

                                Log.i("Searching for image:", iconName);

                                if(fileExistance(iconName)){ // If the file exists in local storage
                                    // Read the file
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(iconName);
                                    }
                                    catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    image = BitmapFactory.decodeStream(fis);
                                    Log.i(iconName, " already exists");

                                }
                                else{

                                    // Download image
                                    URL bmpUrl = new URL(imgURL);
                                    HttpURLConnection connection = (HttpURLConnection) bmpUrl.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        image = BitmapFactory.decodeStream(connection.getInputStream());
                                    }

                                    // Saving to local storage
                                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    publishProgress(100);

                                    Log.i(iconName, " downloaded");

                                }
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            }
            catch(MalformedURLException mfe){ retVal = "Malformed URL Exception"; }
            catch(IOException ioe) { retVal = "IO Exception. Is the WiFi connected?"; }
            catch(XmlPullParserException ppe) { retVal = "XML Pull exception. The XML is not properly formed"; }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return retVal;
        }

        @Override                    //Type 3
        protected void onPostExecute(String sentFromDoInBackground){
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            processStatus.setVisibility(View.INVISIBLE);
            ct.setText("Current Temperature: " + value);
            minTemp.setText("Low: " + min);
            maxTemp.setText("High: "+ max);
            uvRating.setText("UV rating: " + String.valueOf(uvVal));
            img.setImageBitmap(image);

        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:
            processStatus.setVisibility(View.VISIBLE);
            processStatus.setProgress(values[0]);
        }
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}
