package br.brunodea.goclock.util;

public class Util {
	/**
	 * 
	 * @param time String formatada como 00:00:00
	 * @return tempo em milisegundos.
	 */
	public static long timeStringToMillis(String time) {
		int h = getHour(time);
		int m = getMinute(time);
		int s = getSecond(time);
		
		return (h*60*60*1000)+(m*60*1000)+(s*1000);
	}
	public static int getHour(String time) {
		String[] pieces=time.split(":");
		return(Integer.parseInt(pieces[0]));
	}

	public static int getMinute(String time) {
		String[] pieces=time.split(":");
		  
		return(Integer.parseInt(pieces[1]));
	}
	
	public static int getSecond(String time) {
		String[] pieces=time.split(":");
		return Integer.parseInt(pieces[2]);
	}
}
