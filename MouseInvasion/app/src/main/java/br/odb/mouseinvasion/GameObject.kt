package br.odb.mouseinvasion

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point

abstract class GameObject(context: Context) : Renderable, Updatable {

    var active = true

    open fun kill() {}

    val position = Point(0, 0)
    var frames = arrayOf(BitmapFactory.decodeResource(context.resources, R.drawable.icon))
    var frameCount = 1
    var visible = true

    private var frame = 0

    override fun update(delta: Long) {}

    override fun draw(canvas: Canvas, paint: Paint) {
        if (visible) {
            canvas.drawBitmap(frames[frame]!!, position.x.toFloat(), position.y.toFloat(), null)
            frame = (frame + 1) % frameCount
        }
    }
}