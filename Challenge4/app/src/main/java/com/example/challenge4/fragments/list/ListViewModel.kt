package com.example.challenge4.fragments.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.challenge4.data.Notes.Note
import com.example.challenge4.data.Notes.NoteViewModel

class ListViewModel(application :Application) : AndroidViewModel (application) {
    private var localRoomServer : NoteViewModel = NoteViewModel(application)

    fun getAllNotes() : LiveData<MutableList<Note>>{
        return localRoomServer.getAllNotes()
    }

    fun deleteNote(note : Note){
        localRoomServer.delete(note)
    }

}
