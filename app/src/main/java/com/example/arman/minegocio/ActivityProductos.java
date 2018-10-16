package com.example.arman.minegocio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ActivityProductos extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView image;

    int PICK_IMAGE_REQUEST = 111;
    String URL ="https://dp2018.000webhostapp.com/subir.php";
    Bitmap bitmap;
    ProgressDialog progressDialog;

    EditText nombre;
    EditText preciodeventa;
    EditText cantidad;
    EditText preciodecosto;
    EditText descripcion;
    String fotografia="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image = (ImageView)findViewById(R.id.imageView2);

        nombre=(EditText)findViewById(R.id.nombre);
        preciodeventa=(EditText)findViewById(R.id.preciodeventa);
        cantidad=(EditText)findViewById(R.id.cantidad);
        preciodecosto=(EditText)findViewById(R.id.preciodecosto);
        descripcion=(EditText) findViewById(R.id.descripcion);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                String[] opciones={"Tomar foto","Galeria"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProductos.this);
                builder.setTitle("Seleciona")

                        .setItems(opciones, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which==1)
                                {
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_PICK);
                                    startActivityForResult(Intent.createChooser(intent, "Selecciona la Imagen"), PICK_IMAGE_REQUEST);
                                }
                                if(which==0)
                                {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                            }
                        });
                builder.show();

            }
        });

    }
    public void Alerta(String Titulo,String Mensaje)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ActivityProductos.this).create();
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_activity_productos,menu);
        return true;
    }

    public void guardar()
    {
        //     Alerta();

        progressDialog = new ProgressDialog(ActivityProductos.this);
        progressDialog.setMessage("Guardando los datos , sea paciente...");
        progressDialog.show();

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //  Alerta("Retorno","s="+s);
                String status="";
                String jsonString = s;
                try {
                    JSONObject json = new JSONObject(jsonString);
                    status=json.getString("Status");
                } catch (Exception e) {
                    e.printStackTrace();
                }

               // Toast.makeText(ActivityProductos.this, s, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                if(status.equals("OK")){
                    Toast.makeText(ActivityProductos.this, "Producto subido con exito", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ActivityProductos.this, "Ocurrio algun error!", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ActivityProductos.this, "Algun error ocurrio!! -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                parameters.put("nombre",nombre.getText().toString());
                parameters.put("preciodecosto",preciodecosto.getText().toString());
                parameters.put("cantidad",cantidad.getText().toString());
                parameters.put("preciodeventa",preciodeventa.getText().toString());
                parameters.put("descripcion",descripcion.getText().toString());
                parameters.put("fotografia",fotografia);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(ActivityProductos.this);
        rQueue.add(request);
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_correcto) {
            // Toast.makeText(ActivityProductos.this, "Action clicked", Toast.LENGTH_LONG).show();
            Toast.makeText(ActivityProductos.this, "Fotografia= "+fotografia, Toast.LENGTH_LONG).show();;
            if(fotografia.isEmpty())
            {
                Toast.makeText(ActivityProductos.this, "No se ha capturado la imagen del producto ", Toast.LENGTH_LONG).show();;
            }
            else
            {
                guardar();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fotografia = "F"+ System.currentTimeMillis() + ".jpg";
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bitmap=imageBitmap;
            image.setImageBitmap(imageBitmap);
            fotografia = "F"+ System.currentTimeMillis() + ".jpg";
            //   saveImageFile(imageBitmap);

        }
    }
}
