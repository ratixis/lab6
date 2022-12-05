package com.example.recycleview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_KEY = "EXTRA"
    }

    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    private lateinit var adapter: RecyclerAdapter
    var listOfContacts = mutableListOf<ContactEntity>()
    var listOfFilterContacts = mutableListOf<ContactEntity>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactDao = contactDatabase
        setContentView(R.layout.activity_main)

        adapter = RecyclerAdapter(listOfContacts) {
            val intent = Intent(this, ContactBD::class.java)
            intent.putExtra(EXTRA_KEY, listOfContacts[it].id)
            startActivity(intent)
            finish()
        }
        val RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        listOfContacts.clear()
        listOfContacts.addAll(contactDao.all)
        val editTextContect = findViewById<EditText>(R.id.EditText)
        val button = findViewById<Button>(R.id.findButton)
        val createButton = findViewById<Button>(R.id.button)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        RecyclerView.adapter = adapter
        createButton.setOnClickListener() {
            val editActivity = Intent(this, EditActivity::class.java)
            startActivity(editActivity)
            finish()
        }
        button.setOnClickListener() {
            if (editTextContect.text.isEmpty()) {
                listOfContacts.clear()
                listOfContacts.addAll(contactDao.all)
                adapter = RecyclerAdapter(listOfContacts) {
                    val intent = Intent(this, ContactBD::class.java)
                    intent.putExtra(EXTRA_KEY, listOfContacts[it].id)
                    startActivity(intent)
                    finish()
                }
                RecyclerView.adapter = adapter
                adapter.run { notifyDataSetChanged() }
            } else {
               listOfFilterContacts=listOfContacts.filter {
                    editTextContect.text.toString().lowercase() in it.firstName.lowercase() ||
                            editTextContect.text.toString().lowercase() in it.lastName.lowercase()
                }.toMutableList()
                adapter = RecyclerAdapter(listOfFilterContacts) {
                    val intent = Intent(this, ContactBD::class.java)
                    intent.putExtra(EXTRA_KEY, listOfContacts[it].id)
                    startActivity(intent)
                    finish()
                }
                RecyclerView.adapter = adapter
                adapter.run { notifyDataSetChanged() }
            }
        }
    }
}