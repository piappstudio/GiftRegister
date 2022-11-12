/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

/***
 * Model data class
 * CURD = Create, Update, Read, Delete
 */
@Parcelize
@Entity
data class EventInfo(
    @PrimaryKey (autoGenerate = true)
    var id:Long = 0,
    val title: String? = null,
    val date: String? = null,
    val noOfPeople: Int? = null,
    val cashAmount: Double? = null,
    val totalGold:Float? = null,
    val totalOthers:Int? = null
) : Parcelable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
