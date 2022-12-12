package cz.dzubera.prosebe.android

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class PlaySoundService : Service() {
    var mp: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.imagination);
        mp?.isLooping = false;
        mp?.start();
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop();
        mp?.release();
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}


