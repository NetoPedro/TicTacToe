package com.trimteam.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectGameModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_mode);
        Button buttonSingle  = (Button) findViewById(R.id.button_single_player);
        Button buttonMulti  = (Button) findViewById(R.id.button_multiplayer);
        buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TabuleiroActivity.class);
                i.putExtra("multi",true);
                i.putExtra("level",1);
                startActivity(i);
            }
        });
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SelectDificultyActivity.class);
                i.putExtra("multi",false);
                startActivity(i);

            }
        });
    }



}
