package io.github.netpork.djuradjevdan;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netpork on 12/12/14.
 */
public class Specimen {
    private static final String TAG = "Specimen";

    public boolean ready = false;

    public static final int numberOfPoints = 200;
    public static final int circleScale = 50;
    public int x, y;
    public double rotate, radius, friction, speed, step, freq, prevFreq, boldRate, rotateSpeed;
    public List<Point> points = new ArrayList<Point>();

    public Paint paint;


    public Specimen(float radius) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(false);
//        paint.setARGB(128, 255, 255, 255);
        paint.setColor(Color.WHITE);

        prepare(radius);
    }

    public void prepare(double radius) {
        ready = false;

        x = Video.width / 2;
        y = Video.height / 2;

        rotate = 0;
        this.radius = radius;
        rotateSpeed = MainPanel.RND.nextDouble() * 0.09 + 0.001;
//        friction = MainPanel.RND.nextDouble() * 0.8 + 0.1;

        friction = 0.9;
        speed = MainPanel.RND.nextDouble() * 0.09 + 0.001;
        step = MainPanel.RND.nextDouble() * 0.5 + 0.0001;
        freq = MainPanel.RND.nextDouble() * 0.09 + 0.01;
        boldRate = MainPanel.RND.nextDouble() * 0.3 + 0.1;
        prevFreq = speed;

        points.clear();

        for (int i = 0; i < numberOfPoints; i++) {
            Point p = new Point(x, y);
            points.add(p);
        }

        ready = true;
    }


    public void update() {
        rotate += rotateSpeed;
    }

    public void draw(Canvas c) {
        if (!ready) return;
//        final float scale = Player.FFTAmplitude * circleScale;

        for (int i = 0; i < numberOfPoints; i++) {
            Point p = points.get(i);
            double radius = Math.cos(rotate * 2.321 + freq * i) * this.radius * boldRate + this.radius;
            double tx = x + Math.cos(rotate + step * i) * radius;
            double ty = y + Math.sin(rotate + step * i) * radius;

            p.vx += (tx - p.x) * Player.FFTAmplitude;
            p.vy += (ty - p.y) * Player.FFTAmplitude;

            p.x += p.vx;
            p.y += p.vy;

            p.vx *= friction;
            p.vy *= friction;

            if (p.x >= 0 && p.x <= Video.width && p.y >= 0 && p.y <= Video.height) {
//                c.drawPoint(p.x, p.y, paint);
                c.drawCircle(p.x, p.y, Player.FFTAmplitude * circleScale, paint);
            }
        }
    }

    public class Point {
        public float x, y, vx, vy;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
            vx = 0;
            vy = 0;
        }
    }
}
