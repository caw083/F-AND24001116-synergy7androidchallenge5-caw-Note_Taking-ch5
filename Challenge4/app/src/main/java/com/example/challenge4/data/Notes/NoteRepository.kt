package com.example.challenge4.data.Notes

import androidx.lifecycle.LiveData
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class NoteRepository(application: Application) {

    private var noteDao: NoteDao
    private val database = NoteDatabase.getInstance(application)

    init {
        noteDao = database.noteDao()
    }

    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insert(note)
        }
    }

//    fun update(note: Note) {
//        coroutineScope.launch(Dispatchers.IO) {
//        noteDao.update(note)}
//    }
//
    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }
//
//    fun deleteAllNotes() {
//        noteDao.deleteAllNotes()
//    }
//
    fun getAllNotes(): LiveData<MutableList<Note>> {
        return noteDao.getAllNotes()
    }

    suspend fun countNotes(): Int {
        return CoroutineScope(Dispatchers.IO).async {
            noteDao.countNotes()
        }.await()
}



}