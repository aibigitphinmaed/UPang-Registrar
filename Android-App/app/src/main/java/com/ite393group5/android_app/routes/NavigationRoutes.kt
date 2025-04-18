package com.ite393group5.android_app.routes



sealed class NavigationRoutes{
    sealed class Unauthenticated(val route: String): NavigationRoutes(){
        data object Login: Unauthenticated(route ="login")
        data object LoadingScreen: Authenticated("LoadingScreen")
        data object NavigationRoute: Unauthenticated(route = "unauthenticated")
    }

    sealed class Authenticated(val route: String): NavigationRoutes(){

        data object NavigationRoute: Authenticated("authenticated")
        data object Dashboard: Authenticated("Dashboard")
        data object Logout: Authenticated("Logout")
        data object ProfileManagement: Authenticated("ProfileManagement")


        //remove feature
        data object AppointmentBooking: Authenticated("AppointmentBooking")
        data object QueueTicket: Authenticated("QueueTicket")
        data object ModifyAppointments: Authenticated("ModifyAppointments")
        //end of remove feature




        data object Notifications: Authenticated("Notifications")
        data object FeedbackSubmission: Authenticated("FeedbackSubmission")
        data object HelpAndSupport: Authenticated("HelpAndSupport")
        data object RequestDocument: Authenticated("RequestDocument")
    }

}