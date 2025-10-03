package upc.edu.pe.levelupjourney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import upc.edu.pe.levelupjourney.presentation.screen.welcome.WelcomeScreen
import upc.edu.pe.levelupjourney.presentation.screen.login.LoginScreen
import upc.edu.pe.levelupjourney.presentation.screen.register.RegisterScreen
import upc.edu.pe.levelupjourney.presentation.screen.account.AccountTypeScreen
import upc.edu.pe.levelupjourney.presentation.screen.home.HomeScreen
import upc.edu.pe.levelupjourney.presentation.screen.profile.ProfileScreen
import upc.edu.pe.levelupjourney.presentation.screen.settings.SettingsScreen
import upc.edu.pe.levelupjourney.presentation.screen.userinfo.BirthDateScreen
import upc.edu.pe.levelupjourney.presentation.screen.userinfo.UserInfoScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("login") {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate("accountType") }
            )
        }

        composable("register") {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate("accountType") }
            )
        }

        composable("accountType") {
            AccountTypeScreen(
                onBack = { navController.popBackStack() },
                onTeacherSelected = { navController.navigate("userInfo") },
                onStudentSelected = { navController.navigate("userInfo") }
            )
        }

        composable("userInfo") {
            UserInfoScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate("birthDate") }
            )
        }

        composable("birthDate") {
            BirthDateScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                onProfileClick = { navController.navigate("profile") },
                onSettingsClick = { navController.navigate("settings") },
                onJoinTabClick = { /* TODO: ir a Join */ },
                onCommunityTabClick = { /* TODO: ir a Community */ }
            )
        }


        composable("profile") {
            ProfileScreen(
                onClose = { navController.popBackStack() },
                onSettingsClick = { navController.navigate("settings") },
                onEditClick = { /* TODO: ir a Edit Profile */ },
                onHomeClick = { navController.navigate("home") },
                onJoinClick = { /* TODO: ir a Join */ },
                onCommunityClick = { /* TODO: ir a Community */ }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true } // limpia el backstack
                    }
                }
            )
        }

    }
}
