package com.example.camaradibujo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.UUID;

public class Retocar extends AppCompatActivity implements View.OnClickListener{

    private ImageButton negro;
    private ImageButton blanco;
    private ImageButton rojo;
    private ImageButton verde;
    private ImageButton azul;
    private static Lienzo lienzo;
    private float ppequenyo;
    private float pmediano;
    private float pgrande;
    private float pdefecto;
    private ImageButton trazo;
    private ImageButton anyadir;
    private ImageButton borrar;
    private ImageButton guardar;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ; // servira para el permiso de escritura

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retocar);

        negro = (ImageButton)findViewById(R.id.colornegro);
        blanco = (ImageButton)findViewById(R.id.colorblanco);
        rojo = (ImageButton)findViewById(R.id.colorrojo);
        verde = (ImageButton)findViewById(R.id.colorverde);
        azul = (ImageButton)findViewById(R.id.colorazul);
        trazo = (ImageButton)findViewById(R.id.trazo);
        anyadir = (ImageButton)findViewById(R.id.anyadir);
        borrar = (ImageButton)findViewById(R.id.borrar);
        guardar = (ImageButton)findViewById(R.id.guardar);

        negro.setOnClickListener(this);
        blanco.setOnClickListener(this);
        rojo.setOnClickListener(this);
        verde.setOnClickListener(this);
        azul.setOnClickListener(this);
        trazo.setOnClickListener(this);
        anyadir.setOnClickListener(this);
        borrar.setOnClickListener(this);
        guardar.setOnClickListener(this);

        lienzo = (Lienzo)findViewById(R.id.lienzo);
        // sirven para el tamaño de pincel y tamaño de borrador
        ppequenyo= 10;
        pmediano= 30;
        pgrande= 50;

        pdefecto= pmediano; // es el tamñano or defecto con que inicia el pincel u borrador

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
            case R.id.guardar:  // esto es para guardar la imagen

                AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(this);
                salvarDibujo.setTitle("Salvar dibujo");
                salvarDibujo.setMessage("¿Salvar Dibujo a la galeria?");
                salvarDibujo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //Salvar dibujo
                        lienzo.setDrawingCacheEnabled(true);
                        //intento de salvar
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), lienzo.getDrawingCache(),
                                UUID.randomUUID().toString()+".png", "drawing");
                        //Mensaje de todo correcto
                        if(imgSaved!=null){
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "¡Dibujo salvado en la galeria!", Toast.LENGTH_SHORT);
                            savedToast.show();
                        }
                        else{
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "¡Error! La imagen no ha podido ser salvada.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
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







}
