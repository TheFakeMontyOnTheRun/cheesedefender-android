package br.odb.mouseinvasion

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import kotlin.math.sqrt

class Rocket(context : Context, soundEnabled : Boolean) : GameObject(context), Explosive {

    val target: Point = Point()

    override var explosionTime: Long = 0

    companion object {
        const val EXPLOSION_TIME : Long = 4000

        private var fireSound: MediaPlayer? = null
        private var explodeSound: MediaPlayer? = null
        private var targetMark: Bitmap? = null
        private var rocket1: Bitmap? = null
        private var rocket2: Bitmap? = null
    }

    init {
        explosionTime = 0
        super.frameCount = 2
        super.frames = arrayOfNulls(frameCount)
        if (rocket1 == null) rocket1 = BitmapFactory.decodeResource(
            context.resources, R.drawable.rocket1
        )
        if (rocket2 == null) rocket2 = BitmapFactory.decodeResource(
            context.resources, R.drawable.rocket2
        )
        super.frames[0] = rocket1
        super.frames[1] = rocket2
        if (targetMark == null) {
            targetMark = BitmapFactory.decodeResource(
                context.resources, R.drawable.target
            )
        }
        if (soundEnabled) {
            if (fireSound == null) fireSound = MediaPlayer.create(
                context, R.raw.rocketlaunch
            )
            if (explodeSound == null) explodeSound = MediaPlayer.create(
                context, R.raw.explosion
            )
        }
        if (fireSound != null) {
            fireSound!!.start()
        }
    }

    override fun update(delta: Long) {
        super.update(delta)
        if (isExploding) {
            explosionTime -= delta
            if (!isExploding) {
                active = false
            }
        } else if (visible && super.position.x >= target.x) {
            explode()
        } else {
            super.position.x += 15
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if (explosionTime > 0) {
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.setARGB((255 * explosionTime / EXPLOSION_TIME).toInt(), 255, 0, 0)
            canvas.drawCircle(
                super.position.x.toFloat(), super.position.y.toFloat(), (
                        explosionTime / 50).toFloat(), paint
            )
        } else {
            super.draw(canvas, paint)
            if (visible) canvas.drawBitmap(
                targetMark!!,
                target.x.toFloat(),
                target.y.toFloat(),
                null
            )
        }
    }

    override fun explode() {
        visible = false
        explosionTime = EXPLOSION_TIME
        if (explodeSound != null) {
            explodeSound!!.start()
        }
    }

    override val isExploding: Boolean
        get() = explosionTime > 0

    override fun isHit(go: GameObject): Boolean {

        val x: Int = go.position.x - position.x
        val y: Int = go.position.y - position.y

        return sqrt((x * x + y * y).toDouble()) < explosionTime / 50.0f
    }

    override val isArmed: Boolean
        get() = isExploding
}