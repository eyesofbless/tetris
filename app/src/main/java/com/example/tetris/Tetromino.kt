package com.example.tetris

class Tetromino(val shape: Array<IntArray>) {

    // Координаты фигуры
    var x: Int = 0
    var y: Int = 0

    // Метод для вращения фигуры
    fun rotate() {
        val size = shape.size
        val rotatedShape = Array(size) { IntArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                rotatedShape[j][size - 1 - i] = shape[i][j]
            }
        }
        shape.indices.forEach { i -> shape[i] = rotatedShape[i] }
    }
}
