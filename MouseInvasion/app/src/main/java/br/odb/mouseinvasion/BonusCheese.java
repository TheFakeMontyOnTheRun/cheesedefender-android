package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.media.MediaPlayer;

/**
 * @author monty
 */
public class BonusCheese extends GameObject implements Explosive {

	private static MediaPlayer killSound;
	private static MediaPlayer spawnSound;
	private static MediaPlayer explodeSound;
	private static Bitmap cheese;

	private long explosionTime;

	/**
	 *
	 */
	public BonusCheese() {
		super();


		if (cheese == null)
			cheese = BitmapFactory.decodeResource(CheeseDefenderActivity.getInstance().getResources(), R.drawable.cheese);

		super.frames[0] = cheese;

		if (killSound == null)
			killSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.cheesestolen);

		if (spawnSound == null)
			spawnSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.upgradeappears);

		if (explodeSound == null)
			explodeSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.explosion);

		spawnSound.start();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {

		if (explosionTime > 0) {
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setARGB((int) ((255 * explosionTime) / 2000), 255, 255, 0);
			canvas.drawCircle(super.position.x, super.position.y, explosionTime / 50, paint);
		} else {
			super.draw(canvas, paint);
		}
	}


	@Override
	public void update(long delta) {

		if (!active)
			return;

		if (isExploding()) {
			explosionTime -= delta;

			//não esta mais explodindo...
			if (!isExploding()) {
				active = false;
			}
		}


		super.update(delta);
	}

	public void kill() {

		active = false;
		visible = false;

		if (CheeseDefenderView.cheesePosition > 50)
			CheeseDefenderView.cheesePosition -= 50;
		else
			CheeseDefenderView.cheesePosition = 0;

		killSound.start();
	}


	public void explode() {
		explosionTime = 2000;
		explodeSound.start();
		kill();
	}

	public boolean isExploding() {

		return explosionTime > 0;
	}

	@Override
	public long getExplosionTime() {

		return explosionTime;
	}

	@Override
	public Point getPosition() {

		return position;
	}

	@Override
	public boolean isHit(GameObject go) {
		int x = go.position.x - getPosition().x;
		int y = go.position.y - getPosition().y;

		if (isExploding())
			return (Math.sqrt((x * x) + (y * y)) < (getExplosionTime() / 50.0f));
		else
			return (Math.sqrt((x * x) + (y * y)) < (20));
	}

	@Override
	public boolean isArmed() {
		return true;
	}
}
