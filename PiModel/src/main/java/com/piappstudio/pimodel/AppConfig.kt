/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class AppConfig(
	val ads: Ads? = null,
	val forceUpdate: ForceUpdate? = null
) : Parcelable

@Parcelize
data class ForceUpdate(
	val minAppVersion: Float? = null,
	val enabled: Boolean? = null
) : Parcelable

@Parcelize
data class Ads(
	val service: Boolean? = null,
	val appointment: Boolean? = null,
	val home: Boolean? = null
) : Parcelable
