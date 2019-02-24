package com.example.camaradibujo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class Lienzo extends View {

    Drawable imagen;

    private Path drawPath; //Path que utilizaré para ir pintando las lineas
    private static Paint drawPaint; //Paint de dibujar
    private Paint canvasPaint; // Paint de Canvas
    private static int paintColor = 0xFFFF0000; //Color Inicial del pincel en rojo
    private Canvas drawCanvas; //canvas
    private Bitmap canvasBitmap; //canvas para guardar
    private static float TamanyoPunto;
    private static boolean borrado = false;

    private Paint pintar = new Paint();
    private float touchX = 100.0f;
    private float touchY = 100.0f;
    private int cont = 0;

    public Lienzo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        //Aqui se configura el area sobre la que pintar
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor); // color del pincel determinado
        drawPaint.setAntiAlias(true);
        //setTamanyoPunto(20);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    //Tamaño asignado a la vista
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //Pinta la vista. Será llamado desde el OnTouchEvent, para pintar en el canvas
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

        cont = (cont + 1) % 3; // sirve para rotar las figuras creadas
        switch (cont) {
            case 0: // texto
                setFiguraTexto(canvas);
                break;

            case 1: // punto
                setPunto(canvas);
                break;

            case 2: // triangulo
                setFiguraTriangulo(canvas);
                break;
        }

    }

    //Registra los touch de usuario
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;

            default:
                return false;
        }
        invalidate(); //repintar
        return true;
    }

    //Actualiza color
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);  // para cambiar el color del pincel
        drawPaint.setColor(paintColor); // para pintar en el lienzo con el color elegido
    }

    //Poner tamaño del punto
    public static void setTamanyoPunto(float nuevoTamanyo){
        //float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, nuevoTamanyo, getResources().getDisplayMetrics());
        //TamanyoPunto = pixel;
        drawPaint.setStrokeWidth(nuevoTamanyo);
    }


    //set borrado true or false
    public static void setBorrado(boolean estaborrado){
        borrado = estaborrado;
        if(borrado) {
            drawPaint.setColor(Color.WHITE);
            //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setColor(paintColor);
            //drawPaint.setXfermode(null);
        }
    }

    public void NuevoDibujo(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }


    public void setFiguraTexto(Canvas canvas){
        pintar.setColor(Color.GRAY);
        pintar.setTextSize(120);
        canvas.drawText("Hola Paint", touchX, touchY, pintar);
    }

    public void setFiguraTriangulo(Canvas canvas){
        Path path = new Path();
        path.moveTo(touchX, touchY);
        path.lineTo(touchX / 4, touchY);
        path.lineTo(touchX / 4, touchY / 3);

        Paint paint = new Paint(); // pintura o pincel
        paint.setStyle(Paint.Style.FILL); // el tipo de trazado

        // convertimos un color Hexa en un android.graphics.Color
        paint.setColor(Color.parseColor("#0040FF"));
        // pintamos un triangulo azul
        canvas.drawPath(path, paint);
    }


    public void setPunto(Canvas canvas){
        pintar.setColor(Color.parseColor("#ee82ee"));
        canvas.drawCircle(touchX, touchY, 40, pintar);
    }


    public void cargaImagen(Bitmap bitmap){
        drawCanvas.setBitmap(bitmap);
    }






}
