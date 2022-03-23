package br.odb.mouseinvasion

import android.graphics.Canvas
import android.graphics.Paint

internal interface Renderable {
    fun draw(canvas: Canvas, paint: Paint)
}