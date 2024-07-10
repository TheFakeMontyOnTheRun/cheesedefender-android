package br.odb.mouseinvasion

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {

    private var mp: MediaPlayer? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.gameoverlayout)

        val tvTime: TextView = findViewById(R.id.tvTime)

        tvTime.text =
            "You survived for: " + intent.getStringExtra("result") + " seconds! And you destroyed " + intent.getStringExtra(
                "tally"
            ) + " invaders!"
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras!!.getBoolean("hasSound")) {
            mp = MediaPlayer.create(this, R.raw.sugarplumfairy)
            mp!!.start()
            mp!!.isLooping = true
        }
    }

    override fun onPause() {
        if (mp != null) {
            mp!!.pause()
            mp!!.stop()
        }
        super.onPause()
    }
}