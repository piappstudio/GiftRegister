package com.piappstudio.giftregister.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.piappstudio.authentication.authNavGraph
import com.piappstudio.picloud.driveGraph
import com.piappstudio.pitheme.route.Root
import com.piappstudio.register.homeGraph
import com.piappstudio.welcome.welcomeNavGraph

/** To build the navigation graph*/
@Composable
fun AppNavGraph(navController: NavHostController) {
   NavHost(navController = navController , startDestination = Root.WELCOME, route = Root.APPROOT) {
      welcomeNavGraph()
      homeGraph()
      authNavGraph()
      driveGraph()

   }
}