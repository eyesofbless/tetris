package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random
import android.util.AttributeSet


class TetrisView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint()
    private val gridWidth = 10 // Ширина поля
    private val gridHeight = 20 // Высота поля
    private val blockSize = 60 // Размер блока
    private val grid = Array(gridHeight) { IntArray(gridWidth) }
    private var currentTetromino: Tetromino? = null
    private var tetrominoX = gridWidth / 2 // Начальная позиция X
    private var tetrominoY = 0 // Начальная позиция Y

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        // Генерируем первую фигуру
        spawnTetromino()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
        drawTetromino(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                if (grid[y][x] != 0) {
                    paint.color = Color.BLUE
                } else {
                    paint.color = Color.LTGRAY
                }
                canvas.drawRect(
                    (x * blockSize).toFloat(),
                    (y * blockSize).toFloat(),
                    ((x + 1) * blockSize).toFloat(),
                    ((y + 1) * blockSize).toFloat(),
                    paint
                )
            }
        }
    }

    private fun drawTetromino(canvas: Canvas) {
        currentTetromino?.let { tetromino ->
            paint.color = Color.RED
            for (y in tetromino.shape.indices) {
                for (x in tetromino.shape[y].indices) {
                    if (tetromino.shape[y][x] != 0) {
                        canvas.drawRect(
                            ((tetrominoX + x) * blockSize).toFloat(),
                            ((tetrominoY + y) * blockSize).toFloat(),
                            ((tetrominoX + x + 1) * blockSize).toFloat(),
                            ((tetrominoY + y + 1) * blockSize).toFloat(),
                            paint
                        )
                    }
                }
            }
        }
    }


    fun moveLeft() {
        currentTetromino?.let {
            it.x -= 1
            invalidate()
        }
    }

    fun moveRight() {
        currentTetromino?.let {
            it.x += 1
            invalidate()
        }
    }

    fun moveDown() {
        currentTetromino?.let {
            it.y += 1
            invalidate()
        }
    }

    fun rotate() {
        currentTetromino?.let {
            it.rotate()
            invalidate()
        }
    }

    fun update() {
        // Перемещаем фигуру вниз
        tetrominoY += 1
        if (!isTetrominoValid()) {
            tetrominoY -= 1
            mergeTetromino()
            spawnTetromino()
        }
        invalidate() // Обновляем экран
    }

    private fun isTetrominoValid(): Boolean {
        currentTetromino?.let { tetromino ->
            for (y in tetromino.shape.indices) {
                for (x in tetromino.shape[y].indices) {
                    if (tetromino.shape[y][x] != 0) {
                        val gridX = tetrominoX + x
                        val gridY = tetrominoY + y
                        if (gridX < 0 || gridX >= gridWidth || gridY >= gridHeight || grid[gridY][gridX] != 0) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun mergeTetromino() {
        currentTetromino?.let { tetromino ->
            for (y in tetromino.shape.indices) {
                for (x in tetromino.shape[y].indices) {
                    if (tetromino.shape[y][x] != 0) {
                        val gridX = tetrominoX + x
                        val gridY = tetrominoY + y
                        if (gridY >= 0) {
                            grid[gridY][gridX] = 1
                        }
                    }
                }
            }
        }
    }

    private fun spawnTetromino() {
        val types = arrayOf("I") // Добавьте другие типы
        val type = types[Random.nextInt(types.size)]
        currentTetromino = TetrominoFactory.createTetromino(type)
        tetrominoX = gridWidth / 2
        tetrominoY = 0
        if (!isTetrominoValid()) {
            // Если новая фигура не помещается, игра закончена
            // Можно добавить обработку окончания игры
        }
    }

    // Обработка нажатий клавиш для управления
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> moveTetromino(-1, 0)
            KeyEvent.KEYCODE_DPAD_RIGHT -> moveTetromino(1, 0)
            KeyEvent.KEYCODE_DPAD_DOWN -> update()
            KeyEvent.KEYCODE_DPAD_UP -> rotateTetromino()
        }
        return super.onKeyDown(keyCode, event)
    }

    // Обработка жестов для управления
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = it.x.toInt() / blockSize
                    val y = it.y.toInt() / blockSize
                    if (x < gridWidth / 2) {
                        moveTetromino(-1, 0)
                    } else {
                        moveTetromino(1, 0)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun moveTetromino(dx: Int, dy: Int) {
        tetrominoX += dx
        tetrominoY += dy
        if (!isTetrominoValid()) {
            tetrominoX -= dx
            tetrominoY -= dy
        }
        invalidate() // Обновляем экран
    }

    private fun rotateTetromino() {
        currentTetromino?.rotate()
        if (!isTetrominoValid()) {
            // Если после поворота фигура невалидна, откатываем изменения
            currentTetromino?.rotate()
            currentTetromino?.rotate()
            currentTetromino?.rotate()
        }
        invalidate() // Обновляем экран
    }
}
