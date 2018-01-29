package com.example.alexm.witchercolorwar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static java.lang.Math.sin;

/**
 * Created by alexm on 14.01.2018.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Context cont;
    private boolean thread_started = false;
    Map PlayersMap = new Map();
    final double PI  = 3.14159265;
    final float size = Resources.getSystem().getDisplayMetrics().widthPixels / 40;
    int player_number;
    int Turn = 1;
    int select_x = -1;
    int select_y = -1;
    boolean turn;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        PlayersMap.Generate(13, 5);
        turn = true;
    }

    public boolean IsOnSquare(double x, double y, double size_w, double size_h, double pos_x, double pos_y){
        return (pos_x > x && pos_x < x + size_w && pos_y > y && pos_y < y + size_h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int mx = (int) event.getX();
                int my = (int) event.getY();
                if(IsOnSquare(Resources.getSystem().getDisplayMetrics().widthPixels / 8 * 7, Resources.getSystem().getDisplayMetrics().heightPixels / 8 * 7, Resources.getSystem().getDisplayMetrics().widthPixels / 8, Resources.getSystem().getDisplayMetrics().heightPixels, mx, my)) {
                    turn = false;
                    break;
                }
                for (int i = 0; i < PlayersMap.map.size(); i++) {
                    for (int j = 0; j < PlayersMap.map.get(i).size(); j++) {
                        double k = (j % 2 == 0) ? size * Math.cos(30 * PI / 180) : 0;
                        double x = 50 + j * size + j * size * sin(30 * PI / 180);
                        double y = 50 + i * size * Math.cos(30 * PI / 180) * 2 + k;
                        if (IsOnSquare(x, y, size, size * Math.cos(30 * PI / 180) * 2, mx, my)){
                            //PlayersMap.map.get(i).set(j, 2);
                            if(turn) {
                                if (Turn == PlayersMap.map.get(i).get(j)) {
                                    select_x = i;
                                    select_y = j;
                                } else {
                                    if (select_x != -1 && select_y != -1) {
                                        //if (Math.abs(j - select_y) < 2 && (j % 2)?:)
                                        int opponent = PlayersMap.Strenght.get(i).get(j);
                                        int your = PlayersMap.Strenght.get(select_x).get(select_y);
                                        int f = 0;
                                        if (your - 1 > opponent) {
                                            f = your - opponent - 1;
                                            PlayersMap.map.get(i).set(j, Turn);
                                            PlayersMap.Strenght.get(select_x).set(select_y, 1);
                                            select_x = i;
                                            select_y = j;
                                        }
                                        if (your - 1 == opponent) {
                                            f = 0;
                                            PlayersMap.map.get(i).set(j, 0);
                                            PlayersMap.Strenght.get(select_x).set(select_y, 1);
                                        }
                                        if (your - 1 < opponent) {
                                            f = opponent - your + 1;
                                            PlayersMap.Strenght.get(select_x).set(select_y, 1);
                                        }
                                        PlayersMap.Strenght.get(i).set(j, f);
                                    }
                                }
                            }
                            else{
                                if (Turn == PlayersMap.map.get(i).get(j)) {
                                    PlayersMap.Strenght.get(i).set(j, PlayersMap.Strenght.get(i).get(j) + 1);
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Draw map
        Bitmap background = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        background = Bitmap.createScaledBitmap(background, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels, true);
        canvas.drawBitmap(background, 0, 0, null);
        if (turn) {
            Bitmap choosemode = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.choose_mode));
            choosemode = Bitmap.createScaledBitmap(choosemode, Resources.getSystem().getDisplayMetrics().widthPixels / 8, Resources.getSystem().getDisplayMetrics().heightPixels / 10, true);
            canvas.drawBitmap(choosemode, Resources.getSystem().getDisplayMetrics().widthPixels - choosemode.getWidth(), Resources.getSystem().getDisplayMetrics().heightPixels - choosemode.getHeight(), null);
        }
        else {
            Bitmap endturn = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.endturn));
            endturn = Bitmap.createScaledBitmap(endturn, Resources.getSystem().getDisplayMetrics().widthPixels / 8, Resources.getSystem().getDisplayMetrics().heightPixels / 10, true);
            canvas.drawBitmap(endturn, Resources.getSystem().getDisplayMetrics().widthPixels - endturn.getWidth(), Resources.getSystem().getDisplayMetrics().heightPixels - endturn.getHeight(), null);
        }
         List<List<Integer>> players_map = PlayersMap.map;
        for (Integer i = 0;i < players_map.size();i++){
            for (Integer j = 0;j < players_map.get(i).size();j++){
                //size * cos(30 * PI / 180)
                Paint paint = new Paint();
                switch (players_map.get(i).get(j)){
                    case 0:
                        paint.setColor(Color.GRAY);
                        break;
                    case 1:
                        paint.setColor(Color.GREEN);
                        break;
                    case 2:
                        paint.setColor(Color.RED);
                        break;
                    case 3:
                        paint.setColor(Color.CYAN);
                        break;
                }
                if (i == select_x && j == select_y) {
                    paint.setColor(Color.BLUE);
                }
                paint.setAntiAlias(true);
                int pos_x = (int) (50 + j * size + size * Math.sin(30 * PI / 180) * j);
                int pos_y = (int) (50 + i * size * Math.cos(30 * PI / 180) * 2);
                if (j % 2 == 0){
                    pos_y += size * Math.cos(30 * PI / 180);
                }
                Path path = new Path();
                path.moveTo(pos_x, pos_y);
                path.lineTo(pos_x + size, pos_y);
                path.lineTo((float) (pos_x + size + size * Math.sin(30 * PI / 180)), (float) (pos_y + size * Math.cos(30 * PI / 180)));
                path.lineTo(pos_x + size, (float) (pos_y + 2 * size * Math.cos(30 * PI / 180)));
                path.lineTo(pos_x, (float) (pos_y + 2 * size * Math.cos(30 * PI / 180)));
                path.lineTo((float) (pos_x + -size * Math.sin(30 * PI / 180)),  (float) (pos_y + size * Math.cos(30 * PI / 180)));
                canvas.drawPath(path, paint);
            }
        }
        List<List<Integer>> strenght = PlayersMap.Strenght;
        for (Integer i = 0;i < strenght.size();i++) {
            for (Integer j = 0; j < strenght.get(i).size(); j++) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.RED);
                paint.setTextSize(size / 2);
                int pos_x = (int) (50 + j * size + size * Math.sin(30 * PI / 180) * j);
                int pos_y = (int) (50 + i * size * Math.cos(30 * PI / 180) * 2);
                if (j % 2 == 0){
                    pos_y += size * Math.cos(30 * PI / 180);
                }
                canvas.drawText(Integer.toString(strenght.get(i).get(j)), pos_x + size / 3, pos_y + size, paint);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread_started) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            thread_started = true;
            thread.setRunning(true);
            thread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
