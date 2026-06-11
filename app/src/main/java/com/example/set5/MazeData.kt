package com.example.set5

/**
 * Maze definition and bitmask constants.
 *
 * Bitmask door flags:
 *   1  (bit 0) = LEFT
 *   2  (bit 1) = RIGHT
 *   4  (bit 2) = UP
 *   8  (bit 3) = DOWN
 *
 * Special marker:
 *   16 (bit 4) = START room marker — NOT a door direction
 *
 * End room: value == 0 (after masking out the start bit)
 */
object MazeData {

    const val DOOR_LEFT = 1   // 2^0
    const val DOOR_RIGHT = 2   // 2^1
    const val DOOR_UP = 4   // 2^2
    const val DOOR_DOWN = 8   // 2^3
    const val START_MASK = 16  // 2^4 — start room marker
    const val DOOR_MASK = 0b00001111  // bits 0-3 only

    /**
     * The mandatory 4×4 maze.
     * Start is marked by adding START_MASK (16) to cell [0][0].
     * 10 + 16 = 26  →  start room at row=0, col=0 with doors RIGHT+DOWN (bits 1,3 = 2+8=10).
     *
     * End room is cell [1][2] with value 0.
     *
     * Original values:
     *   { 10,  8, 10,  9 }
     *   { 28,  1,  0, 12 }
     *   { 12, 10,  9, 13 }
     *   {  6,  5,  6,  5 }
     *
     * [0][0] = 10 + 16 = 26  (start marker added here)
     */
    val maze: Array<IntArray> = arrayOf(
        intArrayOf(26, 8, 10, 9),   // row 0 — [0][0] is START (26 = 10 + 16)
        intArrayOf(28, 1, 0, 12),   // row 1 — [1][2] == 0  → EXIT
        intArrayOf(12, 10, 9, 13),   // row 2
        intArrayOf(6, 5, 6, 5)    // row 3
    )

    val rows: Int get() = maze.size
    val cols: Int get() = maze[0].size

    /** Raw cell value (includes start marker bit). */
    fun rawValue(row: Int, col: Int): Int = maze[row][col]

    /** Door bits only — strips the start marker. */
    fun doorBits(row: Int, col: Int): Int = maze[row][col] and DOOR_MASK

    /** True if this cell is the start room. */
    fun isStart(row: Int, col: Int): Boolean =
        (maze[row][col] and START_MASK) != 0

    /** True if this cell is the exit room (value 0 after masking start bit). */
    fun isExit(row: Int, col: Int): Boolean =
        doorBits(row, col) == 0 && !isStart(row, col)

    /** Find the start room coordinates. Returns Pair(row, col). */
    fun findStart(): Pair<Int, Int> {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (isStart(r, c)) return Pair(r, c)
            }
        }
        // Fallback: top-left if no start marker found
        return Pair(0, 0)
    }

    /**
     * Check whether a move from (row, col) in the given direction is allowed.
     * Checks bitmask AND boundary conditions.
     *
     * @param direction One of DOOR_LEFT, DOOR_RIGHT, DOOR_UP, DOOR_DOWN
     * @return true if the move is within bounds AND the current room has that door
     */
    fun canMove(row: Int, col: Int, direction: Int): Boolean {
        val bits = doorBits(row, col)
        if ((bits and direction) == 0) return false  // no door in that direction

        return when (direction) {
            DOOR_LEFT -> col > 0
            DOOR_RIGHT -> col < cols - 1
            DOOR_UP -> row > 0
            DOOR_DOWN -> row < rows - 1
            else -> false
        }
    }

    /** Returns the (newRow, newCol) after moving, or null if move is invalid. */
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

