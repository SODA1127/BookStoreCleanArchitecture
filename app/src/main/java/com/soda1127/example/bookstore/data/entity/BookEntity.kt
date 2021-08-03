package com.soda1127.example.bookstore.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class BookEntity(
    val title: String,
    val subtitle: String,
    @PrimaryKey val isbn13: String,
    val price: String,
    val image: String,
    val url: String
): Parcelable
