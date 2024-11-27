package com.example.wishlistapp

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlist = mutableListOf<WishlistItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wishlistAdapter = WishlistAdapter(wishlist, this::onItemClicked, this::onItemLongClicked)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = wishlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.fabAddItem).setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun showAddItemDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.inputName)
        val priceInput = dialogView.findViewById<TextInputEditText>(R.id.inputPrice)
        val urlInput = dialogView.findViewById<TextInputEditText>(R.id.inputUrl)

        AlertDialog.Builder(this)
            .setTitle("Add Item")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val price = priceInput.text.toString()
                val url = urlInput.text.toString()

                if (name.isNotBlank() && price.isNotBlank() && url.isNotBlank()) {
                    wishlist.add(WishlistItem(name, price, url))
                    wishlistAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun onItemClicked(item: WishlistItem) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemLongClicked(item: WishlistItem) {
        wishlist.remove(item)
        wishlistAdapter.notifyDataSetChanged()
    }
}
