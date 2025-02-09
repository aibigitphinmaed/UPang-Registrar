package com.ite393group5.android_app.utilities

import androidx.navigation.NavHostController
import com.ite393group5.android_app.routes.NavigationRoutes
import javax.inject.Singleton

@Singleton
class AppNavigationActions(private val navController: NavHostController) {

    //these functions will be pass to our screens hilt view models so that we can navigate to every authenticated routes only

    fun navigateToDashboard() {
        navController.navigate(
            route = NavigationRoutes.Authenticated.Dashboard.route
        )
    }

    //always use the  navController to change screens!!!


    fun navigateToAppointmentBooking() {
        TODO("Not yet implemented")
    }

    fun navigateToQueueTicket() {
        TODO("Not yet implemented")
    }

    fun navigateToModifyAppointments() {
        TODO("Not yet implemented")
    }

    fun navigateToNotifications() {
        TODO("Not yet implemented")
    }

    fun navigateToProfileManagement() {
        TODO("Not yet implemented")
    }

    fun navigateToFeedbackSubmission() {
        TODO("Not yet implemented")
    }

    fun navigateToHelpAndSupport() {
        TODO("Not yet implemented")
    }

    //same as log-out
    fun navigateToLogin() {
        TODO("Not yet implemented")
    }

}