package com.example.tetris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.RelativeLayout
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {

    private lateinit var tetrisView: TetrisView
    private val gameSpeed: Long = 500 // Скорость игры (миллисекунды)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем игровой вид программно
        tetrisView = TetrisView(this)
        setContentView(tetrisView)

        // Запускаем игровой цикл
        startGameLoop()
    }

    private fun startGameLoop() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                tetrisView.update()
                tetrisView.invalidate() // Обновляем экран
                handler.postDelayed(this, gameSpeed)
            }
        })
    }
}
