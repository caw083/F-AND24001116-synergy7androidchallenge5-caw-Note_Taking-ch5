package com.example.challenge4.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge4.R
import com.example.challenge4.data.Notes.Note
import com.example.challenge4.fragments.list.ListModel
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class NotesAdapter(private val context: Context, private val notesData: MutableList<Note>, private val viewModel: ListViewModel) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private var model : ListModel = ListModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_card, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesData[position]

        // Bind data to your TextViews
        holder.numberTextView.text = model.getDataString()
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.description

        val imageView = holder.itemView.findViewById<ImageView>(R.id.trashImageViwq)


        imageView.setOnClickListener {
            // Handle the click event here
            // For example, you can show a toast message
            //delete note
            deleteConfirmation(note)
            Toast.makeText(holder.itemView.context, "ImageView clicked", Toast.LENGTH_SHORT).show()
        }



        model.incrementData()
    }

    override fun getItemCount(): Int {
        return notesData.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberTextView: TextView = itemView.findViewById(R.id.number_text)
        val titleTextView: TextView = itemView.findViewById(R.id.text_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textView)
    }

    fun deleteConfirmation(note: Note) {
        // Implement the delete confirmation logic here
        // using alert
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog, which ->
                // Call the deleteNote function from your ViewModel or Repository to delete the note
                viewModel.deleteNote(note)
            }
            .setNegativeButton("No") { dialog, which ->
                // Dismiss the dialog if "No" is clicked (optional)
                dialog.dismiss()
            }
            .show()
    }



}
