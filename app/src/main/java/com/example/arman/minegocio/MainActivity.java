package com.example.arman.minegocio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public class Usuarios
    {
        public int idpersona;
        public String nombre;
        public String usuario;
        public String contrasena;

        public Usuarios(JSONObject objetoJSON) {
            try {
                idpersona = objetoJSON.getInt("idPersona");
                nombre = objetoJSON.getString("nombre");
                usuario = objetoJSON.getString("usuario");
                contrasena = objetoJSON.getString("contrasena");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    List<Usuarios> listausuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefe=getSharedPreferences("datos",Context.MODE_PRIVATE);
        EditText usuario=(EditText)findViewById(R.id.editText);
        EditText contrasena=(EditText)findViewById(R.id.editText2);
        CheckBox c1=(CheckBox)findViewById(R.id.checkBox);

        usuario.setText(prefe.getString("usuario",""));
        contrasena.setText(prefe.getString("contrasena",""));

        if(!usuario.getText().toString().isEmpty())
        {
            c1.setChecked(true);
        }
    }

    public void Alerta(String titulo,String mensaje)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensaje);
        alertDialog.setIcon(R.drawable.iconousuario);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
    void Login(final View v)
    {
        EditText usuario=(EditText)findViewById(R.id.editText);
        EditText contrasena=(EditText)findViewById(R.id.editText2);
        listausuarios= new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://dp2018.000webhostapp.com/logina.php?usuario="+usuario.getText().toString()+"&contrasena="+contrasena.getText().toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String json = response;
                        try {
                            CheckBox c1=(CheckBox)findViewById(R.id.checkBox);
                            JSONObject object = new JSONObject(json); //Creamos un objeto JSON a partir de la cadena
                            JSONArray json_array = object.optJSONArray("records"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"
                            if(json_array.length()>0) {
                                for (int i = 0; i < json_array.length(); i++) {
                                    listausuarios.add(new Usuarios(json_array.getJSONObject(i))); //creamos un objeto Fruta y lo insertamos en la lista
                                }

                                Usuarios usuario= listausuarios.get(0);
                                if(c1.isChecked())
                                {
                                    SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferencias.edit();
                                    editor.putString("usuario", usuario.usuario);
                                    editor.putString("contrasena", usuario.contrasena);
                                    editor.commit();
                                }
                                else
                                {
                                    SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferencias.edit();
                                    editor.putString("usuario", "");
                                    editor.putString("contrasena","");
                                    editor.commit();
                                }
                                // Alerta("Nombre=",usuario.nombre);
                                Intent i = new Intent(MainActivity.this, Main2Activity.class );
                                startActivity(i);
                                // MenuPrincipal(v);
                            }else
                                Alerta("Lo siento","No se encontro el usuario!!");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Alerta("Error","Error al conectarse al servidor!!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
