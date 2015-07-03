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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.miguelboscan.ecomap.R.*;

/**
 * Created by Miguel on 05/06/2015.
 */

public class AgregarEvento extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText tituloEvento, comentario_nuevo_evento, cat_event_nuevo;
    private Button  addEvento;
    private String latitud, longitud;
    private String archivo, fecha;
    private Spinner cat_event_nuev;


    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    private int idd;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";
    String[] strings = {"Aguas residuales","Basura","Bote de agua","Contaminacion Industrial","Contaminacion Sonica","Charlas","Deforestacion","Derrumbe","Desarrollo Hurbano","Desechos","Incendio","Inundacion","Paisaje","Playa","Rio","Zona Para acampar"};


    String[] subs = {"Aguas residuales","Basura","Bote de agua","Contaminacion Industrial","Contaminacion Sonica","Charlas","Deforestacion","Derrumbe","Desarrollo Hurbano","Desechos","Incendio","Inundacion","Paisaje","Playa","Rio","Zona Para acampar"};

    int arr_images[] = { drawable.symbol_aguas_residuales,
            drawable.symbol_basura, drawable.symbol_bote_agua,
            drawable.symbol_charlas, drawable.symbol_contaminacion_industrial, drawable.symbol_contaminacion_sonica,drawable.symbol_deforestacion,
            drawable.symbol_derrumbe, drawable.symbol_desarrollo_urbano, drawable.symbol_desechos, drawable.symbol_incendio,drawable.symbol_inundacion,
            drawable.symbol_paisaje, drawable.symbol_playa,drawable.symbol_rio, drawable.symbol_zona_acampar};

    //testing on Emulator:
    private static final String REGISTER_URL = "http://eco-map.esy.es/BD_Function/agregarEvento.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(layout.add_evento);

        tituloEvento = (EditText)findViewById(id.tituloEvento);
        comentario_nuevo_evento = (EditText)findViewById(id.comentario_nuevo_evento);

        addEvento = (Button)findViewById(id.agregarEvento);

        //Obtenemos una referencia al LocationManager
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        /*latitud = String.valueOf(loc.getLatitude());
        longitud = String.valueOf(loc.getLongitude());*/


        latitud = "8.282069";
        longitud = "-62.727008";
        // Para obtener la fecha y hora actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(c.getTime());

        Time hora = new Time();
        hora.setToNow();

        addEvento.setOnClickListener(this);




        cat_event_nuev = (Spinner) findViewById(id.cat_event_nuevo);
        cat_event_nuev.setAdapter(new MyAdapter(AgregarEvento.this, R.layout.row, strings));

// Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
         //      R.array.pruebaa_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        cat_event_nuev.setOnItemSelectedListener(this);
        //cat_event_nuev.setAdapter(adapter);
       // cat_event_nuev.setBackgroundResource(drawable.symbol_aguas_residuales);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub







        new CreateUser().execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       idd= parent.getSelectedItemPosition();
        pDialog2 = new ProgressDialog(AgregarEvento.this);
        pDialog2.setMessage("Creando Evento..."+ idd);

        pDialog2.setIndeterminate(false);
        pDialog2.setCancelable(true);
        pDialog2.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(strings[position]);

            TextView sub=(TextView)row.findViewById(R.id.sub);
            sub.setText(subs[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);

            return row;
        }
    }









    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AgregarEvento.this);
            pDialog.setMessage("Creando Evento...");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            final String message;
            String titulo = tituloEvento.getText().toString();
            String comentario = comentario_nuevo_evento.getText().toString();
            String categoria =""+idd;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("titulo", titulo));
                params.add(new BasicNameValuePair("comentario", comentario));
                params.add(new BasicNameValuePair("categoria",categoria));
                params.add(new BasicNameValuePair("latitud", latitud));
                params.add(new BasicNameValuePair("longitud", longitud));
                params.add(new BasicNameValuePair("fecha", fecha));
                params.add(new BasicNameValuePair("hora", "10:25"));
                params.add(new BasicNameValuePair("archivo", "La direccion"));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.getString(TAG_MESSAGE).toString());
                    message = json.getString(TAG_MESSAGE).toString();
                    AgregarEvento.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, message, Toast.LENGTH_LONG, "success");
                            toast.show();
                        }
                    });

                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    message = json.getString(TAG_MESSAGE);
                    AgregarEvento.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, message, Toast.LENGTH_LONG, "error");
                            toast.show();
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(AgregarEvento.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
