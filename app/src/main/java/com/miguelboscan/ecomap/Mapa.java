package com.miguelboscan.ecomap;

import android.app.DialogFragment;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mapa extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng UPV, UPV2;
    private ImageButton addEvento;
    private Circle circle;
    private String TituloEvento, CategoriaEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        setUpMapIfNeeded();
        addEvento = (ImageButton)findViewById(R.id.ImageButton_Add_Evento);

        addEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setOnMapClickListener(Mapa.this);

                mMap.setOnMyLocationChangeListener(Mapa.this);
                circle.setStrokeColor(getResources().getColor(R.color.md_light_green_700));
                Toast_Personalizado toast = new Toast_Personalizado(Mapa.this, "Toca la posición en el mapa donde deseas agregar el evento!", Toast.LENGTH_LONG, "success");
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                toast.show();
            }
        });
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
      //  mMap.setMyLocationEnabled(true);

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        mMap.setOnMarkerClickListener(this);

        UPV = new LatLng(8.282069, -62.727008);
        UPV2 = new LatLng(8.282625, -62.725234);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        MarkerOptions m1 = new MarkerOptions()
                .position(UPV)
                .title("Bote de Basura")
                .snippet("Acumulacion de basura en Conjunto residencil Doña Emilia")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bolsa))
                .anchor(0.5f, 0.5f);

        MarkerOptions m2 = new MarkerOptions()
                .position(UPV2)
                .title("Bote de Agua")
                .snippet("Derrame de agua cerca de Los Mangos")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.grifo))
                .anchor(0.5f, 0.5f);
        Marker mar1, mar2;

        mar1 = mMap.addMarker(m1);
        mar2 = mMap.addMarker(m2);
    }

    /**
     *
     * @param location Recibe La posicion actual del dispositivo
     */
    @Override
    public void onMyLocationChange(Location location) {
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
                        location.getLongitude())).zoom(17).build());
        mMap.moveCamera(myLoc);
        mMap.setOnMyLocationChangeListener(null);
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(300)
                .strokeColor(getResources().getColor(R.color.md_transparent))
                .fillColor(getResources().getColor(R.color.md_transparent)));

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
            Toast.makeText(this,
                    "Tocaste dentro del circulo",
                    Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Mapa.this, AgregarEvento.class);
                    startActivity(intent);
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
}