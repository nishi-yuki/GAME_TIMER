package jp.gr.javaConf.nishi1998ez.gameTimer

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

const val INITIAL_POINT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var dtmg: DataManager
    private var isChrnoOn = false
    private var startTime:Long = 0
    //private var gamePoint = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dtmg = DataManager(this)

        result_textView.text = "${dtmg.point}"
        start_stop_button.text = getText(R.string.start)

        start_stop_button.setOnClickListener(this::onStartStopButtonClicked)
        my_chrono.setOnChronometerTickListener {
            result_textView.text = "${dtmg.point - getTimerTime()}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dtmg.save()
    }

    fun getTimerTime() = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()

    private fun onStartStopButtonClicked(view: View) {
        if (!isChrnoOn) {
            startTime = SystemClock.elapsedRealtime()
            my_chrono.base = startTime
            my_chrono.start()
            start_stop_button.text = getText(R.string.stop)
            isChrnoOn = true
        } else {
            my_chrono.stop()
            dtmg.point -= getTimerTime()
            result_textView.text = "${dtmg.point}"

            start_stop_button.text = getText(R.string.start)
            isChrnoOn = false
        }
    }
}

class DataManager(myAct:MainActivity) {
    /**
     * データの保存と読み出しを行うクラス
     * それ以外のことをやらせてはいけない
     */
    private val pref: SharedPreferences =
            myAct.getSharedPreferences("MAIN", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    var point : Int
        get() = pref.getInt("point", INITIAL_POINT)
        set(value) {
            editor.putInt("point", value)
            editor.apply()
        }

    fun save() {
        editor.commit()
    }
}
