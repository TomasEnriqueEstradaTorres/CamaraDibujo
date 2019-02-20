package com.example.camaradibujo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        // oculta el actionBar
        //getActionBar().hide();  //solo usar con extends Activity
        getSupportActionBar().hide();  // solo usar con extends AppCompatActivity

        // retardo de tiempo para la presentacion
        new Handler() .postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Bienvenida.this, MainActivity.class);
                startActivity(intent);
            }
        }, 1000); // Esto de aqui sera para que la pantalla aparesca un tiempo determinado y luego pase a la siguiente pantalla.
    }
}
