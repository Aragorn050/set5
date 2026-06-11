package com.example.set5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val steps = intent.getIntExtra("steps", 0)

        val tvSteps    = findViewById<TextView>(R.id.tvStepCount)
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        val btnMenu    = findViewById<Button>(R.id.btnMenu)

        tvSteps.text = "Steps taken: $steps"

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToMenu()
            }
        })

        btnRestart.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        btnMenu.setOnClickListener { goToMenu() }
    }

    private fun goToMenu() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
