package com.trimteam.tictactoe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pedroneto on 30/12/   16.
 */
public class TabuleiroDeJogo extends View {

    private Tabuleiro tabuleiro;
    private Activity actividade = null;
    private enum Estado{
        JOGADOR_A_JOGAR,IA_A_JOGAR,IA_A_PENSAR,JOGADOR_PASSA,
        IA_PASSA, FIM_DE_JOGO
    };
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
    public TabuleiroDeJogo(Activity actividade){
        super(actividade);
        this.actividade = actividade;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(actividade);

        profundidade = Integer.parseInt(sharedPreferences.getString("nivel_dificuldade","1"));

        if(sharedPreferences.getBoolean("ordem_jogar",true))
            estado = Estado.JOGADOR_A_JOGAR;
        else
            estado = Estado.IA_A_JOGAR;

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
        paint.setColor(Color.GRAY);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);

        for (int linha = 0 ; linha< nrQuadrados;linha++){
            for(int coluna = 0; coluna<nrQuadrados;coluna++){
                int a = coluna * dimQuadrado;
                int b = linha*dimQuadrado;
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(3);
                canvas.drawRect(a,b,a+dimQuadrado,b+dimQuadrado,paint);
                paint.setStrokeWidth(0);
                paint.setColor(Color.rgb(123,167,123));
                canvas.drawRect(a+3,b+3,a+dimQuadrado-3,b+dimQuadrado-3,paint);

                if(!tabuleiro.isVazia(linha,coluna)){
                    paint.setColor(tabuleiro.isX(linha,coluna)? Color.WHITE : Color.BLACK);
                    canvas.drawCircle(a+(dimQuadrado/2),b+(dimQuadrado/2),dimQuadrado*0.45f,paint);
                }

            }
        }
        switch (estado){
            case IA_A_JOGAR: computadorJoga(); break;
            case FIM_DE_JOGO: finalDeJogo();
        }
    }

    public boolean OnTouchEvent(MotionEvent event){
        if(estado!=Estado.JOGADOR_A_JOGAR) return false;
        if(toasAtual != null) toasAtual.cancel();
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) return false;
        if(action == MotionEvent.ACTION_UP) {
            if (event.getX() > Tabuleiro.LINS * dimQuadrado ||
                    event.getY() > Tabuleiro.COLS * dimQuadrado) return false;
            int col = ((int) event.getX() / dimQuadrado);
            int lin = ((int) event.getY() / dimQuadrado);

            if (!tabuleiro.jogadaValida(lin, col)) {
                toasAtual = Toast.makeText(getContext(), "Jogada Invalida", Toast.LENGTH_SHORT);
                toasAtual.show();
            } else {
                ultimaJogada = null;
                tabuleiro = tabuleiro.realizaJogada(lin,col,1);
                if(tabuleiro.fimDoJogo() != -1) estado = Estado.FIM_DE_JOGO;
                else estado =Estado.IA_A_JOGAR;

                invalidate();
            }
            return true;

        }
        return false;
    }

}
