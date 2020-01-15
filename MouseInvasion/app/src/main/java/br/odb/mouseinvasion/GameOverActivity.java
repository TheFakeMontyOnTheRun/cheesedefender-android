package br.odb.mouseinvasion;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class GameOverActivity extends Activity implements OnClickListener {

	private MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gameoverlayout);

		Button btn = findViewById(R.id.btnOk);
		btn.setOnClickListener(this);

		TextView tvTime;
		tvTime = findViewById(R.id.tvTime);

		tvTime.setText("You survived for: " + getIntent().getStringExtra("result") + " seconds! And you destroyed " + getIntent().getStringExtra("tally") + " invaders!");
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getIntent().getExtras().getBoolean("hasSound")) {
			mp = MediaPlayer.create(this, R.raw.sugarplumfairy);
			mp.start();
			mp.setLooping(true);
		}
	}

	@Override
	protected void onPause() {

		if (mp != null ) {
			mp.pause();
			mp.stop();
		}

		super.onPause();
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}
}
