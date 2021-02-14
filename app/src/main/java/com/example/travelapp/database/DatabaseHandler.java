package com.example.travelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.travelapp.R;
import com.example.travelapp.model.DirectionsData;
import com.example.travelapp.model.RecentPlacesData;
import com.example.travelapp.model.TopPlacesData;
import com.example.travelapp.model.UsersData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "travelapp.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_TABLE_1 = "CREATE TABLE " + DirectionsEntry.TABLE_NAME + " ("
                + DirectionsEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // THIS AUTOMATICALLY INCREMENTS THE ID BY 1
                + DirectionsEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, "
                + DirectionsEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, "
                + DirectionsEntry.COLUMN_PRICE + " TEXT NOT NULL, "
                + DirectionsEntry.COLUMN_RATING + " DECIMAL NOT NULL, "
                + DirectionsEntry.COLUMN_IMAGE  + " INTEGER, "
                + DirectionsEntry.COLUMN_CREATED_AT + " DATETIME NOT NULL, "
                + DirectionsEntry.COLUMN_UPDATED_AT + " DATETIME NOT NULL);";
        db.execSQL(SQL_TABLE_1);

        String SQL_TABLE_2 = "CREATE TABLE " + UsersEntry.TABLE_NAME + " ("
                + UsersEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // THIS AUTOMATICALLY INCREMENTS THE ID BY 1
                + UsersEntry.COLUMN_USERNAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_CREATED_AT + " DATETIME NOT NULL, "
                + UsersEntry.COLUMN_UPDATED_AT + " DATETIME NOT NULL);";
        db.execSQL(SQL_TABLE_2);

        String SQL_TABLE_3 = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " ("
                + FavoritesEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // THIS AUTOMATICALLY INCREMENTS THE ID BY 1
                + FavoritesEntry.COLUMN_USER_ID + " INTEGER NOT NULL, "
                + FavoritesEntry.COLUMN_DIRECTION_ID + " INTEGER NOT NULL, "
                + FavoritesEntry.COLUMN_CREATED_AT + " DATETIME NOT NULL, "
                + FavoritesEntry.COLUMN_UPDATED_AT + " DATETIME NOT NULL, "
                + "FOREIGN KEY("+FavoritesEntry.COLUMN_USER_ID+") REFERENCES "+ UsersEntry.TABLE_NAME+"("+ UsersEntry.ID+"), "
                + "FOREIGN KEY("+FavoritesEntry.COLUMN_DIRECTION_ID+") REFERENCES "+DirectionsEntry.TABLE_NAME+"("+DirectionsEntry.ID+") );";
        db.execSQL(SQL_TABLE_3);

        insertDummyData(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DirectionsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UsersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }

    public List<DirectionsData> getAllDirections() {
        List<DirectionsData> directionsList= new ArrayList<DirectionsData>();
        String selectQuery= "SELECT * FROM directions";
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DirectionsData data= new DirectionsData(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4),cursor.getInt(5));
                directionsList.add(data);
            }while(cursor.moveToNext());
        }
        return directionsList;
    }

    public List<TopPlacesData> getAllTopPlaces() {
        List<TopPlacesData> topPlacesList= new ArrayList<TopPlacesData>();
        String selectQuery= "SELECT * FROM directions ORDER BY rating DESC LIMIT 3";
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TopPlacesData data= new TopPlacesData( cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4),cursor.getInt(5));
                topPlacesList.add(data);
            }while(cursor.moveToNext());
        }
        return topPlacesList;
    }


    public List<RecentPlacesData> getAllRecentPlaces() {
        List<RecentPlacesData> recentPlacesList= new ArrayList<RecentPlacesData>();
        String selectQuery= "SELECT * FROM directions ORDER BY created_at DESC LIMIT 5";
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RecentPlacesData data= new RecentPlacesData( cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4),cursor.getInt(5));
                recentPlacesList.add(data);
            }while(cursor.moveToNext());
        }
        return recentPlacesList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertDirection(DirectionsData data){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DirectionsEntry.COLUMN_PLACE_NAME,data.getPlaceName());
        values.put(DirectionsEntry.COLUMN_COUNTRY_NAME,data.getCountryName());
        values.put(DirectionsEntry.COLUMN_PRICE,data.getPrice());
        values.put(DirectionsEntry.COLUMN_RATING,data.getRating());
        values.put(DirectionsEntry.COLUMN_IMAGE,data.getImageUrl());
        values.put(DirectionsEntry.COLUMN_CREATED_AT,formatDate(LocalDateTime.now()));
        values.put(DirectionsEntry.COLUMN_UPDATED_AT,formatDate(LocalDateTime.now()));
        db.insert(DirectionsEntry.TABLE_NAME,null,values);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDirection(DirectionsData data, int dirId){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DirectionsEntry.COLUMN_PLACE_NAME,data.getPlaceName());
        values.put(DirectionsEntry.COLUMN_COUNTRY_NAME,data.getCountryName());
        values.put(DirectionsEntry.COLUMN_PRICE,data.getPrice());
        values.put(DirectionsEntry.COLUMN_RATING,data.getRating());
        values.put(DirectionsEntry.COLUMN_IMAGE,data.getImageUrl());
        values.put(DirectionsEntry.COLUMN_UPDATED_AT,formatDate(LocalDateTime.now()));
        db.update(DirectionsEntry.TABLE_NAME,values,DirectionsEntry.ID+" = ?", new String[] { String.valueOf(dirId) });
        db.close();
    }

    public void deleteDirection(int dirId) {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(DirectionsEntry.TABLE_NAME, DirectionsEntry.ID + " = ?",new String[] { String.valueOf(dirId) });
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertUser(UsersData data){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UsersEntry.COLUMN_USERNAME,data.getUsername());
        values.put(UsersEntry.COLUMN_FIRST_NAME,data.getFirstName());
        values.put(UsersEntry.COLUMN_LAST_NAME,data.getLastName());
        values.put(UsersEntry.COLUMN_PASSWORD,data.getPassword());
        values.put(UsersEntry.COLUMN_CREATED_AT,formatDate(LocalDateTime.now()));
        values.put(UsersEntry.COLUMN_UPDATED_AT,formatDate(LocalDateTime.now()));
        db.insert(UsersEntry.TABLE_NAME,null,values);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUser(UsersData data, int userId){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UsersEntry.COLUMN_USERNAME,data.getUsername());
        values.put(UsersEntry.COLUMN_FIRST_NAME,data.getFirstName());
        values.put(UsersEntry.COLUMN_LAST_NAME,data.getLastName());
        values.put(UsersEntry.COLUMN_PASSWORD,data.getPassword());
        values.put(UsersEntry.COLUMN_UPDATED_AT,formatDate(LocalDateTime.now()));
        db.update(UsersEntry.TABLE_NAME,values,UsersEntry.ID+" = ?", new String[] { String.valueOf(userId) });
        db.close();
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(UsersEntry.TABLE_NAME, UsersEntry.ID + " = ?",new String[] { String.valueOf(userId) });
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDate(LocalDateTime dateObj){
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fDate = dateObj.format(formatObj);
        return  fDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertDummyData(SQLiteDatabase db){
        List<DirectionsData> directionsList=new ArrayList<DirectionsData>();

        directionsList.add(new RecentPlacesData("AM Lake","India","From $200",4.5,R.drawable.recentimage1));
        directionsList.add(new RecentPlacesData("Nilgiri Hills","India","From $300",4.3,R.drawable.recentimage2));
        directionsList.add(new RecentPlacesData("AM Lake","India","From $200",4,R.drawable.recentimage1));
        directionsList.add(new RecentPlacesData("Nilgiri Hills","India","From $300",4.2,R.drawable.recentimage2));
        directionsList.add(new RecentPlacesData("AM Lake","India","From $200",4.1,R.drawable.recentimage1));
        directionsList.add(new RecentPlacesData("Nilgiri Hills","India","From $300",3.9,R.drawable.recentimage2));

        directionsList.add(new TopPlacesData("Kasimir Hill","India","$200 - $500",3.8, R.drawable.topplaces));
        directionsList.add(new TopPlacesData("Kasimir Hill","India","$200 - $500",4.9,R.drawable.topplaces));
        directionsList.add(new TopPlacesData("Kasimir Hill","India","$200 - $500",4.7, R.drawable.topplaces));
        directionsList.add(new TopPlacesData("Kasimir Hill","India","$200 - $500",3.6,R.drawable.topplaces));

        for(DirectionsData data:directionsList) {
            try {
                Thread.sleep(2500);//pour decaler de temps de creation de chaque element
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ContentValues values=new ContentValues();
            values.put(DirectionsEntry.COLUMN_PLACE_NAME,data.getPlaceName());
            values.put(DirectionsEntry.COLUMN_COUNTRY_NAME,data.getCountryName());
            values.put(DirectionsEntry.COLUMN_PRICE,data.getPrice());
            values.put(DirectionsEntry.COLUMN_RATING,data.getRating());
            values.put(DirectionsEntry.COLUMN_IMAGE,data.getImageUrl());
            values.put(DirectionsEntry.COLUMN_CREATED_AT,formatDate(LocalDateTime.now()));
            values.put(DirectionsEntry.COLUMN_UPDATED_AT,formatDate(LocalDateTime.now()));
            db.insert(DirectionsEntry.TABLE_NAME,null,values);
        }
    }
}
