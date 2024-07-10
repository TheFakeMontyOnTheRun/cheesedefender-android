package br.odb.mouseinvasion

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class CheeseDefenderActivity : AppCompatActivity(), Runnable {

    private var gameView: CheeseDefenderView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        running = true
        gameView = CheeseDefenderView(this, intent.extras!!.getBoolean("hasSound"))

        if (Build.VERSION.SDK_INT >= 29) {
            enterStickyImmersiveMode()
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        running = true
        val updateThread = Thread(this)
        updateThread.start()
    }

    private fun enterStickyImmersiveMode() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    public override fun onPause() {
        super.onPause()

        running = false
    }

    override fun run() {
        var t0: Long = 0
        var t1: Long

        while (running) {
            try {
                Thread.sleep(150)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            t1 = System.currentTimeMillis()
            if (gameView != null) {
                gameView!!.update(t1 - t0)
                gameView!!.postInvalidate()
            }
            t0 = System.currentTimeMillis()
        }
    }

    companion object {
        var running = true
    }
}