package br.odb.mouseinvasion

import android.app.Application
import android.media.AudioManager

class CheeseDefenderApplication : Application() {
	fun mayEnableSound(): Boolean {
		val am = this.getSystemService(AUDIO_SERVICE) as AudioManager
		return (am.ringerMode == AudioManager.RINGER_MODE_NORMAL)
	}
}