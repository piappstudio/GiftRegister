package com.piappstudio.welcome.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import com.piappstudio.pinavigation.NavInfo
import com.piappstudio.pitheme.component.PILottie
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.welcome.R
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center)
                    .align(
                        Alignment.Center
                    )
            ) {

                val progress = PILottie(resourceId = R.raw.gift, modifier = Modifier.fillMaxSize(0.6f))
                Text(text =  stringResource(id = R.string.title_gift_register), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(Dimen.space))
                Text(text = stringResource(R.string.welcome_description), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(Dimen.space))
                LaunchedEffect(key1 = progress) {
                    Timber.d("Progress : $progress")
                    if (progress == 1.0f) {


                        viewModel.navManager.navigate(NavInfo(id = Root.HOME,
                            navOption = NavOptions.Builder().setPopUpTo(Route.Welcome.SPLASH, inclusive = true).build()))

                        Timber.d("Animation is completed")
                    }
                }
            }

            Text(text = stringResource(R.string.copy_rights), color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Light, modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .align(
                    Alignment.BottomCenter
                )
                .padding(Dimen.doubleSpace))

        }
    }
}