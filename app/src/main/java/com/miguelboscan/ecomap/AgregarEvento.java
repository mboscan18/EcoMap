package com.miguelboscan.ecomap;

import java.io.File;
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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
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
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import static com.miguelboscan.ecomap.R.*;

/**
 * Created by Miguel on 05/06/2015.
 */

public class AgregarEvento extends Activity implements AdapterView.OnItemSelectedListener {
    private EditText tituloEvento, comentario_nuevo_evento, cat_event_nuevo;
    private Button  addEvento, quitarImagen;
    private Double latitud, longitud;
    private String archivo, fecha, hora;
    private Spinner cat_event_nuev;
    private ImageButton cargarArchivo;


    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    private int idd;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";
    String[] strings = {"Bote de Agua","Aguas residuales","Basura Acumulada", "Charlas Ambientalistas","Contaminacion Industrial","Contaminacion Sonica","Deforestacion","Zona para Acampar","Derrumbe","Desarrollo Urbano","Desechos Toxicos","Incendio","Inundacion","Paisaje Natural","Playa","Rio o Lago"};


    String[] subs = {"Derrame de Agua/Tubo Roto","Aguas Contaminadas/Cloacas","Basura en la calle/Acumalcion de Basura", "Foros/Conferencias/Charlas","Contaminacion por Emmpresas","Contaminacion Sonica","Tala de Arboles/Daño a las Plantas","Zona para Acampar","Derrumbe","Desarrollo Urbano","Desechos Toxicos","Incendio","Inundacion","Paisaje Natural","Balneario Turistico","Rio o Lago"};

    int arr_images[] = { drawable.symbol_bote_agua, drawable.symbol_aguas_residuales,
            drawable.symbol_basura, drawable.symbol_charlas, drawable.symbol_contaminacion_industrial,
            drawable.symbol_contaminacion_sonica, drawable.symbol_deforestacion, drawable.symbol_zona_acampar,
            drawable.symbol_derrumbe, drawable.symbol_desarrollo_urbano, drawable.symbol_desechos, drawable.symbol_incendio,
            drawable.symbol_inundacion, drawable.symbol_paisaje, drawable.symbol_playa,drawable.symbol_rio};

    //testing on Emulator:
    private static final String REGISTER_URL = "http://eco-map.esy.es/BD_Function/agregarEvento.php";
    private static final String UPLOAD_URL = "http://eco-map.esy.es/BD_Function/upload_File.php.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    Bitmap photobmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(layout.add_evento);

        tituloEvento = (EditText)findViewById(id.tituloEvento);
        comentario_nuevo_evento = (EditText)findViewById(id.comentario_nuevo_evento);
        cargarArchivo = (ImageButton)findViewById(id.ImageButtonSubirArchivo);
        addEvento = (Button)findViewById(id.agregarEvento);
        quitarImagen = (Button)findViewById(id.ButtonQuitarImagen);

        quitarImagen.setVisibility(View.INVISIBLE);

        //Obtenemos una referencia al LocationManager
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        /*latitud = String.valueOf(loc.getLatitude());
        longitud = String.valueOf(loc.getLongitude());*/
        latitud = (Double)getIntent().getExtras().getSerializable("Latitud");
        longitud = (Double)getIntent().getExtras().getSerializable("Longitud");

        // Para obtener la fecha y hora actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");
        fecha = sdf.format(c.getTime());
        hora = sdfH.format(c.getTime());


        addEvento.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateUser().execute();
            }
        });

        cargarArchivo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete la accion usando..."), 1);
            }
        });

        quitarImagen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarArchivo.setBackgroundResource(drawable.fondo_icono_add_archivo);
                archivo = "NO FILE";
                Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, "Archivo Deseleccionado", Toast.LENGTH_LONG, "error");
                toast.show();
                quitarImagen.setVisibility(View.INVISIBLE);
            }
        });




        cat_event_nuev = (Spinner) findViewById(id.cat_event_nuevo);
        cat_event_nuev.setAdapter(new MyAdapter(AgregarEvento.this, R.layout.row, strings));
        cat_event_nuev.setOnItemSelectedListener(this);

// Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
         //      R.array.pruebaa_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        //cat_event_nuev.setAdapter(adapter);
       // cat_event_nuev.setBackgroundResource(drawable.symbol_aguas_residuales);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri fileSelected = data.getData();
            archivo = getRealPathFromURI(fileSelected);
            String[] str = archivo.split("\\.");
            Log.d("tipo",str[1]);
            quitarImagen.setVisibility(View.VISIBLE);

            // Es una Imagen
            if((str[1].equals("jpg") == true) || (str[1].equals("jpeg") == true) || (str[1].equals("png") == true) || (str[1].equals("gif") == true)){
                cargarArchivo.setBackgroundResource(drawable.icono_image_added);
                Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, "Imagen Seleccionada Correctamente", Toast.LENGTH_LONG, "success");
                toast.show();
            }else
            // Es un archivo de Audio
            if((str[1].equals("mpeg4") == true) || (str[1].equals("mpeg") == true) || (str[1].equals("aac") == true) || (str[1].equals("wav") == true) || (str[1].equals("ogg") == true) || (str[1].equals("midi") == true) || (str[1].equals("x-ms-wma") == true)){
                cargarArchivo.setBackgroundResource(drawable.icono_audio_added);
                Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, "Archivo de Audio Seleccionado Correctamente", Toast.LENGTH_LONG, "success");
                toast.show();
            }else
            // Es un Video
            if((str[1].equals("mp4") == true) || (str[1].equals("x-msvideo") == true) || (str[1].equals("x-ms-wmv") == true)){
                cargarArchivo.setBackgroundResource(drawable.symbol_bote_agua);
                Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, "Video Seleccionado Correctamente", Toast.LENGTH_LONG, "success");
                toast.show();
            }else
            // Cualquier otro tipo de archivo
            {
                cargarArchivo.setBackgroundResource(drawable.icono_file_added);
                Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, "Archivo Seleccionado Correctamente", Toast.LENGTH_LONG, "success");
                toast.show();
            }

            Log.d("FILE NAME",archivo);

        }
    }



    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getApplicationContext().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       idd= parent.getSelectedItemPosition()+1;


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
        int sw = 0;

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
                params.add(new BasicNameValuePair("latitud", String.format("%.6f",latitud)+""));
                params.add(new BasicNameValuePair("longitud", String.format("%.6f",longitud)+""));
                params.add(new BasicNameValuePair("fecha", fecha));
                params.add(new BasicNameValuePair("hora", hora));
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
                    sw = 1;
                    if(archivo.equals("NO FILE") == true){
                        new HttpRequest(UPLOAD_URL, "POST")
                                .part("archivo", "file.png", new File(archivo))
                                .part("path","path/")
                                .code();
                    }


                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    message = json.getString(TAG_MESSAGE);
                    AgregarEvento.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast_Personalizado toast = new Toast_Personalizado(AgregarEvento.this, message, Toast.LENGTH_LONG, "error");
                            toast.show();
                        }
                    });
                    sw = -1;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (sw == 1){
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                AgregarEvento.this.finish();
            }
            if (file_url != null){
                Toast.makeText(AgregarEvento.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent returnIntent = new Intent();
            setResult(RESULT_OK,returnIntent);
            AgregarEvento.this.finish();

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
