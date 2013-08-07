package br.brunodea.goclock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import br.brunodea.goclock.db.DBStructure.PresetTable;
import br.brunodea.goclock.db.DBStructure.TimeRulesTable;
import br.brunodea.goclock.timerule.ByoYomiTimeRule;

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
		for(String script : DBStructure.createTablesScript()) {
			db.execSQL(script);
		}
		db.execSQL("INSERT INTO "+TimeRulesTable.TABLE_NAME+" VALUES(0,'"+ByoYomiTimeRule.BYOYOMI_KEY+"');");
		db.execSQL("INSERT INTO "+PresetTable.TABLE_NAME+" VALUES(0,0,'AGA','04:00:00','00:01:00','5');");
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
