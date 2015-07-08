package com.miguelboscan.ecomap;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 05/06/2015.
 */

public class Mapa extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener,  GoogleMap.OnMapClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng UPV;
    private ImageButton addEvento;
    private Circle circle;
    private String TituloEvento, CategoriaEvento;
    private static String url_eventos = "http://eco-map.esy.es/BD_Function/getEventos.php";

    JSONParser jParser = new JSONParser();      // Convierte los datos recibidos en formato JSON
    JSONArray eventos = null;                   // Guarda el JSON de los eventos extraidos
    JSONArray comentarios = null;               // Guarda el JSON de los comentarios extraidos

    private static final String TAG_SUCCESS = "success";                    // Es una bandera que representa si la extraccion

    private static final String TAG_OBJETO_EVENTOS = "eventos";
    private static final String TAG_ID = "id";
    private static final String TAG_TITULO = "titulo";
    private static final String TAG_LAT = "latitud";
    private static final String TAG_LON = "longitud";
    private static final String TAG_CATEGORIA = "categoria";
    private static final String TAG_CATEGORIA_TIT = "categoria_tit";

    private static final String TAG_OBJETO_COMENTARIOS = "comentarios";
    private static final String TAG_COMENTARIO = "comentario";
    private static final String TAG_FECHA_HORA = "fechahora";
    private static final String TAG_ID_EVENTO_COMEN = "id_eventos";
    private static final String TAG_ARCHIVO = "archivo";

    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    private ProgressDialog pDialog;
    private MarkerOptions[] optionMarker;
    private Double latitudCentro, longitudCentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        setUpMapIfNeeded();
        addEvento = (ImageButton)findViewById(R.id.ImageButton_Add_Evento);

        addEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setStrokeColor(getResources().getColor(R.color.md_transparent));
                mMap.setOnMapClickListener(Mapa.this);
                mMap.setOnMyLocationChangeListener(Mapa.this);
                circle.setStrokeColor(getResources().getColor(R.color.md_light_green_700));
                Toast_Personalizado toast = new Toast_Personalizado(Mapa.this, "Toca la posición en el mapa donde deseas agregar el evento!", Toast.LENGTH_LONG, "success");
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                toast.show();
            }
        });
    }

    /**
     *
     * @param requestCode Codigo por medio del cual se van a recibir los datos de la actividad Hijo
     * @param resultCode Codigo que
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mMap.setOnMyLocationChangeListener(Mapa.this);
                circle.setStrokeColor(getResources().getColor(R.color.md_transparent));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        mMap.setOnMarkerClickListener(this);

        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    public void AddMarker(MarkerOptions mar){
        Log.d("Titulo Marker",mar.getTitle());
        mMap.addMarker(mar);
    }

    /**
     *
     * @param location Recibe La posicion actual del dispositivo
     */
    @Override
    public void onMyLocationChange(Location location) {
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
                        location.getLongitude())).zoom(16).build());
        mMap.moveCamera(myLoc);
        mMap.setOnMyLocationChangeListener(null);
        latitudCentro = location.getLatitude();
        longitudCentro = location.getLongitude();
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(300)
                .strokeColor(getResources().getColor(R.color.md_transparent))
                .fillColor(getResources().getColor(R.color.md_transparent)));
        new getEventos().execute();

    }

    /**
     *
     * @param marker Recibe el marcador pulsado
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        String tit, cat;
        TituloEvento = marker.getSnippet();
        CategoriaEvento = marker.getTitle();
        final DialogFragment dialogoPreseleccionEvento = new Preseleccion_Evento();
        dialogoPreseleccionEvento.show(Mapa.this.getFragmentManager(), "Preseleccion_Evento");


        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        /*Toast.makeText(this,
                "Lat: " + latLng.latitude +
                "Lon: " + latLng.longitude,
                Toast.LENGTH_SHORT).show();*/

        Double latitudCentro, longitudCentro, latitudSeleccionada, longitudSeleccionada, radio, Calculo;
        latitudSeleccionada = latLng.latitude;
        longitudSeleccionada = latLng.longitude;
        latitudCentro = circle.getCenter().latitude;
        longitudCentro = circle.getCenter().longitude;
        radio = circle.getRadius();

        Calculo = 6371 * Math.acos( Math.cos( Math.toRadians( latitudCentro ) ) * Math.cos( Math.toRadians( latitudSeleccionada ) ) * Math.cos( Math.toRadians( longitudSeleccionada ) - Math.toRadians( longitudCentro ) ) + Math.sin( Math.toRadians( latitudCentro ) ) * Math.sin( Math.toRadians( latitudSeleccionada ) ) );



        if (Calculo*1000 < radio){
            Intent intent = new Intent(Mapa.this, AgregarEvento.class);
            intent.putExtra("Latitud", latitudSeleccionada);
            intent.putExtra("Longitud", longitudSeleccionada);
            startActivityForResult(intent,1);
        }else{
            Toast_Personalizado toast = new Toast_Personalizado(Mapa.this, "No puedes agregar un evento fuera del área permitida!!", Toast.LENGTH_LONG, "error");
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

    }
    public class Preseleccion_Evento extends DialogFragment {

        private Button VerEventos;
        private TextView tituloEvento, categoriaEvento;
        private ImageView imagen;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.evento_preseleccionado, container);
            getDialog().requestWindowFeature(STYLE_NO_TITLE);

            // Se encuentran los objetos necesarios del linearLayout evento_preseleccionado.xml
            VerEventos = (Button)view.findViewById(R.id.BotonVerEvento);
            tituloEvento = (TextView)view.findViewById(R.id.titulo_Evento);
            categoriaEvento = (TextView)view.findViewById(R.id.categoria_evento);

            tituloEvento.setText(TituloEvento);
            categoriaEvento.setText(CategoriaEvento);

            // Accion a ralizar cuando se presione el boton Sobrescribir
            VerEventos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            getDialog().setCanceledOnTouchOutside(true);
            return view;
        }
    }



    class getEventos extends AsyncTask<String, String, String> {


        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Mapa.this);
            pDialog.setMessage("Cargando Eventos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            params.add(new BasicNameValuePair("latitud", String.format("%.6f",latitudCentro)+""));
            params.add(new BasicNameValuePair("longitud", String.format("%.6f",longitudCentro)+""));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_eventos, "POST", params);


            // Check your log cat for JSON reponse
            System.out.print("EVENTOS Y COMENTARIOS: " + json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    eventos = json.getJSONArray(TAG_OBJETO_EVENTOS);
                    comentarios = json.getJSONArray(TAG_OBJETO_COMENTARIOS);
                    optionMarker = new MarkerOptions[eventos.length()];

                    // looping through All Products
                    Log.d("Tam Evento",eventos.length()+"");
                    Log.d("Tam Comentario",comentarios.length()+"");
                    for (int i = 0; i < eventos.length(); i++) {
                        JSONObject ev = eventos.getJSONObject(i);
                        JSONObject cm = comentarios.getJSONObject(i);
                        Log.d("Evento ", i+ " :"+ev.toString());

                        // Storing each json item in variable
                        int id = ev.getInt(TAG_ID);
                        Double lat = Double.parseDouble(ev.getString(TAG_LAT));
                        Double lon = Double.parseDouble(ev.getString(TAG_LON));
                        String titulo = ev.getString(TAG_TITULO);
                        int categoria = ev.getInt(TAG_CATEGORIA);
                        String categoria_tit = ev.getString(TAG_CATEGORIA_TIT);
                        String comentario = cm.getString(TAG_COMENTARIO);

                        UPV = new LatLng(lat, lon);

                        int icono=0;
                        switch (categoria) {
                            case 1:
                                icono = R.drawable.symbol_bote_agua;
                                break;
                            case 2:
                                icono = R.drawable.symbol_aguas_residuales;
                                break;
                            case 3:
                                icono = R.drawable.symbol_basura;
                                break;
                            case 4:
                                icono = R.drawable.symbol_charlas;
                                break;
                            case 5:
                                icono = R.drawable.symbol_contaminacion_industrial;
                                break;
                            case 6:
                                icono = R.drawable.symbol_contaminacion_sonica;
                                break;
                            case 7:
                                icono = R.drawable.symbol_deforestacion;
                                break;
                            case 8:
                                icono = R.drawable.symbol_zona_acampar;
                                break;
                            case 9:
                                icono = R.drawable.symbol_derrumbe;
                                break;
                            case 10:
                                icono = R.drawable.symbol_desarrollo_urbano;
                                break;
                            case 11:
                                icono = R.drawable.symbol_desechos;
                                break;
                            case 12:
                                icono = R.drawable.symbol_incendio;
                                break;
                            case 13:
                                icono = R.drawable.symbol_inundacion;
                                break;
                            case 14:
                                icono = R.drawable.symbol_paisaje;
                                break;
                            case 15:
                                icono = R.drawable.symbol_playa;
                                break;
                            case 16:
                                icono = R.drawable.symbol_rio;
                                break;

                            default:
                                break;
                        }
                        optionMarker[i] = new MarkerOptions()
                                .position(UPV)
                                .title(categoria_tit)
                                .snippet(titulo)
                                .icon(BitmapDescriptorFactory.fromResource(icono))
                                .anchor(0.5f, 0.5f);
                        Log.d("Dentro del For del pre",i+" :"+optionMarker[i].getTitle());

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            MarkerOptions m;
            pDialog.dismiss();
            Log.d("Tam M1",optionMarker.length+"");
            for (int i = 0; i < optionMarker.length; i++) {
                Log.d("Dentro del For", i + " :" + optionMarker[i].getTitle());
                mMap.addMarker(optionMarker[i]);
            }
        }
    }
}