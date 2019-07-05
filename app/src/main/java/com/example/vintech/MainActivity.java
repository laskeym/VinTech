package com.example.vintech;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;

    private CardView scanCard;
    private CardView listCard;

    private ImageView scanImg;
    private ImageView listImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanCard = findViewById(R.id.scanCard);
        listCard = findViewById(R.id.listCard);

        scanCard.setOnClickListener(openActivityButtonClick);
        listCard.setOnClickListener(openActivityButtonClick);

        scanImg = findViewById(R.id.scanImg);
        int imageResource = getResources().getIdentifier("@drawable/ic_barcode_scanner", null, this.getPackageName());
        scanImg.setImageResource(imageResource);

        listImg = findViewById(R.id.listImg);
        int listImageResource = getResources().getIdentifier("@drawable/ic_task_list", null, this.getPackageName());
        listImg.setImageResource(listImageResource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.aboutItem:
                Toast.makeText(context, "About item selected!", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
