package com.example.moneymapp.pasivos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mentorapp.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Modelo> {

    private List<Modelo> mList;
    private Context mContext;
    private int resourceLayout;

    public ListAdapter(@NonNull Context context, int resource, List<Modelo> objects) {
        super(context, resource, objects);
        this.mList=objects;
        this.mContext=context;
        this.resourceLayout=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= convertView;

        if (view==null)
            view= LayoutInflater.from(mContext).inflate(resourceLayout,null);

        Modelo modelo = mList.get(position);

        TextView textoNombre= view.findViewById(R.id.txvNombrep);
        textoNombre.setText(modelo.getNombre());

        TextView textoMonto= view.findViewById(R.id.txvMonto);
        textoMonto.setText(modelo.getMonto());

        return view;
    }
}
