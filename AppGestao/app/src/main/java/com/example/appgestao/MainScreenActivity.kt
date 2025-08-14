package com.example.appgestao

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        findViewById<Button>(R.id.buttonPendentes).setOnClickListener {
            startActivity(Intent(this, PendentesActivity::class.java))
        }

        findViewById<Button>(R.id.buttonResolvidos).setOnClickListener {
            startActivity(Intent(this, ResolvidosActivity::class.java))
        }

        findViewById<Button>(R.id.buttonSair).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
