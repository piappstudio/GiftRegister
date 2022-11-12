/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PiSession @Inject constructor() {
    var appName:String?=null
    var packageName:String?=null
    var appVersion:String? = null
    var buildNumber:String? = null
}
