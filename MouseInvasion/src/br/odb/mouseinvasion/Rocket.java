/**
 * 
 */
package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.FloatMath;

/**
 * @author monty
 * 
 */
public class Rocket extends GameObject implements Explosive {

	public Point target;
	private long explosionTime;
	static MediaPlayer fireSound;
	static MediaPlayer explodeSound;
	static Bitmap targetMark;
	static Bitmap rocket1;
	static Bitmap rocket2;

	@Override
	public void update(long delta) {
		super.update(delta);

		if (isExploding()) {
			explosionTime -= delta;

			// n��o esta mais explodindo...
			if (!isExploding()) {
				active = false;
			}

		} else if (visible && super.position.x >= target.x) {
			explode();
		} else {
			super.position.x += 5;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {

		if (explosionTime > 0) {
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setARGB((int) ((255 * explosionTime) / 2000), 255, 0, 0);
			canvas.drawCircle(super.position.x, super.position.y,
					explosionTime / 50, paint);
		} else {
			super.draw(canvas, paint);

			if (visible)
				canvas.drawBitmap(targetMark, target.x, target.y, null);
		}
	}

	public Rocket() {
		super();
		target = new Point();
		explosionTime = 0;
		super.frameCount = 2;
		super.frames = new Bitmap[frameCount];

		if (rocket1 == null)
			rocket1 = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.rocket1);

		if (rocket2 == null)
			rocket2 = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.rocket2);

		super.frames[0] = rocket1;
		super.frames[1] = rocket2;

		if (targetMark == null) {
			targetMark = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.target);
		}

		if (fireSound == null)
			fireSound = MediaPlayer.create(
					CheeseDefenderActivity.getInstance(), R.raw.rocketlaunch);

		if (explodeSound == null)
			explodeSound = MediaPlayer.create(
					CheeseDefenderActivity.getInstance(), R.raw.explosion);

		fireSound.start();
	}

	public long getExplosionTime() {

		return explosionTime;
	}

	public void explode() {
		this.visible = false;
		explosionTime = 2000;
		explodeSound.start();
	}

	public boolean isExploding() {

		return explosionTime > 0;
	}

	@Override
	public Point getPosition() {
		return super.position;
	}

	@Override
	public boolean isHit(GameObject go) {
		int x = go.position.x - getPosition().x;
		int y = go.position.y - getPosition().y;

		return (FloatMath.sqrt((x * x) + (y * y)) < (getExplosionTime() / 50));
	}

	@Override
	public boolean isArmed() {
		return isExploding();
	}
}
