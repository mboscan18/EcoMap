package com.miguelboscan.ecomap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Miguel on 05/06/2015.
 */

public class AgregarEvento extends Activity {
    private EditText tituloEvento, comentario_nuevo_evento, cat_event_nuevo;
    private Button  addEvento;
    private String latitud, longitud;
    private String archivo;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";

    //testing on Emulator:
    private static final String REGISTER_URL = "http://eco-map.esy.es/BD_Function/agregarEvento.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        tituloEvento = (EditText)findViewById(R.id.tituloEvento);
        comentario_nuevo_evento = (EditText)findViewById(R.id.comentario_nuevo_evento);
        cat_event_nuevo = (EditText)findViewById(R.id.cat_event_nuevo);

        addEvento = (Button)findViewById(R.id.agregarEvento);

        //Obtenemos una referencia al LocationManager
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        latitud = String.valueOf(loc.getLatitude());
        longitud = String.valueOf(loc.getLongitude());


        // Para obtener la fecha y hora actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String fecha = sdf.format(c.getTime());

        Time hora = new Time();
        hora.setToNow();

        addEvento.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                // Check for success tag
                int success;
                String titulo = tituloEvento.getText().toString();
                String comentario = comentario_nuevo_evento.getText().toString();
                String categoria = cat_event_nuevo.getText().toString();
                try {
                    // Building Parameters
                    List params = new ArrayList();
                    params.add(new BasicNameValuePair("titulo", titulo));
                    params.add(new BasicNameValuePair("comentario", comentario));
                    params.add(new BasicNameValuePair("categoria", categoria));
                    params.add(new BasicNameValuePair("latitud", latitud));
                    params.add(new BasicNameValuePair("longitud", longitud));
                    params.add(new BasicNameValuePair("fecha", fecha));
                    params.add(new BasicNameValuePair("password", comentario));
                    params.add(new BasicNameValuePair("archivo", "La direccion"));
                    params.add(new BasicNameValuePair("archivoCom", "La direccion"));

                    Log.d("request!", "starting");

                    //Posting user data to script
                    JSONObject json = jsonParser.makeHttpRequest(
                            REGISTER_URL, "POST", params);

                    // full json response
                    Log.d("Registering attempt", json.toString());

                    // json success element
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("User Created!", json.toString());
                        finish();
                        Toast.makeText(AgregarEvento.this, json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                        Toast.makeText(AgregarEvento.this, json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}

