package com.trimteam.tictactoe;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);
        boolean multi = getIntent().getExtras().getBoolean("multi");
        if(multi){
            intentCreator(multi,1);
        }

        TextView title = (TextView) findViewById(R.id.text_select_difficulty);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/zorque.ttf");
        title.setTypeface(typeface);


        Button buttonEasy = (Button) findViewById(R.id.button_easy);
        Button buttonMedium = (Button) findViewById(R.id.button_hard);
        Button buttonHard = (Button) findViewById(R.id.button_impossible);

        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCreator(false,1);
            }
        });
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCreator(false,2);
            }
        });
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCreator(false,3);
            }
        });
    }

    private void intentCreator(boolean multi,  int level){
        Intent i = new Intent(SelectDifficultyActivity.this,TabuleiroActivity.class);
        i.putExtra("multi",multi);
        i.putExtra("level",level);
        startActivity(i);

    }
}
