package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * @author monty
 */
public class Sprite implements Renderable, Updatable {
	public final Point position;
	Bitmap[] frames;
	int frameCount = 1;
	boolean visible;
	private int frame = 0;

	Sprite() {
		position = new Point(0, 0);
		frames = new Bitmap[1];
		frames[0] = BitmapFactory.decodeResource(CheeseDefenderActivity.getInstance().getResources(), R.drawable.icon);
		visible = true;
	}


	/* (non-Javadoc)
	 * @see br.odb.mouseinvasion.Updatable#update(long)
	 */
	@Override
	public void update(long delta) {

	}

	/* (non-Javadoc)
	 * @see br.odb.mouseinvasion.Renderable#draw(android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	public void draw(Canvas canvas, Paint paint) {
		if (visible) {

			canvas.drawBitmap(frames[frame], position.x, position.y, null);
			frame = (frame + 1) % frameCount;
		}

	}

}
