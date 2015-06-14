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


    public Toast_Personalizado(Context contexto, String mensaje, int duration) {
        super(contexto);
        this.setDuration(duration);
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_personalizado, (ViewGroup) ((Activity) contexto).findViewById(R.id.toast_personalizado));
        this.setView(view);
        TextView tv = (TextView) view.findViewById(R.id.text_toast);
        tv.setText(mensaje);
    }

}


/*  Para llamar al toast pegar este codigo
        Toast_Personalizado toast = new Toast_Personalizado(contexto, "string", duracion);
        toast.show();
 */

