package com.trimteam.tictactoe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by pedroneto on 30/12/   16.
 */
public class TabuleiroDeJogo extends View {

    private Tabuleiro tabuleiro;
    private TabuleiroActivity actividade = null;
    private MediaPlayer mp;

    private boolean multi ;

    public AlertDialog getDialog() {
        return fimDoJogo;
    }

    private enum Estado{
        JOGADOR_A_JOGAR,IA_A_JOGAR,IA_A_PENSAR,JOGADOR_PASSA,
        IA_PASSA, FIM_DE_JOGO
    };
    private int jogador;
    private Estado estado;
    private Paint paint ;
    private int dimQuadrado;
    private int nrQuadrados = 3;
    private Toast toasAtual = null;
    private ProgressDialog progresso = null;
    private AlertDialog fimDoJogo= null;
    private AlertDialog.Builder builder = null;
    private Posicao ultimaJogada = null;
    private int profundidade;
    private SharedPreferences sharedPreferences = null;
    public TabuleiroDeJogo(TabuleiroActivity actividade, boolean cpuLastWinner, int level , boolean multi){
        super(actividade);
        this.actividade = actividade;
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(actividade);
        this.multi = multi;
        profundidade =level;

        if(!cpuLastWinner ){
            estado = Estado.JOGADOR_A_JOGAR;
            jogador =1;}
        else {
            if(!multi) estado =Estado.IA_A_JOGAR;
            else estado = Estado.JOGADOR_A_JOGAR;
            jogador = 2;
        }
        tabuleiro = new Tabuleiro();
        paint = new Paint();

        progresso = new ProgressDialog(getContext());
        progresso.setMessage("A jogar ... ");
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.getWindow().setGravity(Gravity.BOTTOM);
        progresso.setIndeterminate(true);
        builder = new AlertDialog.Builder(actividade)
                .setTitle("TicTacToe")
                .setPositiveButton("OK",null);
        }

    protected void onMeasure(int widthMeasure, int heightMeasure){
        int width = MeasureSpec.getSize(widthMeasure);
        int height = MeasureSpec.getSize(heightMeasure);

        int d =(width <height) ? width : height;
        dimQuadrado = d/nrQuadrados;
        setMeasuredDimension(width,height);
    }

    protected void onDraw (Canvas canvas){
        ArrayList<Posicao> jogadasValidas = null;
        if(estado != Estado.IA_A_PENSAR) progresso.hide();
        if(estado==Estado.JOGADOR_A_JOGAR){
            jogadasValidas = tabuleiro.jogadas();
        }
        //paint.setColor(Color.GRAY);
        //canvas.drawRect(0,0,3*(3 + dimQuadrado),(3+ dimQuadrado)*3,paint);
        for (int linha = 0 ; linha< nrQuadrados;linha++){
            for(int coluna = 0; coluna<nrQuadrados;coluna++){
                int a = coluna * dimQuadrado;
                int b = linha*dimQuadrado;
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(3);
                int top = 0, left = 0, right =0,  down = 0;
                if(coluna == 0){
                    left = 3;

                }

                if(coluna == 2){
                    right = 3;
                }
                if (linha == 0) top = 3;
                if (linha ==2) down = 3;

                canvas.drawRect(a+left,b+top,a+dimQuadrado-right,b+dimQuadrado-down,paint);
                paint.setStrokeWidth(0);
                paint.setColor(Color.parseColor("#ff303030"));
                canvas.drawRect(a+3,b+3,a+dimQuadrado-3,b+dimQuadrado-3,paint);

                if(!tabuleiro.isVazia(linha,coluna)){


                    paint.setColor(tabuleiro.isX(linha,coluna)? Color.LTGRAY : Color.parseColor("#ffffbb33"));
                    if(tabuleiro.isX(linha,coluna)){
                        paint.setStrokeWidth(25);
                        canvas.drawLine(a+10+10,b+10+10,a+dimQuadrado-10-10,b+dimQuadrado-10-10,paint);
                        //canvas.drawLine(a+dimQuadrado+3+10,b-3-10,a+3+10,b-3-10+dimQuadrado,paint);
                        canvas.drawLine(a+20,b+dimQuadrado-20,a+dimQuadrado-20,b+20,paint);
                    }else{
                        canvas.drawCircle(a+(dimQuadrado/2),b+(dimQuadrado/2),dimQuadrado*0.45f,paint);
                        paint.setColor(Color.parseColor("#ff303030"));
                        canvas.drawCircle(a+(dimQuadrado/2),b+(dimQuadrado/2),dimQuadrado*0.35f,paint);
                    }
                    //canvas.drawCircle(a+(dimQuadrado/2),b+(dimQuadrado/2),dimQuadrado*0.45f,paint);
                }

            }
        }
        switch (estado){
            case IA_A_JOGAR: computadorJoga(); break;
            case FIM_DE_JOGO: finalDeJogo();
        }
    }

