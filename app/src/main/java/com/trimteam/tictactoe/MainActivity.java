package com.trimteam.tictactoe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public  boolean mIsBound = false;
    public static MusicService mServ;
    private ImageView musicMain;
    public static boolean outraAtividade = false;
    public static  Intent music;
    public static AudioManager am;
    public static Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            mServ.pauseMusic();
        }
    };
    public static AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        mServ.pauseMusic();
                        // Wait 30 seconds before stopping playback
                        mHandler.postDelayed(mDelayedStopRunnable, 30 * 1000);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        mServ.pauseMusic();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mServ.mPlayer.setVolume(5,5);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        if (musicaOn ){
                            mServ.resumeMusic();
                             mServ.mPlayer.setVolume(20,20);
                        }
                    }
                }


            };
    public static Handler mHandler = new Handler();
    public static boolean musicaOn =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        musicaOn = (sharedPreferences.getBoolean("som_ligado",true));

    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageView play = (ImageView) findViewById(R.id.play);
        doBindService();

        //mServ = new MusicService();


        int result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ) {
            // Start playback
            if(mServ == null ) {
                this.doBindService();
                music = new Intent(this, MusicService.class);
                if (musicaOn)startService(music);
            }
            else if(mServ.mPlayer == null){
                    if(musicaOn)mServ.startMusic();

            }
               if(mServ != null ) if (mServ.mPlayer!= null){
                   if (musicaOn) mServ.resumeMusic();
                   else mServ.pauseMusic();
               }


            //musicMain = (ImageView) findViewById(R.id.musicMain);
            /*musicMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (musicaOn) {
                        if (mServ != null) {
                            if (mServ.mPlayer == null) {
                                mServ.startMusic();
                            }
                            mServ.pauseMusic();
                            musicaOn = false;
                            musicMain.setImageResource(R.drawable.mute);

                        }
                        } else {
                            if (mServ != null) {
                                if (mServ.mPlayer == null) {
                                    mServ.startMusic();
                                }
                                mServ.resumeMusic();
                                musicaOn = true;
                                musicMain.setImageResource(R.drawable.unmute);
                            }
                        }
                    }

            });*/
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TabuleiroActivity.class);
                startActivity(i);
                outraAtividade = true;
            }
        });
        ImageView opcoes = (ImageView) findViewById(R.id.settings);
        opcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Opcoes.class);
                startActivity(i);
                outraAtividade = true;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mServ != null)
            if (mServ.mPlayer == null) {
                mServ.pauseMusic();
            }
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
            if (mServ != null) {
                if (mServ.mPlayer == null) {
                    mServ.startMusic();
                }
                if(musicaOn){
                    mServ.resumeMusic();}
                else mServ.pauseMusic();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServ.pauseMusic();
         doUnbindService();

         stopService(music);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if(!outraAtividade) {
            //doUnbindService();
            //stopService(music);
            mServ.pauseMusic();
            am.abandonAudioFocus(afChangeListener);
        }

    }

    @Override
    public void finish() {
        super.finish();
        mServ.pauseMusic();
        doUnbindService();

        am.abandonAudioFocus(afChangeListener);

    }



    public  ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    public  void doBindService(){
        Intent bindIntent = new Intent(this,MusicService.class);
        mIsBound = bindService(bindIntent,Scon,this.BIND_AUTO_CREATE);
    }



    public  void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }
}
