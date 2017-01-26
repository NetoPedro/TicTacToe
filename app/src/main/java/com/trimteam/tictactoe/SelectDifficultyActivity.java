package com.trimteam.tictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectDifficultyActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);
        boolean multi = getIntent().getExtras().getBoolean("multi");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        TextView title = (TextView) findViewById(R.id.text_select_difficulty);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/zorque.ttf");
        title.setTypeface(typeface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button buttonEasy = (Button) findViewById(R.id.button_easy);
        Button buttonMedium = (Button) findViewById(R.id.button_hard);
        Button buttonHard = (Button) findViewById(R.id.button_impossible);
        int niveis = sharedPreferences.getInt("niveisPassados", 1);


        switch (niveis) {

            case 7: buttonHard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentCreator(false, 7);
                }
            });
                buttonHard.setEnabled(true);
            case 3 :buttonMedium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentCreator(false, 3);
                }
            });
                buttonMedium.setEnabled(true);
            default:buttonEasy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentCreator(false, 1);
                }
            });
                buttonEasy.setEnabled(true);
        }
    }

    private void intentCreator(boolean multi, int level){
        Intent i = new Intent(SelectDifficultyActivity.this,TabuleiroActivity.class);
        i.putExtra("multi",multi);
        i.putExtra("level",level);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