  /*  public void run(){

        switch (estado){
            case IA_A_JOGAR: computadorJoga(); break;
            case FIM_DE_JOGO: finalDeJogo();
        }
    }*/

    public boolean onTouchEvent(MotionEvent event){
        if(estado!=Estado.JOGADOR_A_JOGAR) return false;
        if(toasAtual != null) toasAtual.cancel();
        int action = event.getAction();

            if (event.getX() > Tabuleiro.LINS * dimQuadrado ||
                    event.getY() > Tabuleiro.COLS * dimQuadrado) return false;
            int col = ((int) event.getX() / dimQuadrado);
            int lin = ((int) event.getY() / dimQuadrado);

            if (!tabuleiro.jogadaValida(lin, col)) {
                //toasAtual = Toast.makeText(getContext(), "Jogada Invalida", Toast.LENGTH_SHORT);
                //toasAtual.show();
            } else {
                 mp = MediaPlayer.create(actividade, R.raw.button_16);
                mp.start();
                ultimaJogada = null;
                tabuleiro = tabuleiro.realizaJogada(lin,col,1);
                ultimaJogada = new Posicao(lin,col);
                //fillPosicaoJogador();
                if(tabuleiro.fimDoJogo() != -1) estado = Estado.FIM_DE_JOGO;
                else if(!multi) estado =Estado.IA_A_JOGAR;
                else estado = Estado.JOGADOR_A_JOGAR;
                mp =null;
                invalidate();
            }
            return true;

    }

    public void computadorJoga(){
        estado = Estado.IA_A_PENSAR;
        //progresso.show();
        //invalidate();
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MiniMax mm;
                        if(tabuleiro.getJogador() == 1)
                         mm= new MiniMax(tabuleiro, 1,2);
                        else
                         mm = new MiniMax(tabuleiro,2,1);
                        int[] vecJogada = mm.move(profundidade);
                        Posicao jogada = new Posicao(vecJogada[0],vecJogada[1]);
                        ultimaJogada = new Posicao(jogada);
                        tabuleiro = tabuleiro.realizaJogada(jogada.getX(),jogada.getY(),2);
                        if(tabuleiro.fimDoJogo() != -1) estado = Estado.FIM_DE_JOGO;
                        else estado = Estado.JOGADOR_A_JOGAR;

                        postInvalidate();
                        //fillPosicaoAI();
                    }
                }).start();
            }
        }, 500);


    }

    public void finalDeJogo(){
        if(fimDoJogo!= null && fimDoJogo.isShowing())  return;
            progresso.hide();
            int vencedor = tabuleiro.fimDoJogo();
            String message = "";
        actividade.cpuLastWinner = !actividade.cpuLastWinner;

        if (vencedor == 0){
                message = getContext().getString(R.string.draw);
            }
            else if (vencedor == jogador) {
                if(!multi){
                    message = getContext().getString(R.string.player2Win);}
                else
                    message = getContext().getString(R.string.player1Win);
                actividade.increaseUserVic();
            }
            else {
                if(!multi)
                    message = getContext().getString(R.string.cpuWin);
                else
                    message = getContext().getString(R.string.playerWin);
                actividade.increaseCpuVic();
            }
            fimDoJogo = builder.create();
            fimDoJogo.setMessage(message);
            fimDoJogo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    getActivity().restart();
                }
            });
            fimDoJogo.show();

    }


    public TabuleiroActivity getActivity(){
        return  actividade;
    }



}
