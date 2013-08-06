package br.brunodea.goclock.db;

import android.util.Pair;

public class DBStructure {
	
	public static String createTablesScript() {
		return new TimeRulesTable().creationScript();
	}
	
	protected static abstract class BaseTable {
		public static final String ID_COLUMN = "id";
		
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
}
