package com.example.arman.minegocio;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private final Activity context;
    private static List<Producto> productoList;

    //Provide a reference to the views for each data item
    //Complex data items may need more than one view per item, and
    //you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        //each data item is just a string in this case
        public TextView nombre,descripcion,precio;
        public ImageView imagen;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView)v.findViewById(R.id.Nombre);
            descripcion = (TextView) v.findViewById(R.id.Descripcion);
            precio = (TextView) v.findViewById(R.id.Precio);
            imagen=(ImageView)v.findViewById(R.id.imgProducto);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        Producto clickedDataItem =productoList.get(pos);
                      //  Toast.makeText(v.getContext(), "You clicked "+clickedDataItem.nombre, Toast.LENGTH_SHORT).show();
                        String[] opciones={"Editar","Eliminar"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Seleciona")

                                .setItems(opciones, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // The 'which' argument contains the index position
                                        // of the selected item
                                        if(which==1)
                                        {

                                        }
                                        if(which==0)
                                        {

                                        }
                                    }
                                });
                        builder.show();
                    }
                }
            });

        }
    }

    //Provide a suitable constructor
    public ProductoAdapter(Activity context,List<Producto> productoList){
        this.context=context;
        this.productoList = productoList;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);

        //set the view's size, margins, paddings and layout parameters
       // v.setOnClickListener(FragmentProductos.myOnClickListener);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Replace the contents of a view (invoked by the layout manager
    @Override
    public void onBindViewHolder(ProductoAdapter.ViewHolder holder, int position) {

        // - get element from arraylist at this position
        // - replace the contents of the view with that element

        Producto producto = productoList.get(position);
        holder.nombre.setText(producto.nombre);
        holder.descripcion.setText(producto.descripcion);
        holder.precio.setText(String.valueOf(producto.preciodecosto));

        // holder.imagen.setImageResource(R.drawable.iconousuario);

        Picasso.with(context).load("https://dp2018.000webhostapp.com/upload/images/"+producto.fotografia).into(holder.imagen);

    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }
}
