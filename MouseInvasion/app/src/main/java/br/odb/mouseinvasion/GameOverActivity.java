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

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.gameoverlayout );

        Button btn = (Button) findViewById( R.id.btnOk );
        btn.setOnClickListener( this );

        TextView tvTime;
        tvTime = (TextView) findViewById( R.id.tvTime );

        tvTime.setText( "You survived for: " + getIntent().getStringExtra( "result") + " seconds! And you destroyed " + getIntent().getStringExtra( "tally") + " invaders!" );
        mp = MediaPlayer.create( this, R.raw.sugarplumfairy );
        mp.start();
        mp.setLooping( true );
	}

    @Override
    protected void onDestroy() {

    	mp.pause();
    	mp.stop();
    	super.onDestroy();
    }

	@Override
	public void onClick(View arg0) {
		finish();
	}

}
