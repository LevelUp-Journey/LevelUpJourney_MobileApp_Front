package upc.edu.pe.levelupjourney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import upc.edu.pe.levelupjourney.presentation.screen.welcome.WelcomeScreen
import upc.edu.pe.levelupjourney.presentation.screen.login.LoginScreen
import upc.edu.pe.levelupjourney.presentation.screen.register.RegisterScreen
import upc.edu.pe.levelupjourney.presentation.screen.account.AccountTypeScreen
import upc.edu.pe.levelupjourney.presentation.screen.userinfo.UserInfoScreen
import upc.edu.pe.levelupjourney.presentation.screen.userinfo.BirthDateScreen
import upc.edu.pe.levelupjourney.presentation.screen.home.HomeScreen
import upc.edu.pe.levelupjourney.presentation.screen.profile.ProfileScreen
import upc.edu.pe.levelupjourney.presentation.screen.settings.SettingsScreen
import upc.edu.pe.levelupjourney.presentation.screen.community.CommunityScreen
import upc.edu.pe.levelupjourney.presentation.screen.community.CommentsScreen
import upc.edu.pe.levelupjourney.domain.model.Post
import upc.edu.pe.levelupjourney.domain.model.Comment

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {

    val posts = remember {
        mutableStateListOf(
            Post(1, "Teacher1", "First teacher post", likes = 12, comments = 2),
            Post(2, "Teacher2", "Another interesting post", likes = 5, comments = 0)
        )
    }

    val commentsMap = remember {
        mutableStateMapOf(
            1 to mutableStateListOf(
                Comment(1, "Student1", "Great post!"),
                Comment(2, "Student2", "Thanks for sharing 🙌")
            ),
            2 to mutableStateListOf<Comment>()
        )
    }

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
                onJoinTabClick = { /* TODO */ },
                onCommunityTabClick = { navController.navigate("community") }
            )
        }

        composable("profile") {
            ProfileScreen(
                onClose = { navController.popBackStack() },
                onSettingsClick = { navController.navigate("settings") },
                onEditClick = { /* TODO */ },
                onHomeClick = { navController.navigate("home") },
                onJoinClick = { /* TODO */ },
                onCommunityClick = { navController.navigate("community") }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("community") {
            CommunityScreen(
                posts = posts,
                onPostSelected = { post ->
                    navController.navigate("comments/${post.id}")
                },
                onSettingsClick = { navController.navigate("settings") },
                onHomeClick = { navController.navigate("home") },
                onJoinClick = { /* TODO */ },
                onProfileClick = { navController.navigate("profile") },
                onCommunityClick = { /* stays in community */ }
            )
        }

        composable("comments/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
            val post = posts.find { it.id == postId }
            val comments = commentsMap[postId]

            if (post != null && comments != null) {
                CommentsScreen(
                    post = post,
                    comments = comments,
                    onBack = { navController.popBackStack() },
                    onAddComment = { text ->
                        val newId = (comments.maxOfOrNull { it.id } ?: 0) + 1
                        comments.add(Comment(newId, "CurrentUser", text))
                        post.comments++
                    }
                )
            }
        }
    }
}
