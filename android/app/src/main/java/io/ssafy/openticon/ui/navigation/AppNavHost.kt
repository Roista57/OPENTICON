package io.ssafy.openticon.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import io.ssafy.openticon.ui.screen.EditProfileScreen
import io.ssafy.openticon.ui.screen.EmoticonAllScreen
import io.ssafy.openticon.ui.screen.EmoticonPackDetailScreen
import io.ssafy.openticon.ui.screen.LoginScreen
import io.ssafy.openticon.ui.screen.LoginSuccessScreen
import io.ssafy.openticon.ui.screen.MainScreen
import io.ssafy.openticon.ui.screen.SettingsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current as Activity

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }

        composable(
            route = "emoticonPack/{uuid}",
            arguments = listOf(navArgument("uuid") { type = NavType.StringType })
        ) { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid")
            if (uuid != null) {
                EmoticonPackDetailScreen(emoticonPackUuid = uuid, navController = navController)
            }
        }


        composable(
            route = "login_success?access_token={access_token}",
            arguments = listOf(
                navArgument("access_token") {
                    type = NavType.StringType
                    nullable = true
                },
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "openticon://successLogin?access_token={access_token}" }
            )
        ) { backStackEntry ->
            val accessToken = backStackEntry.arguments?.getString("access_token") ?: ""
            LoginSuccessScreen(accessToken, navController)
        }

        composable("emoticonAll/{type}") { backStackEntry ->
            val arg1 = backStackEntry.arguments?.getString("type")
            EmoticonAllScreen(navController, arg1, null)
        }

        composable(
            route = "emoticonAll/{type}/{tag}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("tag") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val tag = backStackEntry.arguments?.getString("tag")
            EmoticonAllScreen(navController, type, tag)
        }
//
//        composable(
//            route = ""
//            deepLinks = listOf(
//                navDeepLink { uriPattern = "openticon://emoticon-pack/{uuid}" }
//            )
//        ) {  }

    }
}