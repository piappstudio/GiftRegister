/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MediaInfo( @PrimaryKey(autoGenerate = true)
                      var id:Long = 0,
                      val path:String,
                      var eventId:Long?=null,
                      var guestId:Long?=null)
