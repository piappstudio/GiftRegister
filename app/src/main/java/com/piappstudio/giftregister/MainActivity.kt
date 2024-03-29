package com.piappstudio.giftregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.piappstudio.giftregister.navgraph.AppNavGraph
import com.piappstudio.pianalytic.PiTracker
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.error.ErrorCode
import com.piappstudio.pinavigation.ErrorManager
import com.piappstudio.pinavigation.ErrorState
import com.piappstudio.pinavigation.NavManager
import com.piappstudio.pinetwork.PiRemoteDataRepository
import com.piappstudio.pitheme.component.UpdateScreen
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.AppTheme
import com.piappstudio.pitheme.theme.LocalAnalytic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navManager: NavManager
    @Inject
    lateinit var errorManager:ErrorManager

    @Inject
    lateinit var remoteDataRepository: PiRemoteDataRepository

    @Inject
    lateinit var piSession: PiSession

    @Inject
    lateinit var piTracker: PiTracker

    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            AppTheme {
                CompositionLocalProvider(LocalAnalytic provides piTracker) {
                    SetUpAppNavGraph()
                    fetchConfig()
                }

            }
        }
    }

    @Composable
    private fun fetchConfig() {

        val context = LocalContext.current

        var forceUpdate by remember { mutableStateOf(false) }
        if (forceUpdate) {
            UpdateScreen()
        }
        lifecycleScope.launchWhenCreated {
            remoteDataRepository.fetchAppConfig().onEach {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        piSession.appConfig = it.data
                        forceUpdate = piSession.isRequiredUpdate()
                    }
                    Resource.Status.ERROR -> {
                        Timber.e(Throwable("Error while fetch config: ${it.error?.message}"))
                    } else -> {

                    }
                }
            }.collect()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SetUpAppNavGraph() {
        navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        HandleError(snackbarHostState)
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            Surface(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                AppNavGraph(navController = navController)
            }
        }
       // firebaseAnalytics.logEvent("Open_App",null)

        // Listen for navigation change and execute the navigation
        val navInfo by navManager.routeInfo.collectAsState()
        LaunchedEffect(key1 = navInfo) {
            navInfo.id?.let {
                if (it == Route.Control.Back) {
                   // firebaseAnalytics.logEvent("Click_Back",null)

                    navController.navigateUp()
                    navManager.navigate(null)
                    return@let
                }
                var bundle=Bundle()
                bundle.putString("link",it)
               // firebaseAnalytics.logEvent("Click_Navigate",bundle)

                navController.navigate(it, navOptions = navInfo.navOption)
                navManager.navigate(null)
            }

        }

    }

    @Composable
    fun HandleError(snackbarHostState: SnackbarHostState) {
        val errorInfo by errorManager.errorInfo.collectAsState()
        val retry = if (errorInfo.action==null) null else stringResource(id = com.piappstudio.pitheme.R.string.retry)

        val serverFailMessage = stringResource(id = com.piappstudio.pitheme.R.string.general_error)

        var updatedErrorMessage = errorInfo.copy()
        Timber.d("Handle Error function is called")
        if (errorInfo.errorState == ErrorState.SERVER_FAIL || errorInfo.piError != null) {
            updatedErrorMessage = if (errorInfo.piError != null) {
                val errorMessage =
                    when (errorInfo.piError?.code) {
                        ErrorCode.DATABASE_ERROR -> {
                            stringResource(id = com.piappstudio.pitheme.R.string.db_error)
                        }
                        else -> {
                            serverFailMessage
                        }
                    }
                errorInfo.copy(message = errorMessage)

            } else {
                errorInfo.copy(message = serverFailMessage)
            }
        }

        LaunchedEffect(errorInfo) {

            updatedErrorMessage.message?.let { message ->
                if (updatedErrorMessage.errorState == ErrorState.POSITIVE) {
                    snackbarHostState.showSnackbar(
                        message,
                        withDismissAction = false,
                        duration = SnackbarDuration.Short
                    )
                    errorManager.post(null)
                } else if (updatedErrorMessage.errorState != ErrorState.NONE) {
                    val displayMessage = updatedErrorMessage.message
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = displayMessage ?: EMPTY_STRING,
                        actionLabel = retry,
                        withDismissAction = true,
                        duration = SnackbarDuration.Indefinite
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        errorManager.post(null)
                        updatedErrorMessage.action?.invoke()
                    } else {
                        errorManager.post(null)
                    }
                }

            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
    }
}