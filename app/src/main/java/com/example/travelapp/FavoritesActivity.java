package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.travelapp.adapter.DirectionsAdapter;
import com.example.travelapp.adapter.FavoritesAdapter;
import com.example.travelapp.database.DatabaseHandler;
import com.example.travelapp.model.DirectionsData;
import com.example.travelapp.model.FavoritesData;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView favoritesRecycler;
    FavoritesAdapter favoritesAdapter;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        /*dbHandler=new DatabaseHandler(this);

        List<DirectionsData> favoritesDataList = dbHandler.getAllFavorites();
        setFavoritesRecycler(directionsDataList);*/

        ImageView homeSelector = (ImageView) findViewById(R.id.homeSelector);
        homeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this ,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView userSelector = (ImageView) findViewById(R.id.userSelector);
        userSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this ,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView dirSelector = (ImageView) findViewById(R.id.directionsSelector);
        dirSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this ,DirectionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setFavoritesRecycler(List<FavoritesData> favoritesDataList){
        favoritesRecycler = findViewById(R.id.favorites_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        favoritesRecycler.setLayoutManager(layoutManager);
        favoritesAdapter = new FavoritesAdapter(this, favoritesDataList);
        favoritesRecycler.setAdapter(favoritesAdapter);
    }

}