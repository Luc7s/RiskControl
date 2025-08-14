package com.example.appgestao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetalhesRiscoActivity : AppCompatActivity() {

    private lateinit var descricao: String
    private lateinit var dataHora: String
    private lateinit var classificacao: String
    private var mapX: Int = -1
    private var mapY: Int = -1
    private lateinit var usuarioId: String
    private lateinit var riscoKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_risco)

        descricao = intent.getStringExtra("descricao") ?: ""
        dataHora = intent.getStringExtra("dataHora") ?: ""
        classificacao = intent.getStringExtra("classificacao") ?: ""
        mapX = intent.getIntExtra("mapX", -1)
        mapY = intent.getIntExtra("mapY", -1)
        usuarioId = intent.getStringExtra("usuarioId") ?: ""
        riscoKey = intent.getStringExtra("riscoKey") ?: ""

        findViewById<TextView>(R.id.textDescricao).text = "Descrição: $descricao"
        findViewById<TextView>(R.id.textDataHora).text = "Data: $dataHora"
        findViewById<TextView>(R.id.textClassificacao).text = "Classificação: $classificacao"

        val imgMapa = findViewById<ImageView>(R.id.imageMapa)
        val pontoMarcado = findViewById<View>(R.id.pontoMarcado)

        imgMapa.post {
            if (mapX >= 0 && mapY >= 0 && imgMapa.width > 0 && imgMapa.height > 0) {
                val imgWidth = imgMapa.width.toFloat()
                val imgHeight = imgMapa.height.toFloat()

                val originalWidth = 1000f
                val originalHeight = 1000f

                val posX = (mapX / originalWidth) * imgWidth - pontoMarcado.width / 2
                val posY = (mapY / originalHeight) * imgHeight - pontoMarcado.height / 2

                pontoMarcado.x = posX
                pontoMarcado.y = posY
                pontoMarcado.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Localização inválida para o risco", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSolucionado).setOnClickListener {
            val db = FirebaseDatabase.getInstance("https://projeto-integrador-95cb8-default-rtdb.firebaseio.com/")
            val refOriginal = db.getReference("riscos").child(usuarioId).child(riscoKey)
            val refResolvidos = db.getReference("resolvidos").child(usuarioId).child(riscoKey)

            val risco = mapOf(
                "descricao" to descricao,
                "dataHora" to dataHora,
                "classificacao" to classificacao,
                "mapX" to mapX,
                "mapY" to mapY,
                "usuarioId" to usuarioId
            )

            refResolvidos.setValue(risco).addOnSuccessListener {
                refOriginal.removeValue().addOnSuccessListener {
                    startActivity(Intent(this, ResolvidosActivity::class.java))
                    finish()
                }
            }
        }
    }
}
