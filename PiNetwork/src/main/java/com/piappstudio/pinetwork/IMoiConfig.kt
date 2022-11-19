package com.piappstudio.pinetwork

import com.piappstudio.pimodel.AppConfig
import retrofit2.Response
import retrofit2.http.GET

interface IMoiConfig {
    @GET("piappstudio/resources/main/moi/json/appconfig.json")
    suspend fun fetchAppConfig(): Response<AppConfig?>
}
