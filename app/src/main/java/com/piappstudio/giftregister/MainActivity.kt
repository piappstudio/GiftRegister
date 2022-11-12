package com.piappstudio.giftregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.piappstudio.giftregister.navgraph.AppNavGraph
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pimodel.error.ErrorCode
import com.piappstudio.pinavigation.ErrorManager
import com.piappstudio.pinavigation.ErrorState
import com.piappstudio.pinavigation.NavManager
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    @Inject
    lateinit var navManager: NavManager
    @Inject
    lateinit var errorManager:ErrorManager



    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the FirebaseAnalytics instance.
       firebaseAnalytics=FirebaseAnalytics.getInstance(this)
        setContent {
            AppTheme {
                SetUpAppNavGraph()
            }
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
            Surface(modifier = Modifier.padding(it).fillMaxSize()) {
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