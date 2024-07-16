package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditListItemActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_list_item)

        editTitle = findViewById(R.id.editTitle)
        editText = findViewById(R.id.editText)
        val btnSave: Button = findViewById(R.id.btnSave)

        val position = intent.getIntExtra("position", -1)
        val title = intent.getStringExtra("title")
        val text = intent.getStringExtra("text")

        if (title != null) {
            editTitle.setText(title)
        }

        if (text != null) {
            editText.setText(text)
        }

        btnSave.setOnClickListener {
            val data = Intent().apply {
                putExtra("position", position)
                putExtra("title", editTitle.text.toString())
                putExtra("text", editText.text.toString())
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
