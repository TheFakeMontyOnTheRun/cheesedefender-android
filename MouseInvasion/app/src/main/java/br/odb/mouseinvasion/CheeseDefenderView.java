/**
 *
 */
package br.odb.mouseinvasion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author monty
 *
 */
public class CheeseDefenderView extends View implements Updatable,
		OnTouchListener {
	private static final long SECONDS_2 = 1000;
	private Paint paint;
	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> recycleList;
	private long gameTime;
	public static int cheesePosition;
	long t0;
	long t1;
	long timeSinceLastLaunch = 1000;
	private Context context;
	private long tally;
	private static CheeseDefenderView instance;

	public GameObject addEntity(int x, int y, int kind) {
		GameObject go;

		if (kind == 1)
			go = new Rocket();
		else if (kind == 0)
			go = new Mouse();
		else if  ( kind == 2 ) {
			go = new BonusCheese();
		} else {
			go = new BeamCheese( kind );
		}

		if ( x < cheesePosition && go instanceof Explosive ) {
			( ( Explosive ) go ).explode();
		}

		go.position.x = x;
		go.position.y = y;
		gameObjects.add(go);

		return go;
	}

	public void init(Context context) {
		instance = this;
		this.context = context;
		gameTime = 0;
		t0 = System.currentTimeMillis();
		cheesePosition = 0;
		paint = new Paint();
		gameObjects = new ArrayList<GameObject>();
		recycleList = new ArrayList<GameObject>();
		setOnTouchListener(this);
	}

	/**
	 * @param context
	 */
	public CheeseDefenderView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CheeseDefenderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CheeseDefenderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		GameObject go;

		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.BLACK);

		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

		drawBackground( canvas, paint );

		for (int c = 0; c < gameObjects.size(); ++c) {
			go = gameObjects.get(c);
			go.draw(canvas, paint);
		}
		paint.setColor(Color.YELLOW);
		canvas.drawLine(cheesePosition, 0, cheesePosition, getHeight(), paint);
	}

	private void drawBackground( Canvas canvas, Paint paint ) {

		paint.setStyle(Style.FILL_AND_STROKE );
		paint.setColor( 0xFF000000 );
		canvas.drawCircle( getWidth() / 2, getHeight() * 1.5f, getWidth() / 2, paint );

		paint.setColor( 0xFFFFFFFF );
		canvas.drawCircle( getWidth() / 2, getHeight() / 4, 16, paint );

		paint.setStyle(Style.STROKE);

		for ( int c = 0; c < 15; ++c ) {

			paint.setColor( 0xFF000000 + ( c * ( 0xFF / 15 ) ) );
			canvas.drawCircle( getWidth() / 2, (getHeight() * 1.5f) - c, getWidth() / 2, paint );
		}

		paint.setColor( 0xFFFFFFFF );
		canvas.drawCircle( getWidth() / 2, (getHeight() * 1.5f) - 16, getWidth() / 2, paint );

	}

	@Override
	public void update(long delta) {
		boolean killed = false;
		Explosive rocket;
		GameObject go;

		gameTime += delta;
		timeSinceLastLaunch += delta;

		for (int c = 0; c < gameObjects.size(); ++c) {

			if (gameObjects.get(c).active)
				gameObjects.get(c).update(delta);

		}

		for (int c = 0; c < gameObjects.size(); ++c) {

			if (!gameObjects.get(c).active) {
				recycleList.add(gameObjects.get(c));
				continue;
			}


			if ( gameObjects.get(c) instanceof Explosive ) {

				rocket = (Explosive) gameObjects.get(c);

				if ( rocket.isArmed() ) {

					for (int d = 0; d < gameObjects.size(); ++d) {

						go = gameObjects.get(d);

						if ( go == rocket )
							continue;

						if (!go.active)
							continue;

						if ( rocket.isHit( go ) ) {

							if (gameObjects.get(d) instanceof Mouse) {
								tally++;
								gameObjects.get(d).kill();

								if ( !rocket.isExploding() )
									rocket.explode();

								killed = true;

							} else if (gameObjects.get(d) instanceof Explosive) {
								if (!((Explosive) gameObjects.get(d))
										.isExploding())
									((Explosive) gameObjects.get(d)).explode();
							}
						}
					}
				}
			}
		}

		for (GameObject g : recycleList) {
			gameObjects.remove(g);
		}

		recycleList.clear();

		if (gameTime % 5 == 0) {
			addEntity(getWidth(), (int) (Math.random() * getHeight()), 0);
		}

		if ( tally % 11 == 10 && killed ) {
			addEntity((int) (Math.random() * getWidth()),
					(int) (Math.random() * getHeight()), ( int )( ( Math.random() * 4 ) + 2 ) );
		}


		if (cheesePosition >= getWidth()) {

			((CheeseDefenderActivity) context).runOnUiThread(new Runnable() {

				public void run() {

					CheeseDefenderActivity.running = false;
					t1 = System.currentTimeMillis();

					((CheeseDefenderActivity) context).getIntent().putExtra(
							"result", "" + ((t1 - t0) / 1000));
					((CheeseDefenderActivity) context).getIntent().putExtra(
							"tally", "" + tally);
					((CheeseDefenderActivity) context).setResult(
							Activity.RESULT_OK,
							((CheeseDefenderActivity) context).getIntent());
					((CheeseDefenderActivity) context).finish();
				}
			});
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (timeSinceLastLaunch < SECONDS_2)
			return true;

		timeSinceLastLaunch = 0;

		GameObject go = addRocket(cheesePosition, (int) event.getY(),
				(int) event.getX(), (int) event.getY());

		((Rocket) go).target.x = (int) Math.max(event.getX(), cheesePosition);
		((Rocket) go).target.y = (int) event.getY();

		postInvalidate();

		return true;
	}

	private Rocket addRocket(int x, int y, int targetX, int targetY) {
		Rocket toReturn = new Rocket();
		toReturn.position.x = x;
		toReturn.position.y = y;
		toReturn.target.x = targetX;
		toReturn.target.y = targetY;
		gameObjects.add(toReturn);
		return toReturn;
	}

	public static CheeseDefenderView getInstance() {

		return instance;
	}
}
