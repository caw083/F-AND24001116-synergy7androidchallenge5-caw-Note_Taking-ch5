package com.example.challenge4.fragments.add

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.challenge4.data.Notes.NoteViewModel
import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.example.challenge4.data.Notes.Note

class FormViewModel(application: Application) : AndroidViewModel(application) {

    private val mNoteViewModel: NoteViewModel = NoteViewModel(application)

    fun getCountNotes(): LiveData<Int> {
        val countLiveData = MutableLiveData<Int>()
        viewModelScope.launch {
            val count = mNoteViewModel.countUserNotes()
            countLiveData.postValue(count)
        }
        return countLiveData
    }

    private fun inputCheck(firstName: String, lastName: String, age: String): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }


    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }


    fun insertDataToDatabase(titleNotesText: String, descriptionNotesText: String, priorityNotesText: String): Boolean{
        if (inputCheck(titleNotesText, descriptionNotesText, priorityNotesText)){
            val note = Note( titleNotesText, descriptionNotesText, Integer.parseInt(priorityNotesText))
            mNoteViewModel.insert(note)
            showToast(getApplication(), "Successfully added!")
            return true
        }else{
            showToast(getApplication(), "Please fill out all fields.")
            return false
        }
    }
}