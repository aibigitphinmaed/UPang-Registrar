package com.ite393group5.android_app.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Output
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Queue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AppDrawer(
            currentRoute = currentRoute,
            closeDrawer = { coroutineScope.launch { drawerState.close() } },
            modifier = Modifier,
            navigateToDashboard = { navigationActions.navigateToDashboard() },
            navigateToQueueTicket = { navigationActions.navigateToQueueTicket() },
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
            navigateToRequestDocument = { navigationActions.navigateToRequestDocument() }
        )
    }) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDashboard: () -> Unit,
    navigateToQueueTicket: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToProfileManagement: () -> Unit,
    navigateToFeedbackSubmission: () -> Unit,
    navigateToHelpAndSupport: () -> Unit,
    navigateToLogout: () -> Unit,
    navigateToRequestDocument: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp


    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .widthIn(min = 0.dp, max = screenWidth / 2) // 🔹 Limit to Half Screen
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)) // 🔹 Rounded edges
            .shadow(8.dp)
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

//region UPang Registrar Logo
            // 🔹 Image at the Top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B5E20))
                    .padding(vertical = 24.dp)
                    .height(150.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "UPang Registrar Logo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                )
            }
            //endregion


            // 🔹 Spacing between sections
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray, thickness = 3.dp)
            Spacer(modifier = Modifier.height(12.dp))
            //region first section


            DrawerButton(modifier = modifier,
                Icons.Rounded.Home,
                contentDescription = stringResource(id = R.string.home_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.Dashboard.route,
                action = {
                    navigateToDashboard()
                    closeDrawer()
                })



            DrawerButton(modifier = modifier,
                Icons.Rounded.Book,
                contentDescription = stringResource(id = R.string.document_request_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.RequestDocument.route,
                action = {
                    navigateToRequestDocument()
                    closeDrawer()
                })
            DrawerButton(modifier = modifier,
                Icons.Rounded.History,
                contentDescription = stringResource(id = R.string.track_requested_documents_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.QueueTicket.route,
                action = {
                    navigateToQueueTicket()
                    closeDrawer()
                })
//            DrawerButton(modifier = modifier,
//                Icons.Rounded.EditNote,
//                contentDescription = stringResource(id = R.string.modify_appointments_content_description),
//                isSelected = currentRoute == NavigationRoutes.Authenticated.ModifyAppointments.route,
//                action = {
//                    navigateToModifyAppointments()
//                    closeDrawer()
//                })

            //endregion

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            //region second section
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)


            DrawerButton(modifier = modifier,
                Icons.Rounded.Person,
                contentDescription = stringResource(id = R.string.profile_management_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.ProfileManagement.route,
                action = {
                    navigateToProfileManagement()
                    closeDrawer()
                })
            DrawerButton(modifier = modifier,
                Icons.Rounded.Notifications,
                contentDescription = stringResource(id = R.string.notifications_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.Notifications.route,
                action = {
                    navigateToNotifications()
                    closeDrawer()
                })


            DrawerButton(modifier = modifier,
                Icons.Rounded.QuestionMark,
                contentDescription = stringResource(id = R.string.help_and_support_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.HelpAndSupport.route,
                action = {
                    navigateToHelpAndSupport()
                    closeDrawer()
                })
            DrawerButton(modifier = modifier,
                Icons.Rounded.Feedback,
                contentDescription = stringResource(id = R.string.feedback_submission_content_description),
                isSelected = currentRoute == NavigationRoutes.Authenticated.FeedbackSubmission.route,
                action = {
                    navigateToFeedbackSubmission()
                    closeDrawer()
                })
            //endregion

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.weight(1f))

            DrawerButton(modifier = modifier,
                Icons.Rounded.Output,
                contentDescription = stringResource(id = R.string.log_out_content_description),
                isSelected = false,
                action = {
                    closeDrawer()
                    navigateToLogout()
                })
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
        onClick = action, modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                dashboardIcon, contentDescription = null, // decorative
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
            navigateToQueueTicket = {},
            navigateToNotifications = {},
            navigateToProfileManagement = {},
            navigateToFeedbackSubmission = {},
            navigateToHelpAndSupport = {},
            navigateToLogout = {},
            navigateToRequestDocument = {}
        )
    }
}