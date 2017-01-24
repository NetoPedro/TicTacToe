package com.trimteam.tictactoe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.icu.util.TimeUnit;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public  boolean mIsBound = false;
    private  Rater rater ;
    static boolean focus = false;
    public static boolean resumed = false;
    private ImageView shareIcon;
    public static boolean outraAtividade = false;
    public static  Intent music;
    public static AudioManager am;
    public static Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };
    public static AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        // Wait 30 seconds before stopping playback
                        mHandler.postDelayed(mDelayedStopRunnable, 30 * 1000);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        if (musicaOn ){
                            if (resumed && focus) {
                            }
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
        rater = new Rater(getApplicationContext());
        android.app.AlertDialog ad = rater.show();
        if(ad!=null ) ad.show();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3410114126236036~1623476703");
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //musicaOn = (sharedPreferences.getBoolean("som_ligado",true));


        TextView title = (TextView) findViewById(R.id.text_main_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/gomarice_super_g_type_2.ttf");
        title.setTypeface(typeface);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageView play = (ImageView) findViewById(R.id.play);

        //mServ = new MusicService();

        focus = true;
        resumed = true;
        int result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);






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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SelectGameModeActivity.class);
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

        shareIcon = (ImageView) findViewById(R.id.share);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlToShare = "http://play.google.com/store/apps/details?id=com.trimteam.tictactoe";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

// As fallback, launch sharer.php in a browser
                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        resumed = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        //PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //boolean screenOn;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
         //   screenOn = pm.isInteractive();
        //} else {
        //    screenOn = pm.isScreenOn();
       // }
        //if (screenOn) {
            /**if (mServ != null) {
                if (mServ.mPlayer == null) {
                    mServ.startMusic();
                }
                if(musicaOn){
                    mServ.resumeMusic();}
                else mServ.pauseMusic();
            }**/
        //resumed = true;

        //}
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        focus = hasFocus;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resumed = false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        resumed = false;
        if(!outraAtividade) {
            //doUnbindService();
            //stopService(music);
            am.abandonAudioFocus(afChangeListener);
        }

    }

    @Override
    public void finish() {
        super.finish();
        resumed = false;


        am.abandonAudioFocus(afChangeListener);

    }






}
