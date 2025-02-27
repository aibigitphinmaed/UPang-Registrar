package com.ite393group5.android_app.routes

import androidx.compose.material3.DrawerState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ite393group5.android_app.appointmentbooking.AppointmentBookingScreen
import com.ite393group5.android_app.dashboard.DashboardScreen
import com.ite393group5.android_app.loadingscreen.LoadingScreen
import com.ite393group5.android_app.login.LoginScreen
import com.ite393group5.android_app.logout.LogoutScreen
import com.ite393group5.android_app.profilemanagement.ProfileScreen
import com.ite393group5.android_app.utilities.AppModalDrawer
import com.ite393group5.android_app.utilities.AppNavigationActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//please do not touch this part without consulting me
fun NavGraphBuilder.unauthenticatedGraph(
    navController: NavController,
    modifier: Modifier
) {
    navigation(
        route = NavigationRoutes.Unauthenticated.NavigationRoute.route,
        startDestination = NavigationRoutes.Unauthenticated.Login.route
    ) {
        composable(route = NavigationRoutes.Unauthenticated.Login.route) {
            LoginScreen(
                onNavigationToAuthenticatedRoutes = {
                   navController.navigate(route = NavigationRoutes.Unauthenticated.LoadingScreen.route)
                }
            )
        }
        //start LoadingScreen
        composable(route = NavigationRoutes.Unauthenticated.LoadingScreen.route) {
            LoadingScreen(
                navigateToDashboard = {
                    navController.navigate(route = NavigationRoutes.Authenticated.NavigationRoute.route) {
                        popUpTo(route = NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }


    }
}
//end of do not touch this


//here everyone can touch on the Authenticated routes
fun NavGraphBuilder.authenticatedGraph(
    drawerState: DrawerState,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    appNavigationActions: AppNavigationActions,
    currentRoute: String
) {
    navigation(
        route = NavigationRoutes.Authenticated.NavigationRoute.route,
        startDestination = NavigationRoutes.Authenticated.Dashboard.route
    ) {


        //region start Dashboard
        composable(route = NavigationRoutes.Authenticated.Dashboard.route) {
            AppModalDrawer(drawerState, currentRoute, appNavigationActions) {
                DashboardScreen(
                    modifier = modifier,
                    openDrawer = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    onNavigateToProfileScreen = {
                        appNavigationActions.navigateToProfileManagement()
                    }
                )
            }
        }
        //endregion

        //region start logout
         composable(route = NavigationRoutes.Authenticated.Logout.route) {
             LogoutScreen(
                 onNavigationToUnauthenticatedRoutes = {
                     appNavigationActions.logout()
                 }
             )
         }
        //endregion

        //region start of Appointment Booking
        composable(route = NavigationRoutes.Authenticated.AppointmentBooking.route){
            AppModalDrawer(drawerState, currentRoute, appNavigationActions) {
                AppointmentBookingScreen(
                    modifier = modifier,
                    openDrawer = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
        //endregion

        //region start of Profile Screen
        composable(route = NavigationRoutes.Authenticated.ProfileManagement.route) {
            AppModalDrawer(drawerState, currentRoute, appNavigationActions) {
                ProfileScreen(
                    modifier = modifier,
                    openDrawer = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
        //endregion

        //region start of appointment booking
        composable(route = NavigationRoutes.Authenticated.AppointmentBooking.route){
            AppModalDrawer(drawerState, currentRoute, appNavigationActions) {
                AppointmentBookingScreen(
                    modifier = modifier,
                    openDrawer = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
        //endregion
    }

}