package com.ite393group5.android_app

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ite393group5.android_app.routes.NavigationRoutes
import com.ite393group5.android_app.routes.authenticatedGraph
import com.ite393group5.android_app.routes.unauthenticatedGraph
import com.ite393group5.android_app.utilities.AppNavigationActions
import kotlinx.coroutines.CoroutineScope

//treat this as our navigation map graph
@Composable
fun CustomGraph(
    modifier: Modifier = Modifier,
    navController : NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navActions: AppNavigationActions = remember(navController){
        AppNavigationActions(navController)
    }
){

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    //Check the routes folder for routes and if you wanna add new routes for new screens.
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: NavigationRoutes.Authenticated.NavigationRoute.route

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Unauthenticated.NavigationRoute.route,
        modifier = modifier
    ){

        unauthenticatedGraph(navController, modifier)
        authenticatedGraph(drawerState, modifier, coroutineScope, navActions, currentRoute)

    }



}