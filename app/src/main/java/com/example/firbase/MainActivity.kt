package com.example.firbase

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var rvNotes: RecyclerView
    private lateinit var rvAdapter: NoteAdapter
    private lateinit var editText: EditText
    private lateinit var submitBtn: Button

    lateinit var mainViewModel: MainViewModel
    lateinit var notes: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Note App"
        notes = arrayListOf()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.getNotes().observe(this, { notes ->
            rvAdapter.update(notes)
        })

        editText = findViewById(R.id.noteET)
        submitBtn = findViewById(R.id.submit)
        submitBtn.setOnClickListener {
            mainViewModel.addNote(Note("", editText.text.toString()))
            editText.text.clear()
            editText.clearFocus()
        }

        rvNotes = findViewById(R.id.noteRecyclerView)
        rvAdapter = NoteAdapter(this)
        rvNotes.adapter = rvAdapter
        rvNotes.layoutManager = LinearLayoutManager(this)

        mainViewModel.getData()
    }

    fun raiseDialog(id: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->
                mainViewModel.editNote(id, updatedNote.text.toString())
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }

    fun deleteDialog(id: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val textNote = TextView(this)
        textNote.text = "Delete this Note ?"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("DELETE", DialogInterface.OnClickListener { _, _ ->
                mainViewModel.deleteNote(id,)
            })
            .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete")
        alert.setView(textNote)
        alert.show()
    }
}