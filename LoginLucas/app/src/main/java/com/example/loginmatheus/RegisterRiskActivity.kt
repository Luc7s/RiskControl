package com.example.riskcontrol

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class RegisterRiskActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var pontoX: Float? = null
    private var pontoY: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_risk)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: run {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val edtDescricao = findViewById<EditText>(R.id.edtDescricao)
        val spinnerClassificacao = findViewById<Spinner>(R.id.spinnerClassificacao)
        val imgMapa = findViewById<ImageView>(R.id.imgMapa)
        val pontoMarcado = findViewById<View>(R.id.pontoMarcado)
        val txtCoordenadas = findViewById<TextView>(R.id.txtCoordenadas)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        imgMapa.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                pontoX = event.x
                pontoY = event.y

                pontoMarcado.visibility = View.VISIBLE
                pontoMarcado.x = pontoX!! - pontoMarcado.width / 2
                pontoMarcado.y = pontoY!! - pontoMarcado.height / 2

                txtCoordenadas.text = "Ponto marcado: x=${pontoX?.toInt()}, y=${pontoY?.toInt()}"
            }
            true
        }

        btnRegistrar.setOnClickListener {
            val descricao = edtDescricao.text.toString().trim()
            val classificacao = spinnerClassificacao.selectedItem.toString()

            if (descricao.isEmpty()) {
                Toast.makeText(this, "Digite a descrição do risco", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pontoX == null || pontoY == null) {
                Toast.makeText(this, "Marque um ponto no mapa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = user.uid
            val dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val idRisco = FirebaseDatabase.getInstance().getReference("riscos").push().key

            val risco = mapOf(
                "descricao" to descricao,
                "dataHora" to dataHora,
                "usuarioId" to userId,
                "classificacao" to classificacao,
                "mapX" to pontoX!!.toInt(),
                "mapY" to pontoY!!.toInt()
            )

            FirebaseDatabase.getInstance()
                .getReference("riscos")
                .child(userId)
                .child(idRisco ?: UUID.randomUUID().toString())
                .setValue(risco)
                .addOnSuccessListener {
                    Toast.makeText(this, "Risco registrado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainScreenActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao registrar risco: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }
    }
}
