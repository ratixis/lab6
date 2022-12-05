package com.example.recycleview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactBD : AppCompatActivity() {

    companion object {
        const val EXTRA_KEY = "EXTRA"
        const val REQUEST_CODE = 42
    }

    private val contactDatabase by lazy {
        ContactDatabase.getDatabase(this).contactDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        fun call(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }

        fun checkPermission(phoneNumber: String) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CALL_PHONE
                    )
                ) {
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CODE
                    )
                }
            } else {
                call(phoneNumber)
            }
        }
        val id = intent.getLongExtra(MainActivity.EXTRA_KEY, -1)
        val phonenumber = findViewById<TextView>(R.id.textPhoneNumber)
        val name = findViewById<TextView>(R.id.textFirstName)
        val lastName = findViewById<TextView>(R.id.textLastName)
        val birthdayDate = findViewById<TextView>(R.id.textBirthdayDate)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        val contactDao = contactDatabase
        val contactEntity = contactDatabase.getById(id)
        phonenumber.text = phonenumber.text.toString() + " " + contactEntity.phoneNumber
        name.text = name.text.toString() + " " + contactEntity.firstName
        lastName.text = lastName.text.toString() + " " + contactEntity.lastName
        birthdayDate.text = birthdayDate.text.toString() + " " + contactEntity.birthdayDate
        buttonBack.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        phonenumber.setOnClickListener {
            checkPermission(contactEntity.phoneNumber)
        }
        buttonEdit.setOnClickListener {
            val startContactActivity = Intent(this, EditActivity::class.java)
            startContactActivity.putExtra(EXTRA_KEY, id)
            startActivity(startContactActivity)
            finish()
        }
        buttonDelete.setOnClickListener {
            val conEntity = contactDatabase.getById(id)
            contactDao.delete(contactEntity)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
    }
}