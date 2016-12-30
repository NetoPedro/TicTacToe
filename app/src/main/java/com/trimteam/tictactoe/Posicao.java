package com.trimteam.tictactoe;

/**
 * Created by pedroneto on 30/12/16.
 */
public class Posicao {

    private int x;
    private int y;

    public Posicao(int x,int y){
        this.x = x;
        this.y = y;
    }

    public Posicao(Posicao posicao){
        this.x = posicao.x;
        this.y= posicao.y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public void incrementa(Posicao p){
        this.x += p.x;
        this.y += p.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Posicao posicao = (Posicao) o;

        if (x != posicao.x) return false;
        return y == posicao.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
