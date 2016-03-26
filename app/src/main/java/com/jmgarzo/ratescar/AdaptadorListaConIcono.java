package com.jmgarzo.ratescar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmgarzo.objects.ListaConIcono;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 2/02/15.
 */
public class AdaptadorListaConIcono extends ArrayAdapter<ListaConIcono> {

    Activity context;
    private ArrayList<ListaConIcono> lista;


    AdaptadorListaConIcono(Activity context, ArrayList<ListaConIcono> lista) {

        super(context, R.layout.lista_con_icono, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder;


        if (item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.lista_con_icono, null);

            holder = new ViewHolder();
            holder.icono = (ImageView) item.findViewById(R.id.ImgIconoListaConIcono);
            holder.titulo = (TextView) item.findViewById(R.id.lbTituloListaConIcono);
            holder.subtitulo = (TextView) item.findViewById(R.id.lbSubtituloListaConIcono);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.icono.setImageResource(lista.get(position).getIcono());
        holder.titulo.setText(lista.get(position).getTitulo());
        holder.subtitulo.setText(lista.get(position).getSubtitulo());


        return (item);
    }

    static class ViewHolder {
        ImageView icono;
        TextView titulo;
        TextView subtitulo;

    }
}





