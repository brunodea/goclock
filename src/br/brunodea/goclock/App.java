package br.brunodea.goclock;

import android.app.Application;

public class App extends Application {

	private static App sInstance = null;
	public static App instance() {
		return sInstance;
	}
	
	@Override
	public final void onCreate() {
		super.onCreate();
		sInstance = this;
	}
}
