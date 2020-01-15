package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;

/**
 * @author monty
 */
public class Mouse extends GameObject implements Explosive {

	private static MediaPlayer killSound;
	private static MediaPlayer alarmSound;
	private static Bitmap mouse1;
	private static Bitmap mouse2;
	private boolean alive = true;
	private final int speed;
	private long explosionTime;

	public Mouse() {
		super();
		speed = (int) (5 + Math.random() * 4);
		super.frameCount = 2;
		super.frames = new Bitmap[frameCount];

		if (mouse1 == null)
			mouse1 = BitmapFactory.decodeResource(CheeseDefenderActivity.getInstance().getResources(), R.drawable.mouse1);

		if (mouse2 == null)
			mouse2 = BitmapFactory.decodeResource(CheeseDefenderActivity.getInstance().getResources(), R.drawable.mouse2);

		super.frames[0] = mouse1;
		super.frames[1] = mouse2;

		if (killSound == null)
			killSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.squeak);

		if (alarmSound == null)
			alarmSound = MediaPlayer.create(CheeseDefenderActivity.getInstance(), R.raw.cheesestolen);
	}

	@Override
	public void update(long delta) {

		if (!active)
			return;

		super.update(delta);

		if (alive) {
			super.position.x -= speed;

			if (visible && super.position.x <= CheeseDefenderView.cheesePosition) {
				super.visible = false;
				CheeseDefenderView.cheesePosition += 5;
				alarmSound.start();
			}

		}

		if (isExploding()) {
			explosionTime -= 3 * delta;

			if (!isExploding()) {
				active = false;
			}
		}


		if (!isExploding() && !alive) {
			active = false;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {

		if (explosionTime > 0) {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setARGB((int) ((255 * explosionTime) / 2000), 255, 0, 0);
			canvas.drawCircle(super.position.x, super.position.y,
					explosionTime / 50, paint);
		} else {
			super.draw(canvas, paint);
		}
	}


	public void kill() {
		if (alive) {
			killSound.start();
			alive = false;
			explode();
		}
	}

	@Override
	public long getExplosionTime() {
		return explosionTime;
	}

	@Override
	public void explode() {
		this.visible = false;
		explosionTime = 2000;
	}

	@Override
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

		return (Math.sqrt((x * x) + (y * y)) < (getExplosionTime() / 50.0f));
	}

	@Override
	public boolean isArmed() {
		return isExploding();
	}
}
