package com.example.recycleview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class EditActivity : AppCompatActivity() {


    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        val id = intent.getLongExtra(ContactBD.EXTRA_KEY, -1)
        val phonenumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val Name = findViewById<EditText>(R.id.editTextName)
        val lastName = findViewById<EditText>(R.id.editTextLastName)
        val birthdayDate = findViewById<EditText>(R.id.editTextBirthday)
        val button = findViewById<Button>(R.id.buttonDone)
        if (id >= 0) {
            val conEntity = contactDatabase.getById(id)
            Name.setText(conEntity.firstName)
            lastName.setText(conEntity.lastName)
            birthdayDate.setText(conEntity.birthdayDate)
            phonenumber.setText(conEntity.phoneNumber)
        }
        var contactEntity: ContactEntity = ContactEntity()
        button.setOnClickListener {
            if ((!Name.text.isEmpty()) && (!phonenumber.text.isEmpty())
            ) {
                if (id >= 0) {
                    contactEntity.id = id
                    contactEntity.phoneNumber = phonenumber.text.toString()
                    contactEntity.firstName = Name.text.toString()
                    contactEntity.lastName = lastName.text.toString()
                    contactEntity.birthdayDate = birthdayDate.text.toString()
                    contactDatabase.update(contactEntity)
                    val startContactBD = Intent(this, ContactBD::class.java)
                    startContactBD.putExtra(ContactBD.EXTRA_KEY, id)
                    startActivity(startContactBD)
                    finish()
                } else {
                    contactEntity.phoneNumber = phonenumber.text.toString()
                    contactEntity.firstName = Name.text.toString()
                    contactEntity.lastName = lastName.text.toString()
                    contactEntity.birthdayDate = birthdayDate.text.toString()
                    contactDatabase.insert(contactEntity)
                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    finish()
                }
            } else {
                val toast = Toast.makeText(
                    applicationContext, // контекст
                    "Нужно ввести телефон и имя",     // текст тоста
                    Toast.LENGTH_SHORT  // длительность показа
                )
                toast.show()
            }
        }
    }
}
