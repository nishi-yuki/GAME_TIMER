package jp.gr.javaConf.nishi1998ez.gameTimer

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private var isChrnoOn = false
    private var startTime:Long = 0
    private var gamePoint = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences("MAIN", Context.MODE_PRIVATE)

        gamePoint = pref.getInt("point", 3600)

        result_textView.text = "${gamePoint}"
        start_stop_button.text = getText(R.string.start)

        start_stop_button.setOnClickListener(this::onStartStopButtonClicked)
        my_chrono.setOnChronometerTickListener {
            result_textView.text = "${gamePoint - getTimerTime()}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pref.edit().commit()
    }

    private fun getTimerTime() = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()

    private fun onStartStopButtonClicked(view: View) {
        if (!isChrnoOn) {
            startTime = SystemClock.elapsedRealtime()
            my_chrono.base = startTime
            my_chrono.start()
            start_stop_button.text = getText(R.string.stop)
            isChrnoOn = true
        } else {
            my_chrono.stop()
            gamePoint -= getTimerTime()
            result_textView.text = "${gamePoint}"
            val editor = pref.edit()
            editor.putInt("point", gamePoint)
            editor.commit()

            start_stop_button.text = getText(R.string.start)
            isChrnoOn = false
        }
    }
}
