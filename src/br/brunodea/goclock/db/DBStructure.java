package br.brunodea.goclock.db;

import java.util.ArrayList;

import android.util.Pair;

/**
 * Estrutura do DB
 * TimeRulesTable - time_rules(_id,name);
 * BasePresetTable - presets(_id, time_rule_id,name,main_time,extra_time,extra_info);
 * ByoYomiPresetTable - byoyomi_presets(_id, time_rule_id,name,main_time,extra_time,periods);
 * CanadianPresetTable - canadian_presets(_id,time_rule_id,name,main_time,extra_time,stones);
 * AbsolutePresetTable - absolute_presets(_id,time_rule_id,name,main_time,extra_time);
 */
@SuppressWarnings("unchecked")
public class DBStructure {
	public static String createTablesScript() {
		return new TimeRulesTable().creationScript()+"\n"+
				new ByoYomiPresetTable().creationScript()+"\n"+
				new CanadianPresetTable().creationScript()+"\n"+
				new AbsolutePresetTable().creationScript();
	}
	protected static abstract class BaseTable {
		public static final String ID_COLUMN = "_id";
		
		public abstract String tableName();
		protected String createScript(Pair<String,String>... cols) {
			String script = "create table if not exists " +
					tableName() + " (" + idColumn() + 
					" integer primary key autoincrement ";
			if(cols.length > 0) {
				script += ",";
			}
			for(int i = 0; i < cols.length; i++) {
				Pair<String,String> info = cols[i];
				script += info.first + " " + info.second;
				if(i+1 < cols.length) {
					script += ",";
				}
			}
			script += ");";
			
			
			return script;
		}
		/**
		 * 
		 * @return script de criação da tabela.
		 */
		public abstract String creationScript();
		protected Pair<String,String> createPair(String col, String info) {
			return new Pair<String,String>(col, info);
		}
		
		public String idColumn() {
			return BaseTable.ID_COLUMN;
		}
	}
	
	public static class TimeRulesTable extends BaseTable {
		/**
		 * Nome da tabela.
		 */
		public static final String TABLE_NAME = "time_rules";
		/**
		 * Colunas.
		 */
		public static final String TIME_RULE_COLUMN = "name";
		
		@Override
		public String tableName() {
			return TimeRulesTable.TABLE_NAME;
		}
		@Override
		public String creationScript() {
			return createScript(createPair(TIME_RULE_COLUMN, "text not null"));
		}
	}
	
	public abstract static class BasePresetTable extends BaseTable {
		public static final String TIME_RULE_COLUMN = "time_rule_id";
		public static final String PRESET_NAME_COLUMN = "name";
		public static final String MAIN_TIME = "main_time";
		public static final String EXTRA_TIME = "extra_time";
		
		protected abstract ArrayList<Pair<String,String>> specificCreationPairs();
		
		@Override
		public String creationScript() {
			ArrayList<Pair<String,String>> pairs = new ArrayList<Pair<String,String>>();
			pairs.add(createPair(TIME_RULE_COLUMN, "integer"));
			pairs.add(createPair(PRESET_NAME_COLUMN, "text not null"));
			pairs.add(createPair(MAIN_TIME, "text not null"));
			pairs.add(createPair(EXTRA_TIME, "text not null"));

			pairs.addAll(specificCreationPairs());
			
			pairs.add(createPair("foreign key ("+TIME_RULE_COLUMN+")", 
					"references "+TimeRulesTable.TABLE_NAME+"("+TimeRulesTable.ID_COLUMN+")"));
			
			return createScript((Pair<String, String>[]) pairs.toArray());
		}
	}
	
	public static class ByoYomiPresetTable extends BasePresetTable {
		public static final String TABLE_NAME = "byoyomi_presets";
		public static final String BYOYOMI_PERIODS_COLUMN = "periods";
		
		@Override
		public String tableName() {
			return TABLE_NAME;
		}

		@Override
		protected ArrayList<Pair<String, String>> specificCreationPairs() {
			ArrayList<Pair<String,String>> pairs = new ArrayList<Pair<String,String>>();
			pairs.add(createPair(BYOYOMI_PERIODS_COLUMN, "integer not null"));
			
			return pairs;
		}
	}
	public static class CanadianPresetTable extends BasePresetTable {
		public static final String TABLE_NAME = "canadian_presets";
		public static final String STONES_PER_ETRA_TIME = "stones";
		@Override
		protected ArrayList<Pair<String, String>> specificCreationPairs() {
			ArrayList<Pair<String,String>> pairs = new ArrayList<Pair<String,String>>();
			pairs.add(createPair(STONES_PER_ETRA_TIME, "integer not null"));
			return pairs;
		}

		@Override
		public String tableName() {
			return TABLE_NAME;
		}
	}
	public static class AbsolutePresetTable extends BasePresetTable {
		public static final String TABLE_NAME = "absolute_presets";
		@Override
		protected ArrayList<Pair<String, String>> specificCreationPairs() {
			return new ArrayList<Pair<String,String>>();
		}

		@Override
		public String tableName() {
			return TABLE_NAME;
		}
	}
}
