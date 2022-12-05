package com.example.recycleview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var phoneNumber: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var birthdayDate: String = ""
}
