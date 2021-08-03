package com.soda1127.example.bookstore.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class BookMemoEntity(
    @PrimaryKey val isbn13: String,
    val memo: String
): Parcelable
