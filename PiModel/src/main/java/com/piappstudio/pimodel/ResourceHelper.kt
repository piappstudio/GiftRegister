/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel
import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ResourceHelper @Inject constructor(@ApplicationContext private val applicationContext: Context) {
    fun getString(@StringRes id:Int):String {
       return applicationContext.getString(id)
    }
}