package br.odb.mouseinvasion;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

public class CheeseDefenderActivity extends Activity implements Runnable {
	public static boolean running = true;
	private static CheeseDefenderActivity instance;
	private CheeseDefenderView gameView;

	public static CheeseDefenderActivity getInstance() {
		return instance;
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		running = true;
		gameView = new CheeseDefenderView(this);

		if (Build.VERSION.SDK_INT >= 29) {
			enterStickyImmersiveMode();
		} else {
			getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
		}

		setContentView(gameView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		running = true;
		Thread updateThread = new Thread(this);
		updateThread.start();
	}

	private void enterStickyImmersiveMode() {
		getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	public void onPause() {
		super.onPause();
		running = false;
	}

	@Override
	public void run() {
		long t0 = 0;
		long t1;
		while (running) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			t1 = System.currentTimeMillis();

			if (gameView != null/* && gameView.hasFocus() */) {
				gameView.update(t1 - t0);
				gameView.postInvalidate();
			}

			t0 = System.currentTimeMillis();
		}
	}
}