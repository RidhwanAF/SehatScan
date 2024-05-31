package com.healthy.sehatscan.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.healthy.sehatscan.R

class Route {

    enum class AuthRoute {
        Auth, // Parent
        Register,
        ForgetPassword,
        Login,
    }

    private enum class MainMenu {
        Home,
        History,
        Favorite,
        Profile
    }

    enum class ScreenRoute {
        ImageScan
    }

    sealed class MainScreen(
        val route: String,
        @StringRes val label: Int,
        @DrawableRes val unselectedIcon: Int,
        @DrawableRes val selectedIcon: Int,
    ) {
        data object Home : MainScreen(
            MainMenu.Home.name,
            R.string.home,
            R.drawable.ic_home,
            R.drawable.ic_home_filled
        )

        data object History : MainScreen(
            MainMenu.History.name,
            R.string.history,
            R.drawable.ic_history,
            R.drawable.ic_history
        )

        data object Favorite : MainScreen(
            MainMenu.Favorite.name,
            R.string.favorite,
            R.drawable.ic_favorite,
            R.drawable.ic_favorite_filled
        )

        data object Profile : MainScreen(
            MainMenu.Profile.name,
            R.string.profile,
            R.drawable.ic_person,
            R.drawable.ic_person_filled
        )
    }

}