package br.odb.mouseinvasion

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max

class CheeseDefenderView(context: Context, enableSound: Boolean) : View(context), Updatable,
    OnTouchListener {

    private var t0: Long = 0
    private var t1: Long = 0
    private var timeSinceLastLaunch: Long = 1000
    private val paint: Paint = Paint()
    private val gameObjects = ArrayList<GameObject>()
    private val recycleList = ArrayList<GameObject>()
    private var gameTime: Long = 0
    private var tally: Long = 0

    private var isSoundEnabled = enableSound

    companion object {
        private const val SECONDS_2: Long = 1000

        var cheesePosition = 10
    }

    init {
        t0 = System.currentTimeMillis()
        setOnTouchListener(this)
    }

    private fun addEntity(x: Int, y: Int, kind: Int) {

        val go: GameObject = when (kind) {
            1 -> Rocket(context, isSoundEnabled)
            0 -> Mouse(context, isSoundEnabled)
            2 -> BonusCheese(context, isSoundEnabled)
            else -> BeamCheese(kind, width.toFloat(), height.toFloat(), context, isSoundEnabled)
        }

        if (x < cheesePosition) {
            (go as Explosive).explode()
        }
        go.position.x = x
        go.position.y = y
        gameObjects.add(go)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var go: GameObject
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.BLACK
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        drawBackground(canvas, paint)
        for (c in gameObjects.indices) {
            go = gameObjects[c]
            go.draw(canvas, paint)
        }
        paint.color = Color.YELLOW
        canvas.drawLine(
            cheesePosition.toFloat(),
            0f,
            cheesePosition.toFloat(),
            height.toFloat(),
            paint
        )
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.GREEN

        canvas.drawRect(
            0f,
            (height - height * timeSinceLastLaunch / SECONDS_2).toFloat(),
            10f,
            height.toFloat(),
            paint
        )

        paint.textSize = 20f
        canvas.drawText("Score: $tally", (width / 2).toFloat(), (height - 10).toFloat(), paint)
    }

    private fun drawBackground(canvas: Canvas, paint: Paint?) {
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint.color = -0x1000000
        canvas.drawCircle((width / 2).toFloat(), height * 1.5f, (width / 2).toFloat(), paint)
        paint.color = -0x1
        canvas.drawCircle((width / 2).toFloat(), (height / 4).toFloat(), 16f, paint)
        paint.style = Paint.Style.STROKE
        for (c in 0..14) {
            paint.color = -0x1000000 + c * (0xFF / 15)
            canvas.drawCircle(
                (width / 2).toFloat(),
                height * 1.5f - c,
                (width / 2).toFloat(),
                paint
            )
        }
        paint.color = -0x1
        canvas.drawCircle((width / 2).toFloat(), height * 1.5f - 16, (width / 2).toFloat(), paint)
    }

    override fun update(delta: Long) {
        var killed = false
        var rocket: Explosive
        var go: GameObject
        gameTime += delta
        timeSinceLastLaunch += delta
        for (c in gameObjects.indices) {
            if (gameObjects[c].active) gameObjects[c].update(delta)
        }
        for (c in gameObjects.indices) {
            if (!gameObjects[c].active) {
                recycleList.add(gameObjects[c])
                continue
            }
            if (gameObjects[c] is Explosive) {
                rocket = gameObjects[c] as Explosive
                if (rocket.isArmed) {
                    for (d in gameObjects.indices) {
                        go = gameObjects[d]
                        if (go === rocket) continue
                        if (!go.active) continue
                        if (rocket.isHit(go)) {
                            if (gameObjects[d] is Mouse) {
                                tally++
                                gameObjects[d].kill()
                                if (!rocket.isExploding) rocket.explode()
                                killed = true
                            } else if (gameObjects[d] is Explosive) {
                                if (!(gameObjects[d] as Explosive)
                                        .isExploding
                                ) (gameObjects[d] as Explosive).explode()
                            }
                        }
                    }
                }
            }
        }
        for (g in recycleList) {
            gameObjects.remove(g)
        }
        recycleList.clear()
        if (gameTime % 5 == 0L) {
            addEntity(width, (Math.random() * height).toInt(), 0)
        }
        if (tally % 57 == 10L && killed) {
            addEntity(
                (Math.random() * width).toInt(),
                (Math.random() * height).toInt(), (Math.random() * 4 + 2).toInt()
            )
        }
        if (cheesePosition >= width) {
            (context as CheeseDefenderActivity?)!!.runOnUiThread {
                CheeseDefenderActivity.running = false
                t1 = System.currentTimeMillis()
                (context as CheeseDefenderActivity?)!!.intent.putExtra(
                    "result", "" + (t1 - t0) / 1000
                )
                (context as CheeseDefenderActivity?)!!.intent.putExtra(
                    "tally", "" + tally
                )
                (context as CheeseDefenderActivity?)!!.setResult(
                    AppCompatActivity.RESULT_OK,
                    (context as CheeseDefenderActivity?)!!.intent
                )
                (context as CheeseDefenderActivity?)!!.finish()
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (timeSinceLastLaunch < SECONDS_2) return true
        timeSinceLastLaunch = 0
        val go = addRocket(
            cheesePosition, event.y
                .toInt(),
            event.x.toInt(), event.y.toInt()
        )
        go.target.x = max(event.x, cheesePosition.toFloat())
            .toInt()
        go.target.y = event.y.toInt()
        postInvalidate()
        return true
    }

    private fun addRocket(x: Int, y: Int, targetX: Int, targetY: Int): Rocket {
        val toReturn = Rocket(context, isSoundEnabled)
        toReturn.position.x = x
        toReturn.position.y = y
        toReturn.target.x = targetX
        toReturn.target.y = targetY
        gameObjects.add(toReturn)
        return toReturn
    }
}