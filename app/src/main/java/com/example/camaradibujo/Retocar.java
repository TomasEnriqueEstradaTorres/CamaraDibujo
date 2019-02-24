package com.example.camaradibujo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class Retocar extends AppCompatActivity implements View.OnClickListener{

    private ImageButton negro, blanco, rojo, verde, azul; //Botones para los colores
    private ImageButton texto, punto, triangulo; // es para las figuras y texto en lienzo
    private static Lienzo lienzo; // esto es el canvas
    private float ppequenyo, pmediano, pgrande, pdefecto; // para el tamaño del pincel o borrador
    private ImageButton trazo; // para el tamaño de pincel
    private ImageButton anyadir; // Para crear un nuevo lienzo
    private ImageButton borrar; // para borrar el lienzo (canvas) por medio del pincel(tamaño de borrador)
    private ImageButton guardar; // para guardar la imagen retocada
    private ImageButton cargarImagenGaleria; // para cargar una imagen desde la galeria
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ; // servira para el permiso de escritura

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retocar);

        // Botones para las figuras texto, punto y triangulo
        texto = findViewById(R.id.texto);
        punto = findViewById(R.id.punto);
        triangulo = findViewById(R.id.triangulo);
        //Botones para los colores
        negro = findViewById(R.id.colornegro);
        blanco = findViewById(R.id.colorblanco);
        rojo = findViewById(R.id.colorrojo);
        verde = findViewById(R.id.colorverde);
        azul = findViewById(R.id.colorazul);
        //Botones para las acciones
        trazo = findViewById(R.id.trazo);
        anyadir = findViewById(R.id.anyadir);
        borrar = findViewById(R.id.borrar);
        guardar = findViewById(R.id.guardar);
        cargarImagenGaleria = findViewById(R.id.cargarImagen);
        // escucha de todos los botones
        negro.setOnClickListener(this);
        blanco.setOnClickListener(this);
        rojo.setOnClickListener(this);
        verde.setOnClickListener(this);
        azul.setOnClickListener(this);
        trazo.setOnClickListener(this);
        anyadir.setOnClickListener(this);
        borrar.setOnClickListener(this);
        guardar.setOnClickListener(this);
        cargarImagenGaleria.setOnClickListener(this);
        texto.setOnClickListener(this);
        punto.setOnClickListener(this);
        triangulo.setOnClickListener(this);
        lienzo = findViewById(R.id.lienzo);  // se declara el lienzo o canvas
        // sirven para el tamaño de pincel y tamaño de borrador
        ppequenyo= 10;
        pmediano= 30;
        pgrande= 50;
        pdefecto= ppequenyo; // es el tamñano por defecto con que inicia el pincel u borrador

        // esto es para el permiso es escritura
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String color = null;

        switch (v.getId()){
            case R.id.colornegro:
                color = v.getTag().toString();// obtiene el color del archivo xml
                lienzo.setColor(color);
                break;
            case R.id.colorblanco:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.colorazul:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.colorverde:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.colorrojo:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.texto:

                // FALTA HACER PARA AGREGAR EL TEXTO

                break;

            case R.id.trazo: // aqui se eligira el tamaño del punto para el pincel
                final CharSequence[] opcionesPincel = {"Pequeño","Mediano", "Grande"}; // opciones a mostrar
                final AlertDialog.Builder tamanyopuntoPincel = new AlertDialog.Builder(Retocar.this); // para mostrar las opciones
                tamanyopuntoPincel.setTitle("Tamaño de punto para Pincel"); // esto es el titulo de las opciones
                tamanyopuntoPincel.setItems(opcionesPincel, new DialogInterface.OnClickListener() { // aqui iran las opciones a escoger
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        if (opcionesPincel[opcion].equals("Pequeño")){
                            Lienzo.setBorrado(false);
                            Lienzo.setTamanyoPunto(ppequenyo);
                            //tamanyopunto.dismiss();
                            dialog.dismiss();
                        }else if(opcionesPincel[opcion].equals("Mediano")){
                            Lienzo.setBorrado(false);
                            Lienzo.setTamanyoPunto(pmediano);
                            //tamanyopunto.dismiss();
                            dialog.dismiss();

                        }else if(opcionesPincel[opcion].equals("Grande")){
                            Lienzo.setBorrado(false);
                            Lienzo.setTamanyoPunto(pgrande);
                            //tamanyopunto.dismiss();
                            dialog.dismiss();
                        }
                    }
                });
                tamanyopuntoPincel.show();//Mostrar y esperar la interacción del usuario.
                break;

            case R.id.anyadir: // para reiniciar el lienzo o no
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("Nuevo Dibujo");
                newDialog.setMessage("¿Comenzar nuevo dibujo (perderás el dibujo actual)?");

                newDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        lienzo.NuevoDibujo();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                newDialog.show();
                break;

            case R.id.borrar: // esto es el tamaño del borrador
                final CharSequence[] opcionesBorrador = {"Pequeño","Mediano", "Grande"}; // opciones a mostrar
                final AlertDialog.Builder tamanyopuntoBorrador = new AlertDialog.Builder(Retocar.this); // para mostrar las opciones

                tamanyopuntoBorrador.setTitle("Tamaño de punto para Borrador"); // esto es el titulo de las opciones
                tamanyopuntoBorrador.setItems(opcionesBorrador, new DialogInterface.OnClickListener() { // aqui iran las opciones a escoger
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        if (opcionesBorrador[opcion].equals("Pequeño")){
                            Lienzo.setBorrado(true);
                            Lienzo.setTamanyoPunto(ppequenyo);
                            dialog.dismiss();

                        }else if(opcionesBorrador[opcion].equals("Mediano")){
                            Lienzo.setBorrado(true);
                            Lienzo.setTamanyoPunto(pmediano);
                            dialog.dismiss();

                        }else if(opcionesBorrador[opcion].equals("Grande")){
                            Lienzo.setBorrado(true);
                            Lienzo.setTamanyoPunto(pgrande);
                            dialog.dismiss();
                        }
                    }
                });
                tamanyopuntoBorrador.show(); //Mostrar y esperar la interacción del usuario.
                break;


             //-------------------------------------------------------------------------------------
             //-------------------------------------------------------------------------------------
            case R.id.cargarImagen: // servira para cargar una imagen de la galeria

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");// direccion donde se encuentran las imagenes(galeria de imagenes)
                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);



                // FALTA HACER PARA CARGAR LA IMAGEN




                break;
            //-------------------------------------------------------------------------------------
            //-------------------------------------------------------------------------------------



            case R.id.guardar:  // esto es para guardar la imagen
                AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(this);
                salvarDibujo.setTitle("Salvar dibujo");
                salvarDibujo.setMessage("¿Salvar Dibujo a la galeria?");
                salvarDibujo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        lienzo.setDrawingCacheEnabled(true);//Salvar dibujo
                        //intento de salvar
                        String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), lienzo.getDrawingCache(),UUID.randomUUID().toString()+".png", "drawing");
                        //Mensaje de todo correcto
                        if(imgSaved!=null){
                            Toast ImagenGuardada = Toast.makeText(getApplicationContext(), "¡Dibujo salvado en la galeria!", Toast.LENGTH_LONG);
                            ImagenGuardada.show();
                        }
                        else{
                            Toast imagenNoGuardada = Toast.makeText(getApplicationContext(), "¡Error! La imagen no ha podido ser salvada.", Toast.LENGTH_LONG);
                            imagenNoGuardada.show();
                        }
                        lienzo.destroyDrawingCache();
                    }
                });
                salvarDibujo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                salvarDibujo.show();
                break;

            default:
                break;
        }
    }



    @Override // es para los permisos y la accion a realizar
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // con esto cargamos la imagen de la galeria
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 10:  // es para seleccionar la foto creada
                    Uri miPath = data.getData();
                    String stringUri;
                    stringUri = miPath.toString(); // convierte el uri en string
                    //System.out.println("===> URI A STRING: " + stringUri);



                    //Log.i("===> miPath<====", " RETOCAR: " + miPath); // direccionde ubicacion
                    // content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F39/ORIGINAL/NONE/592715019


                    //PATH      => /storage/emulated/0/misImagenesPrueba/misFotos/1550976122.jpg
                    //imageUri  =>  content://com.example.camaradibujo.provider/external_files/misImagenesPrueba/misFotos/1550976367.jpg


                    //imagen.setImageURI(miPath);
                    //lienzo.cargaImagen(miPath);
                    break;
            }
        }
    }












}
