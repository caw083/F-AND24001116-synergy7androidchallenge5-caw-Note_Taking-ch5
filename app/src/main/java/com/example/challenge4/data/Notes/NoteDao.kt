package com.example.challenge4.data.Notes

import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("delete from note_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes(): LiveData<MutableList<Note>> // Change the return type to LiveData<MutableList<Note>>

    // count database
    @Query("select count(*) from note_table")
    suspend fun countNotes(): Int

}