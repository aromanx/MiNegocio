package com.example.arman.minegocio;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProductos extends Fragment  {

    private static List<Producto> productos;

    private static RecyclerView ListaDeProductos;
    private static RecyclerView.Adapter mAdapter;
  // private  RecyclerView.LayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager;

    static View.OnClickListener myOnClickListener;

    public FragmentProductos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("Right", "onCreate()");

      // myOnClickListener = new MyOnClickListener(getActivity());

/*       productos= new ArrayList<Producto>();

        myOnClickListener = new MyOnClickListener(getActivity());

       ListaDeProductos= getActivity().findViewById(R.id.lista);

        if(ListaDeProductos!=null) {
            ListaDeProductos.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

         ListaDeProductos.setLayoutManager(mLayoutManager);

        productos=new ArrayList<>();

        CargarProductos();

        mAdapter = new ProductoAdapter(getActivity(),productos);

        ListaDeProductos.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
*/
        productos=new ArrayList<>();


            CargarProductos();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fragment_productos, container, false);
        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.lista);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //ProductoAdapter adapter = new ProductoAdapter(getActivity(), productos);
         mAdapter = new ProductoAdapter(getActivity(), productos);
        recyclerView.setAdapter(mAdapter);


        return  view;
    }
/*
    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            //removeItem(v);
            int selectedItemPosition = ListaDeProductos.getChildAdapterPosition(v);
            Toast.makeText(v.getContext(), "You Clicked at " + selectedItemPosition, Toast.LENGTH_SHORT).show();
        }
    }
*/
    public void CargarProductos()
    {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://dp2018.000webhostapp.com/listarproductos.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String json = response;


                        try {

                            JSONObject object = new JSONObject(json); //Creamos un objeto JSON a partir de la cadena
                            JSONArray json_array = object.optJSONArray("records"); //cogemos cada uno de los elementos dentro de la etiqueta "frutas"

                            if(json_array.length()>0) {

                                for (int i = 0; i < json_array.length(); i++) {
                                    //s=s+json_array.getJSONObject(i).getString("nombre").toString()+"\n";
                                    productos.add(new Producto(json_array.getJSONObject(i))); //creamos un objeto Fruta y lo insertamos en la lista

                                }

                                //  Alerta("Registros ==",""+productos.size());
                                mAdapter.notifyDataSetChanged();

                                // ListaDeProductos.invalidateViews();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Alerta("Error","Al cargar los datos del servidor!!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void Alerta(String Titulo,String Mensaje)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(Titulo);
        alertDialog.setMessage(Mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
/*
    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            //removeItem(v);
            int p=v.
           int selectedItemPosition = ListaDeProductos.getChildLayoutPosition(v);
            Toast.makeText(this.context, "You Clicked at " , Toast.LENGTH_SHORT).show();
        }
    }
*/


}

