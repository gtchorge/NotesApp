package com.example.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
    private val list: List<ListItem>,
    private val onShare: (String) -> Unit,
    private val onEdit: (Int) -> Unit
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val shareButton: Button = itemView.findViewById(R.id.btnShare)
        val editButton: Button = itemView.findViewById(R.id.btnEdit)

        fun bind(item: ListItem) {
            textView.text = item.title
            shareButton.setOnClickListener { onShare(item.text) }
            editButton.setOnClickListener { onEdit(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}
