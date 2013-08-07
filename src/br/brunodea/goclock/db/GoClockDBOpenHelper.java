package br.brunodea.goclock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GoClockDBOpenHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "goclock.db";
	public static final int DB_VERSION = 1;
	
	public GoClockDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBStructure.createTablesScript());
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
