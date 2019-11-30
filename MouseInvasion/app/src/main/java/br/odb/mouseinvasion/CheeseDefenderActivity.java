package br.odb.mouseinvasion;

import android.app.Activity;
import android.os.Bundle;

public class CheeseDefenderActivity extends Activity implements Runnable {
	public static boolean running = true;
	private static CheeseDefenderActivity instance;
	private CheeseDefenderView gameView;
	private Thread updateThread;

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

		setContentView(gameView);

		updateThread = new Thread(this);
		updateThread.start();
	}

	public void onDestroy() {
		running = false;

		super.onDestroy();
	}

	@Override
	public void run() {
		long t0 = 0;
		long t1 = 0;
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