package com.trimteam.tictactoe;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectGameModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_mode);

        TextView title = (TextView) findViewById(R.id.text_select_game_mode);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/zorque.ttf");
        title.setTypeface(typeface);

        Button buttonSingle  = (Button) findViewById(R.id.button_single_player);
        Button buttonMulti  = (Button) findViewById(R.id.button_multiplayer);
        buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TabuleiroActivity.class);
                i.putExtra("multi",true);
                i.putExtra("level",1);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SelectDifficultyActivity.class);
                i.putExtra("multi",false);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
