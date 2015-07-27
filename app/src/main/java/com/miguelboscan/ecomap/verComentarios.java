package com.miguelboscan.ecomap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 01/07/15.
 */
public class verComentarios extends FragmentActivity implements View.OnClickListener {


// Progress Dialog
private ProgressDialog pDialog;

// Creating JSON Parser object
JSONParser jParser = new JSONParser();
    JSONParser jsonParser = new JSONParser();

ArrayList<HashMap<String, String>> empresaList;
    ArrayList<String> empresaList2;


// url to get all products list
private static String url_all_empresas = "http://eco-map.esy.es/BD_Function/getComments.php";
    private static String REGISTER_URL = "http://eco-map.esy.es/BD_Function/addComments.php";

// JSON Node names
private static final String TAG_SUCCESS = "success";
private static final String TAG_PRODUCTS = "comentarios";
private static final String TAG_ID = "comentario";
private static final String TAG_NOMBRE = "fechahora";
    //ids

    private static final String TAG_MESSAGE = "message";


// products JSONArray
     private  EditText comentario_ver;
JSONArray products = null;
    private ScrollView scrollView;
    private LinearLayout lay;
    private LinearLayout info;
    private Button addComent;
    private String archivo, fecha, hora, categoria, titulo;
    private int idevento;
    private TextView category, title;


ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_evento_up);


        // Se encuentran los objetos a utilizar del frameLayout historial.xml
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        lay = (LinearLayout)findViewById(R.id.comentarioNu);
        addComent = (Button)findViewById(R.id.addComent);
        comentario_ver = (EditText)findViewById(R.id.comenta);
        category = (TextView)findViewById(R.id.categoria_evento_up);
        title = (TextView)findViewById(R.id.titulo_Evento_up);

        idevento = (int)getIntent().getExtras().getSerializable("evento");
        categoria = (String)getIntent().getExtras().getSerializable("categoria");
        titulo = (String)getIntent().getExtras().getSerializable("titulo");

        EditText EtOne = (EditText) findViewById(R.id.comenta);
        EtOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.comenta) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        category.setText(categoria);
        title.setText(titulo);

        // Para obtener la fecha y hora actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");
        fecha = sdf.format(c.getTime());
        hora = sdfH.format(c.getTime());

        addComent.setOnClickListener(this);
        // Hashmap para el ListView
        empresaList = new ArrayList<HashMap<String,String>>();

        empresaList2= new ArrayList<String>();
        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listAllProducts);


    }//fin onCreate

    @Override
    public void onClick(View v) {
        new AddComenta().execute();
    }


    class LoadAllProducts extends AsyncTask<String, String, String> {

    /**
     * Antes de empezar el background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(verComentarios.this);
        pDialog.setMessage("Cargando elementos del Servidor");
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

        // getting JSON string from URL
        params.add(new BasicNameValuePair("id_evento", idevento+""));
        pDialog.setMessage("All Products: ");
       JSONObject json = jParser.makeHttpRequest(url_all_empresas, "POST", params);

        // Check your log cat for JSON reponse
//        System.out.print("All Products: " + json.toString());
        Log.d("All productos", json.toString());


        try {
            // Checking for SUCCESS TAG

           int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                //Log.i("ramiro", "produtos.length" + products.length());
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String fecha = c.getString(TAG_NOMBRE);

                    String coment = c.getString(TAG_ID);


                    // creating new HashMap
                    HashMap map = new HashMap();

                    // adding each child node to HashMap key => value
                    map.put(TAG_ID, coment);
                    map.put(TAG_NOMBRE, fecha);
                    empresaList2.add(coment);
                    empresaList.add(map);
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

        // updating UI from Background Thread

                pDialog.dismiss();
                // ----------------- SE SETEAN LAS DIMENSIONES DE LOS ELEMENTOS CON LOS CUALES TRABAJAREMOS ----------------- //

                // Tamao del Lauyout Contenedor
                final LinearLayout.LayoutParams parametroLayout = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Tamao del TextView fecha
                final TableRow.LayoutParams parametroTextViewFecha = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0.7f
                );


                // Tama del FrameLayout divisor
                final FrameLayout.LayoutParams parametroFrameDivisor = new FrameLayout.LayoutParams(
                        4, FrameLayout.LayoutParams.MATCH_PARENT
                );
                parametroFrameDivisor.setMargins(0, 10, 0, 10);


                // ----------------- Se declaran los arreglos de TextView y LinearLayout ----------------- //
                          int cantidad=50;
                LinearLayout[] layo = new LinearLayout[empresaList.size()];
                TextView tv[] = new TextView[empresaList.size()];


                for (int i = 0; i < empresaList.size(); i++) {

                    // <LinearLayout>
                    layo[i] = new LinearLayout(verComentarios.this);
                    layo[i].setOrientation(LinearLayout.HORIZONTAL);
                    layo[i].setLayoutParams(parametroLayout);

                    // <TextView>
                    tv[i] = new TextView(verComentarios.this);
                    tv[i].setLayoutParams(parametroTextViewFecha);
                    tv[i].setText(empresaList.get(i).get(TAG_NOMBRE) + " | " + empresaList.get(i).get(TAG_ID));
                    tv[i].setTextColor(getResources().getColor(R.color.md_text));
                    tv[i].setBackgroundColor(getResources().getColor(R.color.md_green_50));
                    tv[i].setTextSize(18);
                    tv[i].setTypeface(Typeface.create("Verdana", Typeface.NORMAL));
                    tv[i].setGravity(Gravity.CENTER_VERTICAL);
                    tv[i].setPadding(20, 0, 0, 0);
                    // </TextView>

                    layo[i].addView(tv[i]);     // Se  el TextView al Layout principal

                    lay.addView(layo[i]);       // Se al Layout principal a la pantalla


                }  //-- FIN FOR --//



}
}
    class AddComenta extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(verComentarios.this);
            pDialog.setMessage("Agregando..");

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

            String comentario = comentario_ver.getText().toString();


                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("id_evento", idevento+""));
                params.add(new BasicNameValuePair("comentario", comentario));
                params.add(new BasicNameValuePair("fecha", fecha));
                params.add(new BasicNameValuePair("hora", hora));
                params.add(new BasicNameValuePair("archivo", "La direccion"));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());
            try {
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.getString(TAG_MESSAGE).toString());


                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent returnIntent = new Intent();
            setResult(RESULT_OK,returnIntent);
            verComentarios.this.finish();


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}



