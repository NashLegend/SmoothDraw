package com.example.smoothdraw;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnTouchListener {

    private ImageView view;
    private Path path;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        view = (ImageView) findViewById(R.id.view);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bitmap = Bitmap.createBitmap(getWindowWidth(),
                        getWindowHeight(), Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                paint = new Paint();
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                paint.setAntiAlias(true);
                paint.setARGB(255, 0, 0, 255);
                paint.setStrokeWidth(8);
                paint.setStyle(Style.STROKE);
                view.setImageBitmap(bitmap);
                break;
            case MotionEvent.ACTION_MOVE:
                int size = event.getHistorySize();
                for (int i = 0; i < size; i++) {
                    path.lineTo(event.getHistoricalX(i), event.getHistoricalY(i));
                }
                path.lineTo(event.getX(), event.getY());
                canvas.drawPath(path, paint);
                view.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                view.invalidate();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        clear();
                    }
                }, 3000);
                break;
            default:
                break;
        }

        return true;
    }

    protected void clear() {
        if (canvas != null) {
            Paint p = new Paint();
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(p);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            view.invalidate();
        }
    }

    public int getWindowWidth() {
        return getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth();
    }

    public int getWindowHeight() {
        return getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
    }

}