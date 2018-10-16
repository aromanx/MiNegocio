package com.example.arman.minegocio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Producto {

    public Producto(JSONObject objetoJSON) {
        try {
            idproducto = objetoJSON.getInt("idProducto");
            nombre = objetoJSON.getString("nombre").toString();
            descripcion = objetoJSON.getString("descripcion".toString());
            fotografia = objetoJSON.getString("fotografia").toString();
            cantidad = objetoJSON.getInt("cantidad");
            preciodecosto=objetoJSON.getDouble("preciodecosto");
            preciodeventa=objetoJSON.getDouble("preciodeventa");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int idproducto;
    public String nombre;
    public String descripcion;
    public String fotografia;
    public int cantidad;
    public double preciodecosto;
    public double preciodeventa;

}