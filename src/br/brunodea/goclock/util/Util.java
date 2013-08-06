package br.brunodea.goclock.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.view.WindowManager;
import br.brunodea.goclock.preferences.GoClockPreferences;

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
	
	/**
	 * formata um valor de milisegundos para uma string do tipo 00:00:00
	 * @param millis
	 * @return
	 */
	public static String formattedTime(long millis) {
		NumberFormat nf = new DecimalFormat("00");
		int h = (int) TimeUnit.MILLISECONDS.toHours(millis);
		int m = (int) TimeUnit.MILLISECONDS.toMinutes(millis) -
				(int) TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
		int s = (int) TimeUnit.MILLISECONDS.toSeconds(millis) - 
				(int) TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
		
		return nf.format(h)+":"+nf.format(m)+":"+nf.format(s);
		
	}
	
	/**
	 * Ajusta o modo fullscreen em uma activity de acordo com as prefer�ncias do usu�rio.
	 * @param activity
	 */
	public static void adjustActivityFullscreenMode(Activity activity) {
		if(GoClockPreferences.getFullscreen()) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		else {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
	public static void adjustActivityFullscreenMode(Activity activity, boolean fullscreen) {
		if(fullscreen) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		else {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
	public static void adjustActivityKeepScreenOn(Activity activity) {
		if(GoClockPreferences.getKeepScreenOn()) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
}
