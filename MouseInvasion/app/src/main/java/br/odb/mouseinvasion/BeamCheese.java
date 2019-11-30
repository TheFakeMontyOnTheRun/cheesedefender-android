/**
 * 
 */
package br.odb.mouseinvasion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * @author monty
 * 
 */
public class BeamCheese extends BonusCheese {

	private int kind;
	private static Bitmap vertical;
	private static Bitmap horizontal;
	private static Bitmap multi;

	/**
	 * 
	 */
	public BeamCheese(int kind) {
		super();

		if (multi == null)
			multi = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.multi);

		if (vertical == null)
			vertical = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.vertical);

		if (horizontal == null)
			horizontal = BitmapFactory.decodeResource(CheeseDefenderActivity
					.getInstance().getResources(), R.drawable.horizontal);

		this.kind = kind;

		switch (kind) {
		case 3:
			super.frames[0] = horizontal;
			break;
		case 4:
			super.frames[0] = vertical;
			break;
		case 5:
			super.frames[0] = multi;
			break;
		}
	}

	@Override
	public boolean isHit(GameObject go) {
		boolean beamHit;

		if (isExploding()) {
			switch (kind) {
			case 3:
				beamHit = ((go.position.y >= (position.y - 10)) && (go.position.y <= (position.y + 10)));
				break;
			case 4:
				beamHit = ((go.position.x >= (position.x - 10)) && (go.position.x <= (position.x + 10)));
				break;
			default:
				beamHit = ((go.position.y >= (position.y - 10)) && (go.position.y <= (position.y + 10)));
				beamHit = beamHit
						|| (((go.position.x >= (position.x - 10)) && (go.position.x <= (position.x + 10))));
				break;
			}
		} else {
			beamHit = false;
		}
		return super.isHit(go) || beamHit;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {

		super.draw(canvas, paint);

		if (super.getExplosionTime() > 0) {
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setARGB((int) ((255 * getExplosionTime()) / 2000), 255, 255,
					0);

			if (kind == 3)
				canvas.drawLine(0, getPosition().y, CheeseDefenderView
						.getInstance().getWidth(), getPosition().y, paint);

			if (kind == 4)
				canvas.drawLine(getPosition().x, 0, getPosition().x,
						CheeseDefenderView.getInstance().getHeight(), paint);

			if (kind == 5) {
				canvas.drawLine(getPosition().x, 0, getPosition().x,
						CheeseDefenderView.getInstance().getHeight(), paint);
				canvas.drawLine(0, getPosition().y, CheeseDefenderView
						.getInstance().getWidth(), getPosition().y, paint);

			}
		}
	}
}
