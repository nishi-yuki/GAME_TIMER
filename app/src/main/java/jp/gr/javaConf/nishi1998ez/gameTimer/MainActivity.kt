package jp.gr.javaConf.nishi1998ez.gameTimer

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val INITIAL_POINT = 3600
const val EVERYDAY_POINT = 3600

class MainActivity : AppCompatActivity() {

    private lateinit var dtmg: DataManager
    private var isChrnoOn = false
    private var startTime:Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dtmg = DataManager(this)

        val time = dtmg.millisTimeSinceLastUse
        dtmg.point += (time * EVERYDAY_POINT / dtmg.ONE_DAY_MILLIS).toInt()

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
     * SQLにロジックを押し込むのはOK
     */
    private val pref: SharedPreferences =
            myAct.getSharedPreferences("MAIN", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    val ONE_DAY_MILLIS: Long = 1000 * 60 * 60 * 24

    var point : Int
        get() = pref.getInt("point", INITIAL_POINT)
        set(value) {
            editor.putInt("point", value)
            editor.apply()
        }

    /*
    var lastUsedDay : String
        get() = pref.getString("last_used_day", "2018-08-27")
        set(value) {
            editor.putString("last_used_day", value)
            editor.apply()
        }
    */
    val millisTimeSinceLastUse : Long

    init {
        val now = Date().time
        val lastTime = pref.getLong("last_used_time_millis", now)
        millisTimeSinceLastUse = now - lastTime
        editor.putLong("last_used_time_millis", now)
    }

    fun save() {
        editor.commit() //この関数いらない気がする
    }
}
