package br.odb.mouseinvasion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

open class BeamCheese(private val kind: Int,
                      private val screenWidth : Float,
                      private val screenHeight : Float,
                      context : Context,
                      soundEnabled : Boolean) : BonusCheese(context, soundEnabled) {

    companion object {
        private var vertical: Bitmap? = null
        private var horizontal: Bitmap? = null
        private var multi: Bitmap? = null
    }

    init {
        if (multi == null) multi = BitmapFactory.decodeResource(
            context.resources, R.drawable.multi
        )
        if (vertical == null) vertical = BitmapFactory.decodeResource(
            context.resources, R.drawable.vertical
        )
        if (horizontal == null) horizontal = BitmapFactory.decodeResource(
            context.resources, R.drawable.horizontal
        )

        when (kind) {
            3 -> super.frames[0] = horizontal
            4 -> super.frames[0] = vertical
            5 -> super.frames[0] = multi
        }
    }

    override fun isHit(go: GameObject): Boolean {

        var beamHit: Boolean

        if (isExploding) {
            when (kind) {
                3 -> beamHit = go.position.y >= position.y - 10 && go.position.y <= position.y + 10
                4 -> beamHit = go.position.x >= position.x - 10 && go.position.x <= position.x + 10
                else -> {
                    beamHit = go.position.y >= position.y - 10 && go.position.y <= position.y + 10
                    beamHit = (beamHit
                            || go.position.x >= position.x - 10 && go.position.x <= position.x + 10)
                }
            }
        } else {
            beamHit = false
        }

        return super.isHit(go) || beamHit
    }

    override fun draw(canvas: Canvas, paint: Paint) {

        super.draw(canvas, paint)

        if (explosionTime > 0) {
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.setARGB(
                (255 * explosionTime / 2000).toInt(), 255, 255,
                0
            )
            if (kind == 3) canvas.drawLine(
                0f, position.y.toFloat(), screenWidth, position.y.toFloat(), paint
            )
            if (kind == 4) canvas.drawLine(
                position.x.toFloat(), 0f, position.x.toFloat(), screenHeight, paint
            )
            if (kind == 5) {
                canvas.drawLine(
                    position.x.toFloat(), 0f, position.x.toFloat(), screenHeight, paint
                )
                canvas.drawLine(
                    0f, position.y.toFloat(), screenWidth, position.y.toFloat(), paint
                )
            }
        }
    }
}