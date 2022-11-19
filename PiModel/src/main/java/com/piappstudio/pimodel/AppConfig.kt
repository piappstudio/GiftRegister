package com.piappstudio.pimodel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AppConfig(

	@field:SerializedName("ads")
	val ads: Ads? = null,

	@field:SerializedName("force_update")
	val forceUpdate: Boolean? = null
) : Parcelable

@Parcelize
data class Ads(

	@field:SerializedName("service")
	val service: Boolean? = null,

	@field:SerializedName("appointment")
	val appointment: Boolean? = null,

	@field:SerializedName("home")
	val home: Boolean? = null
) : Parcelable
