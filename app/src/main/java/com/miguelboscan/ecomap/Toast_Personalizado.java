package com.miguelboscan.ecomap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Personal on 14/06/2015.
 */
public class Toast_Personalizado extends Toast {


    public Toast_Personalizado(Context contexto, String mensaje, int duration, String tipo) {
        super(contexto);
        this.setDuration(duration);
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if(tipo == "success"){
            view = inflater.inflate(R.layout.toast_personalizado_green, (ViewGroup) ((Activity) contexto).findViewById(R.id.toast_personalizado_green));
        }else
        if(tipo == "error"){
            view = inflater.inflate(R.layout.toast_personalizado_red, (ViewGroup) ((Activity) contexto).findViewById(R.id.toast_personalizado_red));
        }

        this.setView(view);
        TextView tv = (TextView) view.findViewById(R.id.text_toast);
        tv.setText(mensaje);
    }

}


/*  Para llamar al toast pegar este codigo
        Toast_Personalizado toast = new Toast_Personalizado(contexto, "string", duracion);
        toast.show();
 */

