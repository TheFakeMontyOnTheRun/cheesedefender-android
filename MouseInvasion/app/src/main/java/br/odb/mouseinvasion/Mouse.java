/**
 * 
 */
package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

/**
 * @author monty
 *
 */
public class Mouse extends GameObject {
	
	static MediaPlayer killSound;
	static MediaPlayer alarmSound;
	static Bitmap mouse1;
	static Bitmap mouse2;

	@Override
	public void update(long delta) {
		
		if ( !active )
			return;
		
		super.update(delta);
		super.position.x-= 5;
		
		if ( visible && super.position.x <= CheeseDefenderView.cheesePosition ) {
			super.visible = false;
			CheeseDefenderView.cheesePosition+= 5;
			alarmSound.start();			
		}			
	}
	
	public Mouse() {
		super();
		super.frameCount = 2;
		super.frames = new Bitmap[ frameCount ];
		
		if ( mouse1 == null )
			mouse1 = BitmapFactory.decodeResource( CheeseDefenderActivity.getInstance().getResources(), R.drawable.mouse1 );
		
		if ( mouse2 == null )
			mouse2 = BitmapFactory.decodeResource( CheeseDefenderActivity.getInstance().getResources(), R.drawable.mouse2 );
		
		super.frames[ 0 ] = mouse1;
		super.frames[ 1 ] = mouse2;
		
		if ( killSound == null )
			killSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.squeak );
		
		if ( alarmSound == null )
			alarmSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.cheesestolen );
	}
	
	public void kill() {
		active = false;
		visible = false;
		killSound.start();
	}
}