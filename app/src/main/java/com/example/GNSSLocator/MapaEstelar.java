package com.example.GNSSLocator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MapaEstelar extends View {
    private List<float[]> satelitesData = new ArrayList<>();
    private List<Boolean> satelitesInFix = new ArrayList<>();


    public MapaEstelar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //Metodo onde desenha o mapa estelar
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(30);

        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;

        int radius;
        if (viewWidthHalf > viewHeightHalf) {
            radius = viewHeightHalf - 10;
        } else {
            radius = viewWidthHalf - 10;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, 15, paint);

        float textX = viewWidthHalf - 100;
        float textY = viewHeightHalf + 45;
        paint.setColor(Color.WHITE);
        canvas.drawText("Sua Localização", textX, textY, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        //For destinado a implementação visual dos satelites
        for (int i = 0; i < satelitesData.size(); i++) {
            float[] sat = satelitesData.get(i);
            float azimuth = sat[0];
            float elevation = sat[1];
            float Svid = sat[2];
            float Const = sat[3];
            boolean infix = satelitesInFix.get(i);

            double azimuthRad = Math.toRadians(azimuth);
            double elevationRad = Math.toRadians(elevation);
            float adjustedRadius = radius * (float) Math.sin(elevationRad);

            float x = (float) (viewWidthHalf + adjustedRadius * Math.cos(azimuthRad));
            float y = (float) (viewHeightHalf + adjustedRadius * Math.sin(azimuthRad));
            //Diferenciação dos satelites de acordo com suas caracteristicas (infix, const)
            if (Const == 1){
                paint.setColor(Color.GREEN);
            }
            if (Const == 3){
                paint.setColor(Color.YELLOW);
            }
            if (Const == 6){
                paint.setColor(Color.CYAN);
            }
            if (infix) {
                canvas.drawCircle(x, y, 10, paint);

            } else {
                float rectSize = 20; // Definir o tamanho do retângulo
                canvas.drawRect(x - rectSize / 2, y - rectSize / 2, x + rectSize / 2, y + rectSize / 2, paint);

            }

            float textSateliteX = x - 70;
            float textSateliteY = y + 40;
            String textoFix = "Svid =" + Svid;
            paint.setColor(Color.WHITE);
            canvas.drawText(textoFix, textSateliteX, textSateliteY, paint);
        }
    }
    //Metodo para atualizar as informações do satelite
    public void updateSatelliteData(List<float[]> satelites, List<Boolean> satelitesInFix) {
        this.satelitesData = satelites;
        this.satelitesInFix = satelitesInFix;
        invalidate();
    }
}
