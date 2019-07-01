package com.example.vintech;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Context context;

    private CardView scanCard;
    private CardView listCard;

    private ImageView scanImg;
    private Button scanButton;

    private ImageView listImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        scanCard = (CardView) findViewById(R.id.scanCard);
        listCard = (CardView) findViewById(R.id.listCard);

        scanCard.setOnClickListener(openActivityButtonClick);
        listCard.setOnClickListener(openActivityButtonClick);

        scanImg = (ImageView) findViewById(R.id.scanImg);
        int imageResource = getResources().getIdentifier("@drawable/ic_barcode_scanner", null, this.getPackageName());
        scanImg.setImageResource(imageResource);

        listImg = (ImageView) findViewById(R.id.listImg);
        int listImageResource = getResources().getIdentifier("@drawable/ic_task_list", null, this.getPackageName());
        listImg.setImageResource(listImageResource);
    }

    private View.OnClickListener openActivityButtonClick = new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.scanCard:
                    intent = new Intent(context, ScanActivity.class);
                    break;
                case R.id.listCard:
                    intent = new Intent(context, ListActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };
}
