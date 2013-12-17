package org.radiognu.radiognu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "track.db";
    public static final int DB_VERSION = 1;
    
    public static final String TABLE = "info";
    public static final String C_ID = BaseColumns._ID;
    public static final String C_TITLE = "name";
    public static final String C_ARTIST = "artist";
    public static final String C_ALBUM = "album";
    public static final String C_COUNTRY = "country";
    public static final String C_YEARLICENCE = "yearlicence";
    public static final String C_IMAGE = "image";
    
    public static final int C_ID_INDEX = 0;
    public static final int C_NAME_INDEX = 1;
    public static final int C_SCREEN_NAME_INDEX = 2;
    public static final int C_IMAGE_PROFILE_URL_INDEX = 3;
    public static final int C_TEXT_INDEX = 4;
    public static final int C_CREATED_AT_INDEX = 5;
    
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		 String sql = "create table " + TABLE + " (" + C_ID + " int primary key, "
	               + C_TITLE + " text, " + C_ARTIST + " text, " +
	         C_ALBUM + " text, " + C_COUNTRY + " text, " + C_YEARLICENCE +  " text, " +  C_IMAGE  +  " text)";
	        db.execSQL(sql);
	        Log.d(TAG, "onCreated sql: " + sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("drop table if exists " + TABLE); // drops the old database
	        Log.d(TAG, "onUpdated");
	        onCreate(db); // run onCreate to get the new database
		
	}

}
