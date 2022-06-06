package com.example.grafika;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PowierzchniaRysunku extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {
// pozwala kontrolować i monitorować powierzchnię
    private SurfaceHolder mPojemnik;
    // wątek, który odświeża kanwę
    private Thread mWatekRysujacy;
    // flaga logiczna do kontrolowania pracy watku
    private boolean mWatekPracuje = false;
    // obiekt do tworzenia sekcji krytycznych
    private Object mBlokada=new Object();
    public PowierzchniaRysunku(Context context,
                               AttributeSet attrs) {
        super(context, attrs);
        // Pojemnik powierzchni - pozwala kontrolować i monitorować powierzchnię
        mPojemnik = getHolder();
        mPojemnik.addCallback(this);
        //inicjalizacja innych elementów...
    }
    public void wznowRysowanie() {
// uruchomienie wątku rysującego
        mWatekRysujacy = new Thread(this);
        mWatekPracuje = true;
        mWatekRysujacy.start();
    }
    public void pauzujRysowanie() {
        mWatekPracuje = false;
    }
    //obsługa dotknięcia ekranu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //sekcja krytyczna – modyfikacja rysunku na  wyłączność
        synchronized (mBlokada) {
            //modyfikacja rysunku...
        }
        return true;
    }
    //żeby lint nie wyświetlał ostrzeżeń - onTouchEvent i performClick trzeba
    //implementować razem
    public boolean performClick()
    {
        return super.performClick();
    }
    @Override
    public void run() {
        while (mWatekPracuje) {
            Canvas kanwa = null;
            try {
                // sekcja krytyczna - żaden inny wątek nie  może używać pojemnika
                synchronized (mPojemnik) {
                    // czy powierzchnia jest prawidłowa
                    if (!mPojemnik.getSurface().isValid())
                        continue;
                    // zwraca kanwę, na której można rysować, każdy piksel
                    // kanwy w prostokącie przekazanym jako parametr musi być
                    // narysowany od nowa inaczej: rozpoczęcie edycj
                            // zawartości kanwy
                            kanwa =
                            mPojemnik.lockCanvas(null);
                    //sekcja krytyczna – dostęp do rysunku na wyłączność
                    synchronized (mBlokada) {
                        if (mWatekPracuje) {
                            //rysowanie na lokalnej kanwie...
                        }
                    }
                }
            } finally {
                // w bloku finally - gdyby wystąpił wyjątek w powyższym
                        // powierzchnia zostanie zostawiona w spójnym stanie
                if (kanwa != null) {
                    // koniec edycji kanwy i wyświetlenie rysunku na ekranie

                    mPojemnik.unlockCanvasAndPost(kanwa);
                }
            }
            try {
                Thread.sleep(1000 / 25); // 25
            } catch (InterruptedException e) { }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // inicjalizacja...
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int
            format, int width,
                               int height) { }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // zatrzymanie rysowania
        mWatekPracuje = false;
    }
}