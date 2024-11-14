package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            isConnected = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }
    val handler: Handler = Handler(Looper.getMainLooper()){
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var countdownTextView = findViewById<TextView>(R.id.textView)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            if (isConnected && startButton.text == "Start"){
                timerBinder.start(100)
                startButton.text = "Pause"
            }
            if (timerBinder.isRunning && startButton.text == "Pause"){
                timerBinder.pause()
                startButton.text = "Start"

            }

        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected && timerBinder.isRunning){
                timerBinder.stop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)

    }

    fun updateTextView(textView: TextView){

    }

}