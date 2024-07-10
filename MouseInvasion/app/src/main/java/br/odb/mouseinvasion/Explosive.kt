package br.odb.mouseinvasion

import android.graphics.Point

internal interface Explosive {
    val explosionTime: Long
    val isExploding: Boolean
    val position: Point
    val isArmed: Boolean
    fun explode()
    fun isHit(go: GameObject): Boolean
}