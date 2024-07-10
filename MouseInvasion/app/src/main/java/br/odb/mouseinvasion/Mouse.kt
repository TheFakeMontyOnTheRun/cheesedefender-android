package br.odb.mouseinvasion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import kotlin.math.sqrt

class Mouse(context: Context, soundEnabled: Boolean) : GameObject(context), Explosive {

    private var alive = true

    private val speed: Int = ((10 + tally) + Math.random() * 4).toInt()

    companion object {
        const val EXPLOSION_TIME: Long = 3000
        var tally = 0.0f
        private var killSound: MediaPlayer? = null
        private var alarmSound: MediaPlayer? = null
        private var mouse1: Bitmap? = null
        private var mouse2: Bitmap? = null
    }

    init {
        super.frameCount = 2
        super.frames = arrayOfNulls(frameCount)

        if (mouse1 == null) mouse1 = BitmapFactory.decodeResource(
            context.resources, R.drawable.mouse1
        )
        if (mouse2 == null) mouse2 = BitmapFactory.decodeResource(
            context.resources, R.drawable.mouse2
        )

        super.frames[0] = mouse1
        super.frames[1] = mouse2

        if (soundEnabled) {
            if (killSound == null) killSound =
                MediaPlayer.create(context, R.raw.squeak)
            if (alarmSound == null) alarmSound =
                MediaPlayer.create(context, R.raw.cheesestolen)
        }
    }

    override var explosionTime: Long = 0
        private set

    override fun update(delta: Long) {

        if (!active) return

        super.update(delta)

        if (alive) {
            super.position.x -= speed
            if (visible && super.position.x <= CheeseDefenderView.cheesePosition) {
                super.visible = false
                CheeseDefenderView.cheesePosition += 5
                if (alarmSound != null) {
                    alarmSound!!.start()
                }
            }
        }
        if (isExploding) {
            explosionTime -= delta
            if (!isExploding) {
                active = false
            }
        }
        if (!isExploding && !alive) {
            active = false
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if (explosionTime > 0) {
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.setARGB((255 * explosionTime / EXPLOSION_TIME).toInt(), 128, 0, 255)
            canvas.drawCircle(
                super.position.x.toFloat(), super.position.y.toFloat(), (
                        explosionTime / 50).toFloat(), paint
            )
        } else {
            super.draw(canvas, paint)
        }
    }

    override fun kill() {
        if (alive) {
            if (killSound != null) {
                killSound!!.start()
            }
            alive = false
            explode()
            tally += 1f
        }
    }

    override fun explode() {
        visible = false
        explosionTime = EXPLOSION_TIME
    }

    override val isExploding: Boolean
        get() = explosionTime > 0

    override fun isHit(go: GameObject): Boolean {

        val x: Int = go.position.x - position.x
        val y: Int = go.position.y - position.y

        return sqrt((x * x + y * y).toDouble()) + speed < explosionTime / 40.0f
    }

    override val isArmed: Boolean
        get() = isExploding
}