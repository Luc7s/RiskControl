package com.example.riskcontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        auth = FirebaseAuth.getInstance()

        val btnRegistrarRisco = findViewById<Button>(R.id.btnRegistrarRisco)
        val btnSair = findViewById<Button>(R.id.btnSair)
        val btnMapa = findViewById<Button>(R.id.btnMapa)

        btnRegistrarRisco.setOnClickListener {
            val intent = Intent(this, RegisterRiskActivity::class.java)
            startActivity(intent)
        }

        btnMapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
