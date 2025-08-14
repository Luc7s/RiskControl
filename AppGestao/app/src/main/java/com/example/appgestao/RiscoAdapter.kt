package com.example.appgestao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiscoAdapter(
    private val lista: List<Risco>,
    private val onItemClick: (Risco, String) -> Unit
) : RecyclerView.Adapter<RiscoAdapter.RiscoViewHolder>() {

    private var riscoKeys: List<String> = listOf()

    fun setKeys(keys: List<String>) {
        riscoKeys = keys
    }

    class RiscoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descricao: TextView = itemView.findViewById(R.id.textDescricao)
        val dataHora: TextView = itemView.findViewById(R.id.textDataHora)
        val usuario: TextView = itemView.findViewById(R.id.textUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiscoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_risco, parent, false)
        return RiscoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiscoViewHolder, position: Int) {
        val risco = lista[position]
        holder.descricao.text = "Descrição: ${risco.descricao}"
        holder.dataHora.text = "Data: ${risco.dataHora}"
        holder.usuario.text = "Usuário ID: ${risco.usuarioId}"

        holder.itemView.setOnClickListener {
            onItemClick(risco, riscoKeys[position])
        }
    }

    override fun getItemCount() = lista.size
}
