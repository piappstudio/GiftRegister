/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel.database.dao

import androidx.room.*
import com.piappstudio.pimodel.EventInfo
import com.piappstudio.pimodel.EventSummary

@Dao
interface IEventDao {
    @Insert
    suspend fun insert(eventInfo: EventInfo):Long

    @Update
    suspend fun update(eventInfo: EventInfo)

    @Delete
    suspend fun delete(eventInfo: EventInfo)


    @Query ("SELECT * FROM eventinfo")
    suspend fun fetchEvents():List<EventSummary>

    @Query ("SELECT * FROM eventinfo")
    fun fetchAllEvents():List<EventInfo>
    @Insert
    fun insertEvents(events: List<EventInfo>): List<Long>
}