package com.trimteam.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link android.preference.PreferenceActivity} which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
public abstract class AppCompatPreferenceActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;
    private  boolean resumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        resumed = true;
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        resumed = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean valido = true;
                while(!resumed || !hasWindowFocus() ){
                    if (!resumed) {
                        valido = false;
                        break;
                    }
                }
                if(valido) {
                    if (MainActivity.mServ != null) {
                        if (MainActivity.mServ.mPlayer == null) {
                            MainActivity.mServ.startMusic();
                        }
                        if (MainActivity.musicaOn) {
                            MainActivity.mServ.resumeMusic();
                        } else MainActivity.mServ.pauseMusic();
                    }
                }
            }
        }).start();

        MainActivity.outraAtividade = true;
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        resumed = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.musicaOn = (sharedPreferences.getBoolean("som_ligado",true));
        if(MainActivity.mServ != null ) if (MainActivity.mServ.mPlayer!= null){
            if(!MainActivity.musicaOn)
                MainActivity.mServ.pauseMusic();
        }
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resumed = false;
        getDelegate().onDestroy();
        MainActivity.outraAtividade = false;

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        resumed = false;
        if(MainActivity.mServ != null ) if (MainActivity.mServ.mPlayer!= null){
            if(MainActivity.musicaOn)
                MainActivity.mServ.pauseMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumed = false;
        if (MainActivity.mServ != null)
            if (MainActivity.mServ .mPlayer != null)
                MainActivity.mServ .pauseMusic();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}
