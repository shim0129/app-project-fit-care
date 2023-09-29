package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class TimerActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    var timerRunning = false // 타이머 실행 상태
    private var firstState = true // 타이머 실행 처음인지 아닌지

    var tempTime = 0L // 임시 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val profile = findViewById<ImageView>(R.id.get_profile)
        val name = findViewById<TextView>(R.id.get_name)
        val time = findViewById<TextView>(R.id.tv_timer)
        val count = findViewById<TextView>(R.id.get_count)
        val real = findViewById<TextView>(R.id.tv_realcount)

        val img = intent.getStringExtra("profile")?.toIntOrNull() ?: 0
        profile.setImageResource(img)
        name.text = intent.getStringExtra("name")
        time.text = intent.getStringExtra("time") + ":00"
        count.text = " /" + intent.getStringExtra("count") + "set"
        real.text= "0"

        val btstart = findViewById<Button>(R.id.bt_start)
        val btcheck = findViewById<Button>(R.id.bt_check)
        val reset = findViewById<Button>(R.id.bt_reset)
        val finish = findViewById<Button>(R.id.bt_finish)
        val check = findViewById<Button>(R.id.bt_check)

        // start
        btstart.setOnClickListener {
            startStop()
        }

        // stop
        btcheck.setOnClickListener {
            startStop()
        }

        // cancel
        reset.setOnClickListener {
            resetTimer()
        }

        //check +1
        check.setOnClickListener {
            checkcount()
        }

        //길게 누르면 -1
        check.setOnLongClickListener{
            decount()
            true
        }

        //finish
        finish.setOnClickListener{
            congfinish()
        }

    }

    private fun startStop() {
        if (timerRunning) { // 실행 중이면 정지
            stopTimer()
        } else { // 정지면 실행
            startTimer()
        }
    }

    // 타이머 실행
    private fun startTimer() {
        val first = intent.getStringExtra("time") // 타이머 시간
        val time = findViewById<TextView>(R.id.tv_timer)
        var timer = 0L

        if (firstState) {
            val sMin = first.toString()
            val sSec = "0"

            timer = (sMin.toLong() * 60000) + (sSec.toLong() * 1000) + 1000
        } else { // 정지 후 이어서 시작이면 기존값 사용
            if(time.text=="00:00")
            {
                val sMin = first.toString()
                val sSec = "0"

                timer = (sMin.toLong() * 60000) + (sSec.toLong() * 1000) + 1000
            }
            else{timer = tempTime}
        }

        // 타이머 실행
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tempTime = millisUntilFinished // 타이머 업데이트
                updateTime()
            }

            override fun onFinish() {}
        }.start()

        val start = findViewById<Button>(R.id.bt_start)
        start.text = "Stop"
        timerRunning = true // 타이머 실행
        firstState = false // 처음 아님
    }

    // 타이머 정지
    private fun stopTimer() {
        if(timerRunning)
        {countDownTimer.cancel()} // 타이머 취소
        else{}
        timerRunning = false // 타이머 정지

        val start = findViewById<Button>(R.id.bt_start)
        start.text = "Start"
    }

    private fun updateTime() {
        val timerText = findViewById<TextView>(R.id.tv_timer)
        val min = tempTime / 60000
        val sec = (tempTime % 60000) / 1000

        val timeLeftText = String.format("%02d:%02d", min, sec)
        val Inmin: Int = min.toInt()
        val Insec: Int = sec.toInt()

        if (Inmin == 0 && Insec == 0) {
            val sound: MediaPlayer = MediaPlayer.create(this,R.raw.sound)
            sound.start()
            val real = findViewById<TextView>(R.id.tv_realcount)
            val count = real.text.toString().toInt()
            real.text = (count + 1).toString()
            timerText.text = timeLeftText
        } else {
            timerText.text = timeLeftText
        }
    }


    private fun resetTimer() {
        val reset = findViewById<TextView>(R.id.tv_timer)
        stopTimer()
        reset.text= "00:00"
    }

    private fun checkcount(){
        val real = findViewById<TextView>(R.id.tv_realcount)
        val count = real.text.toString().toInt()
        real.text = (count + 1).toString()
    }

    private fun decount() {
        val real = findViewById<TextView>(R.id.tv_realcount)
        val count = real.text.toString().toInt()
        real.text = (count - 1).toString()
    }


    private fun congfinish() {
        stopTimer()
        val purpose = findViewById<TextView>(R.id.tv_realcount)
        val count = intent.getStringExtra("count")?.toIntOrNull() ?: 0
        val purposeCount = purpose.text.toString().toIntOrNull() ?: 0

        if (purposeCount >= count) {
            Toast.makeText(baseContext, "Perfect!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, "Do your best!!", Toast.LENGTH_SHORT).show()
        }

        startActivity(Intent(this, ListActivity::class.java))
    }

}
