package com.example.appgestao

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ResolvidosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val listaRiscos = mutableListOf<Risco>()
    private val riscoKeys = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolvidos)

        recyclerView = findViewById(R.id.recyclerResolvidos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = FirebaseDatabase.getInstance("https://projeto-integrador-95cb8-default-rtdb.firebaseio.com/")
        val resolvidosRef = database.getReference("resolvidos")

        resolvidosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaRiscos.clear()
                riscoKeys.clear()

                if (!snapshot.exists()) {
                    Toast.makeText(applicationContext, "Nenhum risco resolvido encontrado.", Toast.LENGTH_SHORT).show()
                    return
                }

                for (usuarioSnapshot in snapshot.children) {
                    val userId = usuarioSnapshot.key ?: continue
                    for (riscoSnapshot in usuarioSnapshot.children) {
                        val risco = riscoSnapshot.getValue(Risco::class.java)
                        val key = riscoSnapshot.key ?: continue
                        risco?.let {
                            listaRiscos.add(it)
                            riscoKeys.add(key)
                            Log.d("RISCO", "Adicionado: ${it.descricao} de $userId")
                        }
                    }
                }

                if (listaRiscos.isEmpty()) {
                    Toast.makeText(applicationContext, "Lista vazia após leitura.", Toast.LENGTH_SHORT).show()
                }

                val adapter = RiscoAdapter(listaRiscos) { _, _ -> }
                adapter.setKeys(riscoKeys)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
