package com.example.appgestao

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class PendentesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val listaRiscos = mutableListOf<Risco>()
    private val riscoKeys = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendentes)

        recyclerView = findViewById(R.id.recyclerPendentes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = FirebaseDatabase.getInstance("https://projeto-integrador-95cb8-default-rtdb.firebaseio.com/")
        val riscosRef = database.getReference("riscos")

        riscosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaRiscos.clear()
                riscoKeys.clear()

                for (usuarioSnapshot in snapshot.children) {
                    val userId = usuarioSnapshot.key ?: continue
                    for (riscoSnapshot in usuarioSnapshot.children) {
                        val risco = riscoSnapshot.getValue(Risco::class.java)
                        val key = riscoSnapshot.key ?: continue
                        risco?.let {
                            listaRiscos.add(it)
                            riscoKeys.add(key)
                        }
                    }
                }

                val adapter = RiscoAdapter(listaRiscos) { risco, riscoKey ->
                    val intent = Intent(this@PendentesActivity, DetalhesRiscoActivity::class.java)
                    intent.putExtra("descricao", risco.descricao)
                    intent.putExtra("dataHora", risco.dataHora)
                    intent.putExtra("classificacao", risco.classificacao)
                    intent.putExtra("mapX", risco.mapX)
                    intent.putExtra("mapY", risco.mapY)
                    intent.putExtra("usuarioId", risco.usuarioId)
                    intent.putExtra("riscoKey", riscoKey)
                    startActivity(intent)
                }
                adapter.setKeys(riscoKeys)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
