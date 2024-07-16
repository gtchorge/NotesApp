package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private val items = mutableListOf<ListItem>()
    private val EDIT_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val btnAddList: Button = findViewById(R.id.btnAddList)

        adapter = ListAdapter(items, { item -> shareList(item) }, { position -> editList(position) })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddList.setOnClickListener {
            items.add(ListItem("Nova Anotação ${items.size + 1}", "Texto da lista"))
            adapter.notifyItemInserted(items.size - 1)
        }
    }

    private fun shareList(item: String) {
        val qrCodeBitmap = createQRCode(item)
        val file = File(cacheDir, "qrcode.png")
        val outputStream = FileOutputStream(file)
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri: Uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar QR Code"))
    }

    private fun createQRCode(text: String): Bitmap {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun editList(position: Int) {
        val intent = Intent(this, EditListItemActivity::class.java).apply {
            putExtra("position", position)
            putExtra("title", items[position].title)
            putExtra("text", items[position].text)
        }
        startActivityForResult(intent, EDIT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val position = it.getIntExtra("position", -1)
                val title = it.getStringExtra("title")
                val text = it.getStringExtra("text")
                if (position != -1 && title != null && text != null) {
                    items[position].title = title
                    items[position].text = text
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }
}
