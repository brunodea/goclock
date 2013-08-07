package br.brunodea.goclock.db;

import android.util.Pair;

/**
 * Estrutura do DB
 * TimeRulesTable - time_rules(_id,name);
 * PresetTable - presets(_id, time_rule_id,name,main_time,extra_time,extra_info);
 */
@SuppressWarnings("unchecked")
public class DBStructure {
	public static String []createTablesScript() {
		return new String[] {new TimeRulesTable().creationScript(),
				new PresetTable().creationScript()};
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
		public static final String TABLE_NAME = "time_rules";
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
	
	public static class PresetTable extends BaseTable {
		public static final String TABLE_NAME = "presets";
		public static final String TIME_RULE_COLUMN = "time_rule_id";
		public static final String NAME = "name";
		public static final String MAIN_TIME = "main_time";
		public static final String EXTRA_TIME = "extra_time";
		public static final String EXTRA_INFO = "extra_info";
		
		@Override
		public String creationScript() {
			return createScript(
					createPair(TIME_RULE_COLUMN, "integer"),
					createPair(NAME, "text not null"),
					createPair(MAIN_TIME, "text not null"),
					createPair(EXTRA_TIME, "text not null"),
					createPair(EXTRA_INFO, "text"),
					createPair("foreign key ("+TIME_RULE_COLUMN+")", 
							"references "+TimeRulesTable.TABLE_NAME+"("+TimeRulesTable.ID_COLUMN+")"));
		}

		@Override
		public String tableName() {
			return TABLE_NAME;
		}
	}
}
