package com.trimteam.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class TabuleiroActivity extends AppCompatActivity {
    TabuleiroDeJogo tabuleiroDeJogo;
    HashMap<Posicao,ImageView> imagensTab;
    private SharedPreferences sharedPreferences;
    TextView imageView,imageView2, cpuPontText, userPontText;
    private int cpuPont = 0, userPont = 0;

    TextView textViewJOG1, textViewJOG2, textViewPontJOG1, TextViewPontJOG2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabuleiro);
        cpuPontText = (TextView) findViewById(R.id.pontCPU);
        userPontText = (TextView) findViewById(R.id.pontUSER);
        /*imagensTab = new HashMap<>();
        imagensTab.put(new Posicao(0,0), (ImageView) findViewById(R.id.imageView));
        imagensTab.put(new Posicao(0,1), (ImageView) findViewById(R.id.imageView2));
        imagensTab.put(new Posicao(0,2), (ImageView) findViewById(R.id.imageView3));
        imagensTab.put(new Posicao(1,0), (ImageView) findViewById(R.id.imageView4));
        imagensTab.put(new Posicao(1,1), (ImageView) findViewById(R.id.imageView5));
        imagensTab.put(new Posicao(1,2), (ImageView) findViewById(R.id.imageView6));
        imagensTab.put(new Posicao(2,0), (ImageView) findViewById(R.id.imageView7));
        imagensTab.put(new Posicao(2,1), (ImageView) findViewById(R.id.imageView8));
        imagensTab.put(new Posicao(2,2), (ImageView) findViewById(R.id.imageView9));*/
        textViewJOG1 = (TextView) findViewById(R.id.textView);;
                textViewJOG2 = (TextView) findViewById(R.id.textView2);;
        textViewPontJOG1 = (TextView) findViewById(R.id.textView3);;
                TextViewPontJOG2= (TextView) findViewById(R.id.textView5);;

        LinearLayout ll = (LinearLayout) findViewById(R.id.tabuleiroLayout);
        imageView = (TextView) findViewById(R.id.imageView);

        imageView2 = (TextView) findViewById(R.id.imageView2);

        tabuleiroDeJogo = new TabuleiroDeJogo(this);
        getPreferences();
        if(sharedPreferences.getBoolean("dois_jogadores",false)){
            textViewJOG1.setText("Jogador1: ");
            textViewJOG2.setText("Jogador2: ");

            textViewPontJOG1.setText("Jogador1: ");
            TextViewPontJOG2.setText("Jogador2: ");
        }
        ll.addView(tabuleiroDeJogo);
        //setContentView(tabuleiroDeJogo);
        tabuleiroDeJogo.invalidate();
        //tabuleiroDeJogo.run();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if(MainActivity.mServ != null ) if (MainActivity.mServ.mPlayer!= null){
            if(MainActivity.musicaOn)
                MainActivity.mServ.pauseMusic();
        }
        MainActivity.outraAtividade = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean screenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        } else {
            screenOn = pm.isScreenOn();
        }

        if (screenOn) {
            if (MainActivity.mServ != null) {
                if (MainActivity.mServ.mPlayer == null) {
                    MainActivity.mServ.startMusic();
                }
                if(MainActivity.musicaOn){
                    MainActivity.mServ.resumeMusic();}
                else MainActivity.mServ.pauseMusic();
            }
        }
        MainActivity.outraAtividade = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(MainActivity.mServ != null ) if (MainActivity.mServ.mPlayer!= null){
            if(!MainActivity.musicaOn)
                MainActivity.mServ.pauseMusic();

        }
        MainActivity.outraAtividade = false;
        AlertDialog dialog = tabuleiroDeJogo.getDialog();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (MainActivity.mServ != null)
            if (MainActivity.mServ .mPlayer == null)
                if(MainActivity.musicaOn)
                     MainActivity.mServ .pauseMusic();
    }

    public void restart() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.tabuleiroLayout);
        ((ViewGroup)tabuleiroDeJogo.getParent()).removeView(tabuleiroDeJogo);
        tabuleiroDeJogo = new TabuleiroDeJogo(this);
        getPreferences();
        ll.addView(tabuleiroDeJogo);
        //setContentView(tabuleiroDeJogo);
        tabuleiroDeJogo.invalidate();
    }

    public void increaseCpuVic(){
        cpuPont++;
        cpuPontText.setText(""+cpuPont);
    }
    public void increaseUserVic(){
        userPont++;
        userPontText.setText(""+
                userPont);
    }

    public void getPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getBoolean("ordem_jogar",true)){
            imageView.setBackgroundColor(Color.LTGRAY);
            imageView2.setBackgroundColor(Color.rgb(123,167,123));}
        else{
            imageView.setBackgroundColor(Color.rgb(123,167,123));
            imageView2.setBackgroundColor(Color.LTGRAY);}
    }
}
