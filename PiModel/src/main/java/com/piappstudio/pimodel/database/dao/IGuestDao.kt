/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel.database.dao

import androidx.room.*
import com.piappstudio.pimodel.GuestInfo
import com.piappstudio.pimodel.MediaInfo

@Dao
interface IGuestDao {
    @Insert
    suspend fun insert(guestInfo: GuestInfo):Long

    @Update
    suspend fun update(guestInfo: GuestInfo)

    @Delete
    suspend fun delete(guestInfo: GuestInfo)

    @Query ("DELETE FROM guestinfo where eventId = :eventId")
    suspend fun delete(eventId: Long)

    @Query("SELECT * FROM guestinfo where eventId = :eventId")
    suspend fun fetchGuest(eventId:Long):List<GuestInfo>

    @Query("SELECT * FROM guestinfo")
    fun fetchAllGuest():List<GuestInfo>
    @Insert
    fun insertGuests(guests: List<GuestInfo>): List<Long>
}