package com.example.submissionintermediate1.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "story")
class StoryResponseItem (
    @PrimaryKey
    val id: String,

    val name: String? = null,

    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String? = null,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,

    val lon: Double? = null,

    val lat: Double? = null
): Parcelable