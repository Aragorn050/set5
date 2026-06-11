package com.example.set5

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {


    private var playerRow = 0
    private var playerCol = 0
    private var stepCount  = 0


    private lateinit var btnUp    : Button
    private lateinit var btnDown  : Button
    private lateinit var btnLeft  : Button
    private lateinit var btnRight : Button
    private lateinit var tvCoords : TextView
    private lateinit var tvDebug  : TextView
    private lateinit var tvRoom   : TextView
    private lateinit var tvDoors  : TextView


    private val colorEnabled  = Color.parseColor("#3FB950")
    private val colorDisabled = Color.parseColor("#21262D")
    private val textEnabled   = Color.parseColor("#0D1117")
    private val textDisabled  = Color.parseColor("#484F58")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnUp    = findViewById(R.id.btnUp)
        btnDown  = findViewById(R.id.btnDown)
        btnLeft  = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        tvCoords = findViewById(R.id.tvRoomCoords)
        tvDebug  = findViewById(R.id.tvMaskDebug)
        tvRoom   = findViewById(R.id.tvRoomNumber)
        tvDoors  = findViewById(R.id.tvDoorStatus)


        if (savedInstanceState != null) {
            playerRow = savedInstanceState.getInt("row", 0)
            playerCol = savedInstanceState.getInt("col", 0)
            stepCount = savedInstanceState.getInt("steps", 0)
        } else {
            val start = MazeData.findStart()
            playerRow = start.first
            playerCol = start.second
            stepCount = 0
        }

        btnUp.setOnClickListener    { tryMove(MazeData.DOOR_UP)    }
        btnDown.setOnClickListener  { tryMove(MazeData.DOOR_DOWN)  }
        btnLeft.setOnClickListener  { tryMove(MazeData.DOOR_LEFT)  }
        btnRight.setOnClickListener { tryMove(MazeData.DOOR_RIGHT) }

        updateUI()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("row",   playerRow)
        outState.putInt("col",   playerCol)
        outState.putInt("steps", stepCount)
    }


    private fun tryMove(direction: Int) {
        val dest = MazeData.move(playerRow, playerCol, direction) ?: return

        playerRow = dest.first
        playerCol = dest.second
        stepCount++

        if (MazeData.isExit(playerRow, playerCol)) {
            launchResult()
            return
        }

        updateUI()
    }


    private fun updateUI() {
        tvCoords.text = "Room [${playerRow}, ${playerCol}]"
        val raw   = MazeData.rawValue(playerRow, playerCol)
        val doors = MazeData.doorBits(playerRow, playerCol)
        tvDebug.text = "mask: $raw"

        tvRoom.text = "${playerRow}, ${playerCol}"

        val doorList = buildList {
            if (MazeData.canMove(playerRow, playerCol, MazeData.DOOR_UP))    add("U")
            if (MazeData.canMove(playerRow, playerCol, MazeData.DOOR_DOWN))  add("D")
            if (MazeData.canMove(playerRow, playerCol, MazeData.DOOR_LEFT))  add("L")
            if (MazeData.canMove(playerRow, playerCol, MazeData.DOOR_RIGHT)) add("R")
        }
        tvDoors.text = if (doorList.isEmpty()) "Doors: none" else "Doors: ${doorList.joinToString("  ")}"

        styleButton(btnUp,    MazeData.canMove(playerRow, playerCol, MazeData.DOOR_UP))
        styleButton(btnDown,  MazeData.canMove(playerRow, playerCol, MazeData.DOOR_DOWN))
        styleButton(btnLeft,  MazeData.canMove(playerRow, playerCol, MazeData.DOOR_LEFT))
        styleButton(btnRight, MazeData.canMove(playerRow, playerCol, MazeData.DOOR_RIGHT))
    }


    private fun styleButton(btn: Button, available: Boolean) {
        btn.isEnabled = available
        btn.backgroundTintList = android.content.res.ColorStateList.valueOf(
            if (available) colorEnabled else colorDisabled
        )
        btn.setTextColor(if (available) textEnabled else textDisabled)
    }


    private fun launchResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("steps", stepCount)
        startActivity(intent)
        finish()
    }
}