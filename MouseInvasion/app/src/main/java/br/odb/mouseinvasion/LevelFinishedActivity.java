/**
 *
 */
package br.odb.mouseinvasion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author monty
 *
 */
public class LevelFinishedActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.levelfinishedlayout);

		Button btn = (Button) findViewById(R.id.btnNextLevel);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}
}
