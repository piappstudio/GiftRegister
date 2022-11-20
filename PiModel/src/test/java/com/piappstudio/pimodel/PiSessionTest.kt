/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import org.junit.Assert.assertEquals
import org.junit.Test

internal class PiSessionTest {


    @Test
    fun isRequiredUpdate() {
        val piSession = PiSession()
        piSession.appVersion = "1.0"
        piSession.appConfig = AppConfig(forceUpdate = ForceUpdate(1.0f, true))
        var isUpdateRequired = piSession.isRequiredUpdate()
        assertEquals(false, isUpdateRequired)

        piSession.appConfig = AppConfig(forceUpdate = ForceUpdate(2.0f, true))
        isUpdateRequired = piSession.isRequiredUpdate()
        assertEquals(true, isUpdateRequired)

        piSession.appConfig = AppConfig(forceUpdate = ForceUpdate(null, true))
        isUpdateRequired = piSession.isRequiredUpdate()
        assertEquals(false, isUpdateRequired)
    }
}