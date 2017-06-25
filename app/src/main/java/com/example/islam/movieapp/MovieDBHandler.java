package com.example.islam.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by islam on 12/2/2016.
 */
public class MovieDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5 ;
    private static final String DATABASE_NAME = "movie.db";
    private static final String TABLE_NAME = "movies";
    private static final String COLUMN_ID = "id";
    private static final String POSTER = "poster";
    private Context context ;
    public MovieDBHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //Toast.makeText(context, "onCreate method", Toast.LENGTH_LONG).show();
            String query = "CREATE TABLE " + TABLE_NAME +
                    "( " +
                    COLUMN_ID + " TEXT PRIMARY KEY ," +
                    POSTER + " TEXT " +
                    ");";
            db.execSQL(query);
        } catch (SQLException e)
        {
            //Toast.makeText(context,"due to: "+e,Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            //Toast.makeText(context,"onUpgrade method",Toast.LENGTH_LONG).show();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }catch (SQLException e)
        {
           // Toast.makeText(context,"due to: "+e,Toast.LENGTH_LONG).show();
        }
    }

    public void addMovie(DBTable table) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, table.getId());
        values.put(POSTER,table.getPoster());
        SQLiteDatabase db = getWritableDatabase();

        try {
            //Toast.makeText(context,"addMovie method",Toast.LENGTH_LONG).show();

            long rowId = db.insert(TABLE_NAME, null, values);

        }catch (SQLException e)
        {
            //Toast.makeText(context,"due to add : "+e,Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }
    public void deleteMovie(String movie_id)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {
//            Toast.makeText(context,"deleteMovie method",Toast.LENGTH_LONG).show();

            db.execSQL("DELETE FROM "+TABLE_NAME + " WHERE "+COLUMN_ID + "=\"" + movie_id+"\" ;");

        }catch (SQLException e)
        {
            //Toast.makeText(context,"due to delete: "+e,Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }

    public boolean isChecked(String id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME + " WHERE " + COLUMN_ID + " =\"" + id + "\"" ;
        Cursor c= db.rawQuery(query,null);
        int count = c.getCount();

        db.close();
        c.close();

        return count>0 ;

    }

    public ArrayList<DBTable> selectAll()
    {
        ArrayList<DBTable> tables = new ArrayList<DBTable>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1 ;";

        Cursor c = db.rawQuery(query,null);

        try {
      //      Toast.makeText(context,"databaseToString method",Toast.LENGTH_SHORT).show();

            c.moveToFirst();

            String row = "";
            DBTable t ;
            while (!c.isAfterLast())
            {

                t = new DBTable();
                t.setId(c.getString(c.getColumnIndex(COLUMN_ID)));
                t.setPoster(c.getString(c.getColumnIndex(POSTER)));
                tables.add(t);
                c.moveToNext();
            }

        }catch (SQLException e)
        {
          // Toast.makeText(context,"due to: "+e,Toast.LENGTH_LONG).show();
        }finally {
            c.close();
            db.close();
        }

        return tables ;
    }
}
