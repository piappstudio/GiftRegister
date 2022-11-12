/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud.worker

import com.piappstudio.pimodel.EventInfo
import com.piappstudio.pimodel.GuestInfo
import com.piappstudio.pimodel.MediaInfo

data class PiTable(
    val lstOfEvent: List<EventInfo>?=null,
    val lstGuestInfo: List<GuestInfo>?=null,
    val lstMediaInfo: List<MediaInfo>?=null
)