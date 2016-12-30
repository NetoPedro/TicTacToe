package com.trimteam.tictactoe;

import android.app.ActionBar;

import java.util.ArrayList;

/**
 * Created by pedroneto on 30/12/16.
 */
public class Tabuleiro {

    public static final int COLS = 3;
    public static final int LINS = 3;
    public static final int VAZIA = 0;
    public static final int PECA_X= 1;
    public static final int PECA_O = 2;
    private int[][] tabuleiro;
    private int jogador;
    private int nrPosLivres;

    public Tabuleiro(){
        tabuleiro = new int[LINS][COLS];
        for (int linha = 0 ; linha < LINS;linha++){
            for(int coluna = 0 ; coluna < COLS; coluna++){
                tabuleiro[linha][coluna]  =VAZIA;
            }
        }
        jogador = PECA_X;
        nrPosLivres = 3*3;
    }

    public void preencherPosicao(int linha, int coluna ){
        nrPosLivres--;
        tabuleiro[linha][coluna] = jogador;
    }

    public int getValorPosicao(int linha , int coluna){
       return tabuleiro[linha][coluna];
    }

    public boolean isVazia(int linha, int coluna ){
         return tabuleiro[linha][coluna] == VAZIA;
    }

    public boolean isX(int linha, int coluna ){
        return tabuleiro[linha][coluna] == PECA_X;
    }
    public boolean isO(int linha, int coluna ){
        return tabuleiro[linha][coluna] == PECA_O;
    }

    public int fimDoJogo(){

                if (tabuleiro[0][0] != 0 &&tabuleiro[0][0] == tabuleiro[0][1] && tabuleiro[0][1] == tabuleiro[0][2]) return getValorPosicao(0,0);
                if (tabuleiro[1][0] != 0 &&tabuleiro[1][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[1][2]) return getValorPosicao(1,0);
                if (tabuleiro[2][0] != 0 &&tabuleiro[2][0] == tabuleiro[2][1] && tabuleiro[2][1] == tabuleiro[2][2]) return getValorPosicao(2,0);
                if (tabuleiro[0][0] != 0 &&tabuleiro[0][0] == tabuleiro[1][0] && tabuleiro[1][0] == tabuleiro[2][0]) return getValorPosicao(0,0);
                if (tabuleiro[0][1] != 0 &&tabuleiro[0][1] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][1]) return getValorPosicao(0,1);
                if (tabuleiro[0][2] != 0 &&tabuleiro[0][2] == tabuleiro[1][2] && tabuleiro[1][2] == tabuleiro[2][2]) return getValorPosicao(0,2);
                if (tabuleiro[0][0] != 0 &&tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) return getValorPosicao(0,0);
                if (tabuleiro[2][0] != 0 &&tabuleiro[2][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[0][2]) return getValorPosicao(2,0);
        if (nrPosLivres == 0)     return 0;
        return -1;
    }

    public int getJogadorAtual(){
        return jogador;
    }
    public void alternaJogador(){
        jogador = ((jogador== PECA_X) ? PECA_O : PECA_X);
    }

    private Tabuleiro duplica(){
        Tabuleiro novoTabuleiro = new Tabuleiro();
        novoTabuleiro.jogador = jogador;
        novoTabuleiro.nrPosLivres = nrPosLivres;
        novoTabuleiro.tabuleiro = new int[LINS][COLS];
        for (int linha = 0 ; linha < LINS;linha++){
            for(int coluna = 0 ; coluna < COLS; coluna++){
                novoTabuleiro.tabuleiro[linha][coluna]  =tabuleiro[linha][coluna];
            }
        }
        return novoTabuleiro;
    }

    public ArrayList<Posicao> jogadas(){
        ArrayList<Posicao> jogadas = new ArrayList<>();
        for (int linha = 0 ; linha < LINS;linha++){
            for(int coluna = 0 ; coluna < COLS; coluna++){
                if(tabuleiro[linha][coluna]  ==VAZIA)
                    jogadas.add(new Posicao(linha,coluna));

            }
        }
        return jogadas;
    }

    public Tabuleiro realizaJogada(int x, int y , int jogador){
        Tabuleiro novoTabuleiro = duplica();
        novoTabuleiro.preencherPosicao(x,y);
        novoTabuleiro.alternaJogador();
        return novoTabuleiro;
    }
    public int[][] getTabuleiro(){
        return tabuleiro;
    }
}
