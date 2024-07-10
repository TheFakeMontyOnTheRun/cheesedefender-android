package br.odb.mouseinvasion

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class GameStartActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private var mp: MediaPlayer? = null
    private var chkSound: SwitchMaterial? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startgamelayout)
        val btn = findViewById<Button>(R.id.btnStartGame)
        btn.setOnClickListener(this)
        chkSound = findViewById(R.id.swEnableSound)
        chkSound!!.isChecked = (application as CheeseDefenderApplication).mayEnableSound()
        chkSound!!.setOnCheckedChangeListener(this)
    }

    override fun onPause() {
        if (mp != null) mp!!.pause()
        mp = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (chkSound!!.isChecked) {
            mp = MediaPlayer.create(this, R.raw.rvalkyri)
            mp!!.isLooping = true
            mp!!.start()
        }
    }

    override fun onClick(arg0: View) {
        val intent = Intent(baseContext, CheeseDefenderActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("hasSound", chkSound!!.isChecked)
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val result = data!!.getStringExtra("result")
                val intent = Intent(this, GameOverActivity::class.java)
                val bundle = Bundle()
                bundle.putBoolean("hasSound", chkSound!!.isChecked)
                bundle.putString("result", result)
                bundle.putString("tally", data.getStringExtra("tally"))
                intent.putExtras(bundle)
                this.startActivity(intent)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            mp = MediaPlayer.create(this, R.raw.rvalkyri)
            mp!!.isLooping = true
            mp!!.start()
        } else if (mp != null) {
            mp!!.stop()
            mp = null
        }
    }
}