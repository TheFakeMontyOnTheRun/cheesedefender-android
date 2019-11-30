package br.odb.mouseinvasion;

import android.graphics.Canvas;
import android.graphics.Paint;

public class GameObject extends Sprite {

	boolean active = true;

	@Override
	public void update(long delta) {
		super.update( delta );

	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw( canvas, paint );
	}

	public void kill() {
		// TODO Auto-generated method stub

	}
}
