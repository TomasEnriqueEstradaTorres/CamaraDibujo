package com.example.camaradibujo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Camara extends AppCompatActivity {

    private final String CARPETA_RAIZ = "misImagenesPrueba/";  // aqui es donde se guardara las fotos
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos"; // esta ruta permitira cargar la imagen desde el movil
    final int COD_SELECCIONA = 10; // codigo de tipo de accion
    final int COD_FOTO = 20;// codigo de tipo de accion
    private Button botonCargar;
    private ImageView imagen;
    private  String path;  // es la direccion donde se guardara la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        imagen = findViewById(R.id.imagemId);
        botonCargar = findViewById(R.id.btnCargarImg);
        // esto validara los permisos necesarios para usar la camara
        if(validaPermisos()){
            botonCargar.setEnabled(true);
        }else{
            botonCargar.setEnabled(false);
        }
    }


    public void onclick(View view) {
        // llama a la funcion
        cargarImagen();
    }

    private void cargarImagen() {
        // opciones a mostrar cuando se preciona el boton de la camara
        //final CharSequence[] opciones = {"Tomar Foto","Cargar Imagen","Cancelar"};
        final CharSequence[] opciones = {"Tomar Foto","Cargar Imagen"};
        // muestra las opciones por medio de una mensaje de tipo AlerDialog
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(Camara.this);
        alertOpciones.setTitle("Seleccione una Opción"); // titulo del AlerDialog
        // aqui se agregan las opciones al AlerDialog
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override // esto son las opciones al hacer click del boton camara
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia(); //llama a la funcion para tomar la foto
                }else{ // si no
                    if (opciones[i].equals("Cargar Imagen")){ // carga una imagen
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");// direccion donde se encuentran las imagenes
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show(); // muestra las opciones como AlerDialog
    }

    private void tomarFotografia() {
        // Aqui se guardara la foto, y la cargara
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();  // nos dira si fue creada la imagen
        String nombreImagen = "";
        if(isCreada == false){ // si el caso que no se ha creado la imagen
            isCreada=fileImagen.mkdirs();// Esto la creara la carpeta
        }
        if(isCreada == true){// si la imagen esta creada
            //Guardara el archivo con el siguente nombre y formato
            nombreImagen = (System.currentTimeMillis()/1000)+".jpg"; // devolvera la hora actual en milisegundos mas la extencion
        }
        // esto sera la ruta donde se guardara la imagen, File.separator = /
        path = Environment.getExternalStorageDirectory()+ File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
        File imagen = new File(path); // se guardara el archivo creado

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String authorities = getApplicationContext().getPackageName() + ".provider";
            //Uri imageUri = FileProvider.getUriForFile(this,authorities,imagen); // Desactualizado, solo funciona hasta la API 23
            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
    }


    @Override // es para los permisos
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // con esto cargamos la imagen de la galeria
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:  // es para seleccionar la foto creada
                    Uri miPath = data.getData();
                    imagen.setImageURI(miPath);
                    break;

                case COD_FOTO: // con esto permitiremos que las imagenes se almacenen en la galeria
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    // este metodo nos dira si el proceso ya termino
                                    Log.i("Ruta de almacenamiento","Path: " + path);
                                }
                            });
                    //Se crea la imagen de la foto tomada y asignarla al imageView
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
            }
        }
    }


    private boolean validaPermisos() { // validara los permisos necesarios para usar la camara
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        //esto es el permiso de escritura en la memoria
        if((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{
                    WRITE_EXTERNAL_STORAGE,CAMERA
            },100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // aqui se verifican los permisos
        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual(); // sirve para activar los permisos de manera manual en la app
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si","no"}; // opciones, puede ser mas
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getApplicationContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getApplicationContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }




}
