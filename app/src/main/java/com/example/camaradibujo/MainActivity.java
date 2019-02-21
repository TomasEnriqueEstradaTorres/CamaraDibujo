package com.example.camaradibujo;

import android.content.Intent;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button camaraFoto, retocarIamgen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camaraFoto = findViewById(R.id.buttonCamara);
        retocarIamgen = findViewById(R.id.buttonRetocarImagen);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void onResume() {
        super.onResume();

        camaraFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Camara.class);
                startActivity(intent);
            }
        });

        retocarIamgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Retocar.class);
                startActivity(intent);
            }
        });

    }

    public void onRestart() {
        super.onRestart();

    }


    public void onPause() {
        super.onPause();

    }

    public void onStop() {
        super.onStop();

    }

    public void onDestroy() {
        super.onDestroy();

        /*
        Toast toast = Toast.makeText(this, "Ventana 1 Estoy en onDestroy", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 1400);
        toast.show();
        Log.i(AVISO, getLocalClassName() + ".onDestroy;"); */
    }

}
