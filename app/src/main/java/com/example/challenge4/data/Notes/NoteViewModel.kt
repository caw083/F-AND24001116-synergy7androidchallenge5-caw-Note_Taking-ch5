package com.example.challenge4.data.Notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = NoteRepository(app)

    fun insert(note: Note) {
        repository.insert(note)
    }

//    fun update(note: Note) {
//        repository.update(note)
//    }
//
    fun delete(note: Note) {
        repository.delete(note)
    }
//
//    fun deleteAllNotes() {
//        repository.deleteAllNotes()
//    }
//
    fun getAllNotes(): LiveData<MutableList<Note>> {
        return repository.getAllNotes()
    }

    suspend fun countUserNotes(): Int {
        return repository.countNotes()
    }


}
