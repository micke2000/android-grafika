package com.example.grafika;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.core.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
//UWAGA TO NIE DZIALA TO SA WATKI
// POLEGŁEM GDY TO PISAŁEM, DZIAŁA ALE PASKUDNIE
public class PowierzchniaRysunku extends SurfaceView
        implements SurfaceHolder.Callback {
    private final Paint mBitmapPaint;
    private SurfaceHolder surfaceHolder = null;
    private List<Pair<Float,Float>> beginEndCoordinates = new ArrayList<>();
    private Paint paint = null;
    private Thread thread = null;
    // Record whether the child thread is running or not.
    private Boolean threadRunning = true;
    private Canvas kanwa = null;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private float positionX = 0;
    private boolean mStopThread = true;
    private float positionY = 0;

    public Bitmap getmBitmap() {
       return this.mBitmapa;
    }

    public void setmBitmap(Bitmap mBitmapa) {
        this.mBitmapa = mBitmapa;
    }

    private Bitmap mBitmapa = null;
    private boolean initialized = false;
    private Path mSciezka = new Path();
    private Object mBlokada=new Object();
    private DrawingThread mDrawingThread;

    public PowierzchniaRysunku(Context context,
                               AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
// ustawienie koloru
        paint.setColor(Color.BLUE);
// ustawienie szerokości linii
        paint.setStrokeWidth(15);
// styl rysowania – wypełnianie figur
        paint.setStyle(Paint.Style.FILL);
// styl rysowania – rysowanie tylko konturu
        paint.setStyle(Paint.Style.STROKE);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }
    private void wznowRysowanie(){
        synchronized (threadRunning) {
            threadRunning = true;
        }
        mDrawingThread = new DrawingThread();
        mDrawingThread.start();

    }
    public void pauzujRysowanie() {
        synchronized (threadRunning) {
            threadRunning = false;
        }
        try {
            mDrawingThread.join();
        } catch (InterruptedException ignored) {
        }

    }
    private void pauseResume() {
        if (mStopThread) {
            pauzujRysowanie();
            mStopThread = false;
        } else {
            wznowRysowanie();
            mStopThread = true;
        }}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        screenHeight = getHeight();
        screenWidth = getWidth();
        mBitmapa = Bitmap.createBitmap(screenWidth, screenHeight,
                Bitmap.Config.ARGB_8888);
        kanwa = new Canvas(mBitmapa);
        mSciezka = new Path();
//        kanwa.setBitmap(mBitmapa);
        wznowRysowanie();
    }
public void save_to_file(){
    View content = PowierzchniaRysunku.this;
    content.setDrawingCacheEnabled(true);
    content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    Bitmap bitmap = content.getDrawingCache();
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    File file = new File(path+"/image.png");
    FileOutputStream ostream;
    try {
        file.createNewFile();
        ostream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
        ostream.flush();
        ostream.close();
//        Toast.makeText(, "image saved", 5000).show();
    } catch (Exception e) {
        e.printStackTrace();
//        Toast.makeText(getApplicationContext(), "error", 5000).show();
    }
}
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        pauzujRysowanie();
        mBitmapa = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config. ARGB_8888);
    }

    @Override
    public boolean performClick() {
        pauseResume();
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        positionX = event.getX();
        positionY = event.getY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
//                    setRefreshColor();
                    pauseResume();
                    mSciezka.reset();
                    mSciezka.moveTo(positionX, positionY);
                    beginEndCoordinates.add(new Pair<>(positionX,positionY));
                    System.out.println(beginEndCoordinates.get(0).first.toString());
//                    canvas = surfaceHolder.lockCanvas();

//                    surfaceHolder.unlockCanvasAndPost(canvas);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mSciezka.lineTo(positionX, positionY);

                    break;
                case MotionEvent.ACTION_UP:
                    mSciezka.lineTo(positionX, positionY);
                    synchronized (beginEndCoordinates){
                        beginEndCoordinates.add(new Pair<>(positionX,positionY));
                    }
//                    beginEndCoordinates.add(new Pair<>(positionX,positionY));
//                    System.out.println(beginEndCoordinates.get(1).first.toString());
//                    canvas.drawPath(mSciezka, paint);
//                    canvas.drawCircle(event.getX(), event.getY(), 15, paint);
//                    canvas = surfaceHolder.lockCanvas();
//                    surfaceHolder.unlockCanvasAndPost(canvas);
                    break;
            }
//        if(mSciezka  != null){
//            canvas = surfaceHolder.lockCanvas();
//            canvas.drawPath(mSciezka, paint);
//            surfaceHolder.unlockCanvasAndPost(canvas);
//        }
        return true;
    }
    private class DrawingThread extends Thread {
        // rysowanie jak w oficjalnych przykładach Google
        public void run() {
            while (threadRunning) {
                try {
                    // zwraca kanwę, na której można rysować, każdy
                    // piksel kanwy w prostokącie przekazanym jako
// parametr musi być narysowany od nowa
                    // inaczej: rozpoczęcie edycji zawartości kanwy
                    kanwa = surfaceHolder.lockCanvas(null);
                    // sekcja krytyczna - żaden inny wątek nie może
                    // używać
                    // pojemnika (<=>rysować)
                    synchronized (surfaceHolder) {
                        // sekcja krytyczna - żaden inny wątek nie
                        // może zmienić flagi mThreadRunning
                        synchronized (threadRunning) {
                            if (threadRunning) {
//                                kanwa.drawBitmap(mBitmapa,0,0,mBitmapPaint);
//                                kanwa.save();
                                kanwa.drawPath(mSciezka, paint);
                                synchronized (beginEndCoordinates) {
                                    System.out.println(beginEndCoordinates.size() );
                                    if(beginEndCoordinates.size() == 2) {
                                        kanwa.drawCircle(beginEndCoordinates.get(0).first, beginEndCoordinates.get(0).second, 8, paint);
                                        kanwa.drawCircle(beginEndCoordinates.get(1).first, beginEndCoordinates.get(1).second, 8, paint);
                                        System.out.println("rysuje 1 i 2 kolko");
                                        kanwa.drawBitmap(mBitmapa, 0, 0, null);
                                        beginEndCoordinates.clear();
                                    }
//                                    kanwa.drawBitmap(mBitmapa,0,0,mBitmapPaint);
                                }

                            }
                        }}
                } finally {
                    // w bloku finally - gdyby wystąpił wyjątek w
                    // powyższym powierzchnia zostanie zostawiona w
// spójnym stanie
                    if (kanwa != null) {
                        // koniec edycji kanwy i wyświetlenie
                        // rysunku na ekranie

                        surfaceHolder.unlockCanvasAndPost(kanwa);

                    }
                }
                try {
                    Thread.sleep(1000 / 25); // 10 klate na sekundę
                } catch (InterruptedException e) {
                }
            }
        }
    }


}