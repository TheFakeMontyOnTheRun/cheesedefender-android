package br.odb.mouseinvasion;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameStartActivity extends Activity implements OnClickListener {

	private MediaPlayer mp;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.startgamelayout);

		Button btn = findViewById(R.id.btnStartGame);
		btn.setOnClickListener(this);
	}

	@Override
	protected void onPause() {

		if (mp != null)
			mp.pause();

		mp = null;

		super.onPause();
	}

	@Override
	protected void onDestroy() {

		if (mp != null)
			mp.pause();

		mp = null;

		super.onDestroy();
	}

	@Override
	protected void onStop() {

		if (mp != null)
			mp.pause();

		mp = null;

		super.onStop();
	}


	@Override
	protected void onResume() {

		super.onResume();
		mp = MediaPlayer.create(this, R.raw.rvalkyri);
		mp.setLooping(true);
		mp.start();
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(getBaseContext(), CheeseDefenderActivity.class);
		startActivityForResult(intent, 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {

				String result = data.getStringExtra("result");

				Intent intent = new Intent(this, GameOverActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("result", result);
				bundle.putString("tally", data.getStringExtra("tally"));
				intent.putExtras(bundle);

				this.startActivity(intent);
			}
		}
	}
}
