package com.asg.notesapp.ui.notes

import androidx.recyclerview.widget.DiffUtil
import com.asg.notesapp.data.model.Note
import androidx.recyclerview.widget.ListAdapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asg.notesapp.databinding.ItemNoteBinding

class NotesAdapter(
    private val onNoteClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    // Color array for cards
    private val colors = arrayOf("#FF9E9E", "#91F48F", "#FFF599", "#9EFFFF", "#B69CFF")

    // ViewHolder class
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, position: Int) {
            binding.textViewNoteTitle.text = note.title

            // Set card color
            val color = Color.parseColor(colors[position % colors.size])
            binding.cardViewNote.setCardBackgroundColor(color)

            // Set click listener
            binding.root.setOnClickListener {
                onNoteClick(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}