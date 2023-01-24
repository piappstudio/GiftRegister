package com.piappstudio.pianalytic

import com.google.firebase.analytics.FirebaseAnalytics

object PiAnalyticConstant {
    object Key {
        const val URI = "uri"
    }
    const val SCREEN_VIEW = FirebaseAnalytics.Param.SCREEN_NAME
    const val IMPRESSION = "impression"
    const val NAVIGATE = "navigate"
    const val EXPENDED = "expended"
    const val COLLAPSED = "collapsed"
    object Page {
        const val SPLASH = "splash"
        const val EVENT_LIST = "event_list"
        const val EDIT_EVENT = "edit_event"
        const val GUEST_LIST = "guest_list"
        const val EDIT_GUEST = "edit_guest"
    }
}