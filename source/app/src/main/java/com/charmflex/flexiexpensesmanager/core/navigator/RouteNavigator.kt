package com.charmflex.flexiexpensesmanager.core.navigator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface RouteNavigator {

    val navigationEvent: Flow<NavigationEvent>

    fun navigateTo(route: String)
    fun navigateAndPopUpTo(route: String, popUpToRouteInclusive: String)
    fun pop()
    fun popWithArguments(data: Map<String, Any>? = null)

    companion object {
        val instance by lazy { RouteNavigatorImpl() }
    }
}

class RouteNavigatorImpl @Inject constructor() : RouteNavigator {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    override val navigationEvent: Flow<NavigationEvent>
        get() = _navigationEvent.asSharedFlow()

    override fun navigateTo(route: String) {
        _navigationEvent.tryEmit(NavigateTo(route))
    }

    override fun navigateAndPopUpTo(route: String, popUpToRouteInclusive: String) {
        _navigationEvent.tryEmit(NavigateAndPopUpTo(route = route, popToRouteInclusive = popUpToRouteInclusive))
    }

    override fun pop() {
        _navigationEvent.tryEmit(Pop)
    }

    override fun popWithArguments(data: Map<String, Any>?) {
        _navigationEvent.tryEmit(PopWithArguments(data))
    }


}

sealed interface NavigationEvent

data class NavigateTo(val route: String) : NavigationEvent
data class NavigateAndPopUpTo(val route: String, val popToRouteInclusive: String): NavigationEvent
object Pop : NavigationEvent
data class PopWithArguments(
    val data: Map<String, Any>? = null
) : NavigationEvent