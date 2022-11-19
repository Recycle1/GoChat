package com.example.gochat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class Store_information {
    public static void create_table(MyOpenHelper helper,String table){
        SQLiteDatabase db=helper.getWritableDatabase();
        try{
            db.rawQuery("select * from "+table+";",null);
        }catch (Exception e){
            db.execSQL("create table "+table+"(_id integer primary key autoincrement,text_from char(1),text varchar(171),time varchar(20))");
        }finally {
            db.close();
        }
    }
    public static String [] table_names(MyOpenHelper helper){
        SQLiteDatabase db=helper.getWritableDatabase();
        ArrayList <String> names=new ArrayList<>();
        Cursor cursor=db.rawQuery("select name from sqlite_master where type='table';",null);
        if(cursor!=null&&cursor.getCount()>0){
            while ((cursor.moveToNext())){
                names.add(cursor.getString(0));
            }
        }
        db.close();
        return names.toArray(new String[0]);
    }
    public static String[] search_recent(MyOpenHelper helper,String table){
        SQLiteDatabase db=helper.getWritableDatabase();
        String text=" ";
        String time=" ";
        try{
            Cursor cursor=db.rawQuery("select * from "+table+";",null);
            if(cursor!=null&&cursor.getCount()>0){
                while (cursor.moveToNext()){
                    text=cursor.getString(2);
                    time=cursor.getString(3);
                }
            }
            db.close();

        }catch (Exception e){

        }finally {
            db.close();
            String info[]=new String[2];
            info[0]=text;
            info[1]=time;
            return info;
        }
    }
    public static void add_information(MyOpenHelper helper,String table,String text_from,String text,String time){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into "+table+"(text_from,text,time)values(?,?,?);",new Object[]{text_from,text,time});
        db.close();
    }
    public static String [][] get_information(MyOpenHelper helper,String table){
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+table+";",null);
        ArrayList<String> text_from=new ArrayList<>();
        ArrayList<String> text=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                text_from.add(cursor.getString(1));
                text.add(cursor.getString(2));
            }
        }
        String information[][]=new String[2][text_from.size()];
        for(int i=0;i<text_from.size();i++){
            information[0][i]=new String();
            information[0][i]=text_from.get(i);
            information[1][i]=new String();
            information[1][i]=text.get(i);
        }
        db.close();
        return information;
    }
}
