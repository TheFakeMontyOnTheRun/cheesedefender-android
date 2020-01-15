package br.odb.mouseinvasion

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button

class GameStartActivity : Activity(), View.OnClickListener {
    var mp: MediaPlayer? = null
    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startgamelayout)
        val btn = findViewById(R.id.btnStartGame) as Button
        btn.setOnClickListener(this)
    }

    override fun onPause() {
        if (mp != null) mp!!.pause()
        mp = null
        super.onPause()
    }

    override fun onDestroy() {
        if (mp != null) mp!!.pause()
        mp = null
        super.onDestroy()
    }

    override fun onStop() {
        if (mp != null) mp!!.pause()
        mp = null
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        mp = MediaPlayer.create(this, R.raw.rvalkyri)
        mp.setLooping(true)
        mp.start()
    }

    override fun onClick(arg0: View) {
        val intent = Intent(baseContext, CheeseDefenderActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val result = data.getStringExtra("result")
                val intent = Intent(this, GameOverActivity::class.java)
                val bundle = Bundle()
                bundle.putString("result", result)
                bundle.putString("tally", data.getStringExtra("tally"))
                intent.putExtras(bundle)
                this.startActivity(intent)
            }
        }
        if (resultCode == RESULT_CANCELED) { //Write your code on no result return
        }
    }
}