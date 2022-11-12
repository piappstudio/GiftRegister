/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.piappstudio.pimodel.GuestInfo
import com.piappstudio.pimodel.MediaInfo

@Dao
interface IMediaDao {
    @Insert
    suspend fun insert(mediaInfo: MediaInfo):Long

    @Query("SELECT * FROM mediainfo where eventId = :eventId")
    suspend fun fetchEventMedias(eventId:Long):List<MediaInfo>

    @Query("SELECT * FROM mediainfo where guestId = :guestId")
    suspend fun fetchGuestMedias(guestId:Long):List<MediaInfo>

    @Query("SELECT * FROM mediainfo")
    fun fetchAllMedia():List<MediaInfo>

    @Query ("DELETE FROM mediainfo where guestId = :guestId")
    suspend fun delete(guestId: Long)

    @Delete
    suspend fun delete(mediaInfo: MediaInfo)
    @Insert
    fun insertMedias(lstMedia: List<MediaInfo>): List<Long>
}