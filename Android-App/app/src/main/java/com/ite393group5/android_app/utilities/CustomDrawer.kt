package com.ite393group5.android_app.utilities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Output
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Queue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.R
import com.ite393group5.android_app.routes.NavigationRoutes
import com.ite393group5.android_app.theme.CustomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: AppNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToDashboard = { navigationActions.navigateToDashboard() },
                navigateToAppointmentBooking = { navigationActions.navigateToAppointmentBooking() },
                navigateToQueueTicket = { navigationActions.navigateToQueueTicket() },
                navigateToModifyAppointments = { navigationActions.navigateToModifyAppointments() },
                navigateToNotifications = { navigationActions.navigateToNotifications() },
                navigateToProfileManagement = { navigationActions.navigateToProfileManagement() },
                navigateToFeedbackSubmission = { navigationActions.navigateToFeedbackSubmission() },
                navigateToHelpAndSupport = { navigationActions.navigateToHelpAndSupport() },
                navigateToLogout = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navigationActions.navigateToLogoutScreen()
                },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDashboard: () -> Unit,
    navigateToAppointmentBooking: () -> Unit,
    navigateToQueueTicket: () -> Unit,
    navigateToModifyAppointments: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToProfileManagement: () -> Unit,
    navigateToFeedbackSubmission: () -> Unit,
    navigateToHelpAndSupport: () -> Unit,
    navigateToLogout: () -> Unit
) {

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier.fillMaxSize()) {
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Dashboard,
                contentDescription = stringResource(id = R.string.dashboard_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.Dashboard.route,
                action = {
                    navigateToDashboard()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Book,
                contentDescription = stringResource(id = R.string.appointment_booking_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.AppointmentBooking.route,
                action = {
                    navigateToAppointmentBooking()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Queue,
                contentDescription = stringResource(id = R.string.queue_ticket_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.QueueTicket.route,
                action = {
                    navigateToQueueTicket()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.EditNote,
                contentDescription = stringResource(id = R.string.modify_appointments_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.ModifyAppointments.route,
                action = {
                    navigateToModifyAppointments()
                    closeDrawer()
                }
            )

            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Notifications,
                contentDescription = stringResource(id = R.string.notifications_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.Notifications.route,
                action = {
                    navigateToNotifications()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Person,
                contentDescription = stringResource(id = R.string.profile_management_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.ProfileManagement.route,
                action = {
                    navigateToProfileManagement()
                    closeDrawer()
                }
            )

            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Feedback,
                contentDescription = stringResource(id = R.string.feedback_submission_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.FeedbackSubmission.route,
                action = {
                    navigateToFeedbackSubmission()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.QuestionMark,
                contentDescription = stringResource(id = R.string.help_and_support_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.HelpAndSupport.route,
                action = {
                    navigateToHelpAndSupport()
                    closeDrawer()
                }
            )
            DrawerButton(
                modifier = modifier,
                Icons.Rounded.Output,
                contentDescription = stringResource(id = R.string.log_out_content_description),
                isSelected = false,
                action = {
                    closeDrawer()
                    navigateToLogout()
                }
            )
        }
    }

}

@Composable
fun DrawerButton(
    modifier: Modifier,
    dashboardIcon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                dashboardIcon,
                contentDescription = null, // decorative
                tint = tintColor
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = contentDescription,
                style = MaterialTheme.typography.bodySmall,
                color = tintColor
            )
        }
    }
}


@Preview("Drawer Contents")
@Composable
fun AppModalDrawerPreview() {
    CustomTheme {
        AppDrawer(
            currentRoute = "Test",
            closeDrawer = {},
            modifier = Modifier,
            navigateToDashboard = { },
            navigateToAppointmentBooking = { },
            navigateToQueueTicket = {},
            navigateToModifyAppointments = {},
            navigateToNotifications = {},
            navigateToProfileManagement = {},
            navigateToFeedbackSubmission = {},
            navigateToHelpAndSupport = {},
            navigateToLogout = {}
        )
    }
}