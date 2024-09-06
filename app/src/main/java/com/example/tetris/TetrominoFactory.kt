package com.example.tetris

object TetrominoFactory {

    fun createTetromino(type: String): Tetromino {
        return when (type) {
            "I" -> Tetromino(
                arrayOf(
                    intArrayOf(1, 1, 1, 1)
                )
            )
            // Добавьте другие формы тетромино
            else -> Tetromino(
                arrayOf(
                    intArrayOf(1, 1),
                    intArrayOf(1, 1)
                )
            )
        }
    }
}
