package com.ite393group5.android_app.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.services.local.LocalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val localServiceImpl: LocalService,
) : ViewModel()
{
    private val _dashboardStateView = MutableStateFlow(DashboardState())
    val dashboardStateView: StateFlow<DashboardState> = _dashboardStateView

    init {
        viewModelScope.launch {
            val personalInfo = localServiceImpl.getPersonalInfo()
            _dashboardStateView.value = _dashboardStateView.value.copy(
                fullName = personalInfo.lastName + ", " + personalInfo.firstName,
                personalInfo = personalInfo
            )
        }
    }
}