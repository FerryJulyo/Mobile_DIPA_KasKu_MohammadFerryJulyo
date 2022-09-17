package com.ferry.bukukassaya;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "KasKu.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table users(id INTEGER primary key autoincrement, username TEXT, password TEXT)");
        DB.execSQL("INSERT INTO users(username, password) VALUES ('user','user')");
        DB.execSQL("create table cashFlow(id INTEGER primary key autoincrement,tipe TEXT, tanggal TEXT, nominal TEXT, keterangan TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists users");
        DB.execSQL("drop Table if exists cashFlow");
    }

    public  Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = DB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        return cursor.getCount() > 0;
    }

    public  Boolean checkPassword(String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = DB.rawQuery("Select * from users where password = ?", new String[] {password});
        return cursor.getCount() > 0;
    }

    public Boolean insertCashFlow(String tipe, String tanggal, String nominal, String keterangan){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tipe", tipe);
        contentValues.put("tanggal", tanggal);
        contentValues.put("nominal", nominal);
        contentValues.put("keterangan", keterangan);
        long result = DB.insert("cashFlow", null, contentValues);

        return result != -1;
    }

    public  Cursor showCashFlow(){
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("select * from cashFlow ORDER BY tanggal DESC", null);
    }

    public  Cursor showTotalUangMasuk(){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT SUM(nominal) AS totalMasuk FROM cashFlow  WHERE strftime('%m',tanggal) = strftime('%m',date('now')) AND strftime('%Y',tanggal) = strftime('%Y',date('now')) AND tipe = 'masuk'", null);
    }

    public  Cursor showTotalUangKeluar(){
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT SUM(nominal) AS totalKeluar FROM cashFlow  WHERE strftime('%m',tanggal) = strftime('%m',date('now')) AND strftime('%Y',tanggal) = strftime('%Y',date('now')) AND tipe = 'keluar'", null);
    }



    public Boolean changePassword(String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        Cursor cursor = DB.rawQuery("select * from users where id = 1", null);

        if (cursor.getCount()>0) {
            long result = DB.update("users", contentValues, "id = 1", null);
            return result != -1;
        }else{
            return false;
        }
    }
}
