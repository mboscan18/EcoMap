package com.miguelboscan.ecomap;

/**
 * Created by HP on 25/06/15.
 */

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class VerEvent extends FragmentActivity{

    private ScrollView scrollView;
    private LinearLayout lay;
    private LinearLayout info;


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> empresaList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ver_evento);


        // Se encuentran los objetos a utilizar del frameLayout historial.xml
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        lay = (LinearLayout)findViewById(R.id.comentarioNu);





        // ----------------- SE SETEAN LAS DIMENSIONES DE LOS ELEMENTOS CON LOS CUALES TRABAJAREMOS ----------------- //

        // Tamao del Lauyout Contenedor
        final LinearLayout.LayoutParams parametroLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Tamao del TextView fecha
        final TableRow.LayoutParams parametroTextViewFecha = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, 55
        );


        //  del FrameLayout divisor
        final FrameLayout.LayoutParams parametroFrameDivisor = new FrameLayout.LayoutParams(
                4, FrameLayout.LayoutParams.MATCH_PARENT
        );
        parametroFrameDivisor.setMargins(0,10,0,10);



        String[ ] nombre = {"Mara", "Gerson","hola","mierda","nawevona","cabezona","hoa"};
        String[ ] fecha = {"hoy","maana","ayer","nunca","no exite","mielda","lloco"};
        int cantidad=7;
        int test=0;
        String tituloTest = "";
        String resultado = "";
        String opcion = "";

        // ----------------- Se declaran los arreglos de TextView y LinearLayout ----------------- //

        LinearLayout[] layo = new LinearLayout[cantidad];
        TextView tv[] = new TextView[cantidad];


          for (int i = 0; i < cantidad; i++) {

              // <LinearLayout>
              layo[i] = new LinearLayout(this);
              layo[i].setOrientation(LinearLayout.VERTICAL);
              layo[i].setLayoutParams(parametroLayout);

              // <TextView>
              tv[i] = new TextView(this);
              tv[i].setLayoutParams(parametroTextViewFecha);
              tv[i].setText("Comentario: - " + fecha[i]);
              tv[i].setTextColor(getResources().getColor(R.color.md_white_1000));
              tv[i].setBackgroundColor(getResources().getColor(R.color.md_pink_100));
              tv[i].setTextSize(20);
              tv[i].setTypeface(Typeface.create("Verdana", Typeface.NORMAL));
              tv[i].setGravity(Gravity.CENTER_VERTICAL);
              tv[i].setPadding(20, 0, 0, 0);
              // </TextView>

              layo[i].addView(tv[i]);     // Se  el TextView al Layout principal

              lay.addView(layo[i]);       // Se  al Layout principal a la pantalla


          }  //-- FIN FOR --//


        }



    }

