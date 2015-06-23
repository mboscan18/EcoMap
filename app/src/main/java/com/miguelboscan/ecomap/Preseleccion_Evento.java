package com.miguelboscan.ecomap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Miguel on 23/06/2015.
 */

public class Preseleccion_Evento extends DialogFragment {

    private Button VerEventos;
    private TextView tituloEvento, categoriaEvento;
    private ImageView imagen;
    private String titulo, categoria;

    public void Preseleccion_Evento(String tit, String cat){
        titulo = tit;
        categoria = cat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.evento_preseleccionado, container);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        // Se encuentran los objetos necesarios del linearLayout evento_preseleccionado.xml
        VerEventos = (Button)view.findViewById(R.id.BotonVerEvento);
        tituloEvento = (TextView)view.findViewById(R.id.titulo_Evento);
        categoriaEvento = (TextView)view.findViewById(R.id.categoria_evento);

        tituloEvento.setText(titulo);
        categoriaEvento.setText(categoria);

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

/*
----------- PARA LLAMAR EL DIALOGO EN OTRA CLASE PEGAR EL SIGUIENTE CODIGO "-----------

        // * * Inicio seccion de codigo del dialogo del Confirmacion * * //

        final DialogFragment dialogoConfirmarGuardarTest = new Preseleccion_Evento(titulo, categoria);
        dialogoConfirmarGuardarTest.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FondoTransparente);
        dialogoConfirmarGuardarTest.show(getSupportFragmentManager(), "Preseleccion_Evento");

        // * * Final seccion de codigo del dialogo del Confirmacion * * //

 */
