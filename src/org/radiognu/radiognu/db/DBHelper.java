package org.radiognu.radiognu.db;

import org.radiognu.radiognu.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;



public class DBHelper extends SQLiteOpenHelper {
    
	private static boolean existsTrackInfo = false;
    public DBHelper(Context context){
    	super(context,"trackdata",null,1);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE track (TITLE text, ARTIST text, ALBUM text, COUNTRY text, YEARLICENCE text, IMAGE text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}

	@SuppressLint("NewApi")
	public void saveTrackInfo(String title, String artist, String album, String country, String YearLicence, String image ){
		
		if (image == null)
			image = MainActivity.getImgRadioGNU();
				
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from track");
				
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO track VALUES (?,?,?,?,?,?)");
		SQLiteStatement stmt = db.compileStatement(query.toString());
		stmt.bindString(1, title);
		stmt.bindString(2, artist);
		stmt.bindString(3, album);
		stmt.bindString(4, country);
		stmt.bindString(5, YearLicence);
		stmt.bindString(6, image);
		long rowId= stmt.executeInsert();
		stmt.close();
		db.close();
		existsTrackInfo = true;
	}
	public static boolean isExistsTrackInfo() { 
		return existsTrackInfo;
	}
	
}
