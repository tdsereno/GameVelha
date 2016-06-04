package com.dash.tiago.gamevelha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends Activity {
    Tela t;

    int[][] tabuleiro = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
    };

    boolean vezdox = true;
    String msg = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Faz com que o app rode em tela cheia
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        t = new Tela(this);
        setContentView(t);
    }

    public void fimDeJogo(int i) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Fim de Jogo!");

        if(i == 0){
            if(vezdox)
                msg = "O Ganhou!";
            else
                msg = "X Ganhou!";
        }
        else
            msg = "Velha!";

        alertDialogBuilder.setMessage(msg + " Clique em OK para jogar novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class Tela extends View{
        Tela(Context context){
            super(context);
            setBackgroundResource(R.drawable.background);
        }

        @Override
        public boolean onTouchEvent(MotionEvent me) {
            if(me.getAction() == MotionEvent.ACTION_UP) {
                float x = me.getX();
                float y = me.getY();

                // Tamanho Da tela
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                // Acha o lugar
                for(int n = 0;n<3;n++){
                    for(int m = 0;m<3;m++){
                        if(x>m*(width/3) && x<(m+1)*(width/3) && y>n*(height/3) && y<(n+1)*(height/3)){
                            if(tabuleiro[m][n] == 0) {
                                // Operador ternário: if(vezdox)tabuleiro[m][n] = 1; else tabuleiro[m][n] = 2;
                                tabuleiro[m][n] = (vezdox?1:2);
                                vezdox =! vezdox;
                            }
                        }
                    }
                }
                t.invalidate();
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);

            // Tamanho Da tela
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            // Define a pintura
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(getResources().getColor(R.color.branco));

            //Linhas Verticais
            c.drawLine(width/3,0,width/3,height,p);
            c.drawLine((width/3)*2,0,(width/3)*2,height,p);
            //Linhas Horizontais
            c.drawLine(0,height/3,width,height/3,p);
            c.drawLine(0,(height/3)*2,width, (height / 3) * 2, p);

            p.setColor(getResources().getColor(R.color.corDoO));
            for(int n = 0;n<3;n++) {
                for(int m = 0;m<3;m++){
                    if(tabuleiro[m][n] == 2)
                        c.drawCircle( ((width/3)/2)+(m*(width/3)), ((height/3)/2)+(n*(height/3)) ,((width/3)/2),p );
                }
            }

            p.setColor(getResources().getColor(R.color.corDoX));
            for(int n = 0;n<3;n++) {
                for(int m = 0;m<3;m++) {
                    if(tabuleiro[m][n] == 1) {
                        c.drawLine(m*(width/3),n*(height/3),(m+1)*(width/3),(n+1)*(height/3),p);
                        c.drawLine(m*(width/3),(n+1)*(height/3),(m+1)*(width/3),n*(height/3),p);
                    }
                }
            }

            //Seta cor e espessura da linha
            p.setColor(getResources().getColor(R.color.riscaLinha));
            p.setStrokeWidth(4f);

            // Verifica se há ganhador

            //Linhas Horizontais

 boolean acabou = false;
            if(tabuleiro[0][0] != 0) {
                if (tabuleiro[0][0] == tabuleiro[1][0] && tabuleiro[0][0] == tabuleiro[2][0]) {
                    c.drawLine(0, ((height / 3) / 2), width, ((height / 3) / 2), p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }
            if(tabuleiro[0][1] != 0) {
                if (tabuleiro[0][1] == tabuleiro[1][1] && tabuleiro[0][1] == tabuleiro[2][1]) {
                    c.drawLine(0, ((height / 3) / 2) + (height / 3), width, ((height / 3) / 2) + (height / 3), p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }
            if(tabuleiro[0][2] != 0) {
                if (tabuleiro[0][2] == tabuleiro[1][2] && tabuleiro[0][2] == tabuleiro[2][2]) {
                    c.drawLine(0, ((height / 3) / 2) + ((height / 3) * 2), width, ((height / 3) / 2) + ((height / 3) * 2), p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }

            //Linhas Verticais
            if(tabuleiro[0][0] != 0) {
                if (tabuleiro[0][0] == tabuleiro[0][1] && tabuleiro[0][0] == tabuleiro[0][2]) {
                    c.drawLine((width / 3) / 2, 0, (width / 3) / 2, height, p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }
            if(tabuleiro[1][0] != 0) {
                if (tabuleiro[1][0] == tabuleiro[1][1] && tabuleiro[1][0] == tabuleiro[1][2]) {
                    c.drawLine(((width / 3) / 2) + (width / 3), 0, ((width / 3) / 2) + (width / 3), height, p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }
            if(tabuleiro[2][0] != 0) {
                if (tabuleiro[2][0] == tabuleiro[2][1] && tabuleiro[2][0] == tabuleiro[2][2]) {
                    c.drawLine(((width / 3) / 2) + ((width / 3) * 2), 0, ((width / 3) / 2) + ((width / 3) * 2), height, p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }

            //Linhas Diagonais
            if(tabuleiro[0][0] != 0) {
                if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[0][0] == tabuleiro[2][2]) {
                    c.drawLine(0, 0, width, height, p);

                    fimDeJogo(0);
                    acabou = true;
                }
            }
            if(tabuleiro[2][0] != 0) {
                if (tabuleiro[2][0] == tabuleiro[1][1] && tabuleiro[2][0] == tabuleiro[0][2]) {
                    c.drawLine(0, height, width, 0, p);
                    fimDeJogo(0);
                    acabou = true;
                }
            }

            // Verifica se não houve ganhador
            for(int i=0,m=0;m<3;m++){
                for(int n=0;n<3;n++){
                    if(tabuleiro[m][n] != 0)
                        i++;
                }
                if(i == 9 && !acabou)
                    fimDeJogo(1);
            }
        }
    }
}