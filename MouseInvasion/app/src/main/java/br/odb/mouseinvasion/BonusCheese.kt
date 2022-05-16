package br.odb.mouseinvasion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import kotlin.math.sqrt

open class BonusCheese(context: Context, soundEnabled: Boolean) : GameObject(context), Explosive {

	companion object {
		private var killSound: MediaPlayer? = null
		private var spawnSound: MediaPlayer? = null
		private var explodeSound: MediaPlayer? = null
		private var cheese: Bitmap? = null
	}

	init {
		if (cheese == null) cheese = BitmapFactory.decodeResource(
			context.resources,
			R.drawable.cheese
		)

		super.frames[0] = cheese

		if (soundEnabled) {
			if (killSound == null) killSound =
				MediaPlayer.create(context, R.raw.cheesestolen)
			if (spawnSound == null) spawnSound =
				MediaPlayer.create(context, R.raw.upgradeappears)
			if (explodeSound == null) explodeSound =
				MediaPlayer.create(context, R.raw.explosion)
		}

		if (spawnSound != null) {
			spawnSound!!.start()
		}
	}

	override var explosionTime: Long = 0

	override fun draw(canvas: Canvas, paint: Paint) {
		if (explosionTime > 0) {
			paint.style = Paint.Style.FILL_AND_STROKE
			paint.setARGB((255 * explosionTime / 2000).toInt(), 255, 255, 0)
			canvas.drawCircle(
				super.position.x.toFloat(),
				super.position.y.toFloat(),
				(explosionTime / 50).toFloat(),
				paint
			)
		} else {
			super.draw(canvas, paint)
		}
	}

	override fun isHit(go: GameObject): Boolean {

		val x: Int = go.position.x - position.x
		val y: Int = go.position.y - position.y

		return if (isExploding) sqrt((x * x + y * y).toDouble()) < explosionTime / 40.0f else sqrt(
			(x * x + y * y).toDouble()
		) < 20
	}

	override fun update(delta: Long) {

		if (!active) return

		if (isExploding) {
			explosionTime -= delta

			if (!isExploding) {
				active = false
			}
		}

		super.update(delta)
	}

	override fun kill() {
		active = false
		visible = false

		if (CheeseDefenderView.cheesePosition > 50) CheeseDefenderView.cheesePosition -= 50 else CheeseDefenderView.cheesePosition =
			0

		if (killSound != null) {
			killSound!!.start()
		}
	}

	override fun explode() {
		explosionTime = 2000
		if (explodeSound != null) {
			explodeSound!!.start()
		}
		kill()
	}

	override val isExploding: Boolean
		get() = explosionTime > 0

	override val isArmed: Boolean
		get() = true
}