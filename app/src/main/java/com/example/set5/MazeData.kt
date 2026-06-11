package com.example.set5

/**

door flags:
1  (bit 0) = LEFT
2  (bit 1) = RIGHT
4  (bit 2) = UP
8  (bit 3) = DOWN

special marker:
16 (bit 4) = START room marker — NOT a door direction

End room = 0
 */

object MazeData {

    const val DOOR_LEFT = 1
    const val DOOR_RIGHT = 2
    const val DOOR_UP = 4
    const val DOOR_DOWN = 8
    const val START_MASK = 16
    const val DOOR_MASK = 0b00001111  // bits 0-3

    /**
    4×4 maze.
    Start is marked by adding START_MASK (16) to cell [0][0].
    10 + 16 = 26  →  start room at row=0, col=0 with doors RIGHT+DOWN (bits 1,3 = 2+8=10).

    End room is cell [1][2] with value 0.

    Original values:
    { 10,  8, 10,  9 }
    { 28,  1,  0, 12 }
    { 12, 10,  9, 13 }
    {  6,  5,  6,  5 }

     */
    val maze: Array<IntArray> = arrayOf(
        intArrayOf(26, 8, 10, 9),   // row 0 — [0][0] is START (26 = 10 + 16)
        intArrayOf(28, 1, 0, 12),   // row 1 — [1][2] == 0  → EXIT
        intArrayOf(12, 10, 9, 13),   // row 2
        intArrayOf(6, 5, 6, 5)    // row 3
    )

    val rows: Int get() = maze.size
    val cols: Int get() = maze[0].size
    fun rawValue(row: Int, col: Int): Int = maze[row][col]

    fun doorBits(row: Int, col: Int): Int = maze[row][col] and DOOR_MASK

    fun isStart(row: Int, col: Int): Boolean =
        (maze[row][col] and START_MASK) != 0

    fun isExit(row: Int, col: Int): Boolean =
        doorBits(row, col) == 0 && !isStart(row, col)

    fun findStart(): Pair<Int, Int> {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (isStart(r, c)) return Pair(r, c)
            }
        }
        return Pair(0, 0)
    }


    fun canMove(row: Int, col: Int, direction: Int): Boolean {
        val bits = doorBits(row, col)
        if ((bits and direction) == 0) return false

        return when (direction) {
            DOOR_LEFT -> col > 0
            DOOR_RIGHT -> col < cols - 1
            DOOR_UP -> row > 0
            DOOR_DOWN -> row < rows - 1
            else -> false
        }
    }

    fun move(row: Int, col: Int, direction: Int): Pair<Int, Int>? {
        if (!canMove(row, col, direction)) return null
        return when (direction) {
            DOOR_LEFT -> Pair(row, col - 1)
            DOOR_RIGHT -> Pair(row, col + 1)
            DOOR_UP -> Pair(row - 1, col)
            DOOR_DOWN -> Pair(row + 1, col)
            else -> null
        }
    }
}

