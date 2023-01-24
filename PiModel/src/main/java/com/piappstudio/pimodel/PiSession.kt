/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min


@Singleton
class PiSession @Inject constructor() {
    var appName: String? = null
    var packageName: String? = null
    var appVersion: String? = null
    var buildNumber: String? = null
    var appConfig: AppConfig? = null
    var uuid:String? = null
    fun isRequiredUpdate(): Boolean {
        try {
            if (appConfig?.forceUpdate?.enabled == true) {
                val currAppVersion = appVersion?.toFloat()
                val minAppVersion = appConfig?.forceUpdate?.minAppVersion
                if (currAppVersion != null && minAppVersion != null) {
                    if (currAppVersion < minAppVersion) {
                        return true
                    }
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        return false
    }
}
