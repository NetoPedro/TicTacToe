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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;

public class TabuleiroActivity extends AppCompatActivity {
    TabuleiroDeJogo tabuleiroDeJogo;
    HashMap<Posicao,ImageView> imagensTab;
    private SharedPreferences sharedPreferences;
    TextView imageView,imageView2, cpuPontText, userPontText;
    private int cpuPont = 0, userPont = 0;
    InterstitialAd mInterstitialAd;
    private boolean multi ;
    private boolean cpuLastWinner = false;
    private int level ;
    private static int played = 0;
    TextView textViewJOG1, textViewJOG2, textViewPontJOG1, TextViewPontJOG2;
    private boolean resumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabuleiro);
        level = getIntent().getExtras().getInt("level");
        multi = getIntent().getExtras().getBoolean("multi");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3410114126236036/3100209900");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        played++;
        resumed  =true;
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

        tabuleiroDeJogo = new TabuleiroDeJogo(this, cpuLastWinner,level,multi);
        getPreferences();
        if(multi){
            textViewJOG1.setText(R.string.multiPlayer1);
            textViewJOG2.setText(R.string.multiPlayer2);

            textViewPontJOG1.setText(R.string.multiPlayer1);
            TextViewPontJOG2.setText(R.string.multiPlayer2);
        }
        ll.addView(tabuleiroDeJogo);
        //setContentView(tabuleiroDeJogo);
        tabuleiroDeJogo.invalidate();
        //tabuleiroDeJogo.run();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()

                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        resumed = false;

        MainActivity.outraAtividade = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;


        MainActivity.outraAtividade = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        resumed = false;

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
        resumed = false;
    }

    public void restart() {
        if (mInterstitialAd.isLoaded() && TabuleiroActivity.played ==4) {
            mInterstitialAd.show();
            played =0;
        }
            LinearLayout ll = (LinearLayout) findViewById(R.id.tabuleiroLayout);
            ((ViewGroup) tabuleiroDeJogo.getParent()).removeView(tabuleiroDeJogo);
            tabuleiroDeJogo = new TabuleiroDeJogo(this, cpuLastWinner, level,multi);
            getPreferences();
            ll.addView(tabuleiroDeJogo);
            //setContentView(tabuleiroDeJogo);
            tabuleiroDeJogo.invalidate();
        played++;


    }

    public void increaseCpuVic(){
        cpuPont++;
        cpuPontText.setText(""+cpuPont);
        cpuLastWinner = true;
    }
    public void increaseUserVic(){
        userPont++;
        userPontText.setText(""+
                userPont);
        cpuLastWinner = false;
    }

    public void getPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(!cpuLastWinner){
            imageView.setBackgroundColor(Color.LTGRAY);
            imageView2.setBackgroundColor(Color.rgb(123,167,123));}
        else{
            imageView.setBackgroundColor(Color.rgb(123,167,123));
            imageView2.setBackgroundColor(Color.LTGRAY);}
    }
}
