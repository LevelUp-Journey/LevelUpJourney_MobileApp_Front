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
import upc.edu.pe.levelupjourney.ui.screens.auth.LoginScreen
import upc.edu.pe.levelupjourney.ui.screens.auth.SignUpScreen
import upc.edu.pe.levelupjourney.ui.screens.auth.TeacherVerificationScreen
import upc.edu.pe.levelupjourney.presentation.screen.main.MainScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.JoinScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.PinJoinScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.QRScanScreen
import upc.edu.pe.levelupjourney.presentation.screen.settings.SettingsScreen
import upc.edu.pe.levelupjourney.presentation.screen.community.CommunityScreen
import upc.edu.pe.levelupjourney.presentation.screen.community.CommentsScreen
import upc.edu.pe.levelupjourney.community.domain.model.Post
import upc.edu.pe.levelupjourney.community.domain.model.Comment
import upc.edu.pe.levelupjourney.presentation.screen.game.GameFinishedScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.ConnectingScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.EnterPinScreen
import upc.edu.pe.levelupjourney.presentation.screen.game.GameQuestionScreen
import upc.edu.pe.levelupjourney.presentation.screen.game.Question
import upc.edu.pe.levelupjourney.presentation.screen.join.JoinGameScreen
import upc.edu.pe.levelupjourney.presentation.screen.join.NicknameScreen

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

    val questions = listOf(
        Question(
            id = 1,
            title = "What is seen here?",
            placeholder = "Code/Image Placeholder",
            options = listOf("Terminology", "Function", "Error", "None of the above"),
            correctIndex = 1
        ),
        Question(
            id = 2,
            title = "Is this made in Go?",
            placeholder = "Placeholder for code snippet",
            options = listOf("True", "False"),
            correctIndex = 1
        )
    )

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
                onLoginSuccess = { 
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate("register") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("register") {
            SignUpScreen(
                onSignUpSuccess = { email: String, name: String, password: String ->
                    if ((email as String).startsWith("pc") && (email as String).endsWith("@upc.edu.pe")) {
                        // Teacher - go to verification (pass data for later registration)
                        navController.navigate("teacherVerification/$email/$name/$password")
                    } else {
                        // Student - registration completed, go directly to main
                        navController.navigate("main") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("teacherVerification/{email}/{name}/{password}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            TeacherVerificationScreen(
                email = email,
                name = name,
                password = password,
                onVerificationSuccess = {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("main") {
            MainScreen(
                onProfileClick = { /* No profile functionality */ },
                onMenuClick = { navController.navigate("settings") }
            )
        }

        composable("join") {
            JoinScreen(
                onProfileClick = { /* No profile functionality */ },
                onMenuClick = { navController.navigate("settings") },
                onQRScanClick = { navController.navigate("qrScan") },
                onPinJoinClick = { navController.navigate("pinJoin") }
            )
        }

        composable("qrScan") {
            QRScanScreen(
                onBack = { navController.popBackStack() },
                onScanSuccess = { 
                    // Navigate to joined activity or main
                    navController.navigate("main")
                }
            )
        }

        composable("pinJoin") {
            PinJoinScreen(
                onBack = { navController.popBackStack() },
                onJoinSuccess = { 
                    // Navigate to joined activity or main
                    navController.navigate("main")
                }
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
                onPostSelected = { post -> navController.navigate("comments/${post.id}") },
                onSettingsClick = { navController.navigate("settings") },
                onHomeClick = { navController.navigate("main") },
                onJoinClick = { navController.navigate("join") },
                onProfileClick = { navController.navigate("profile") },
                onCommunityClick = { /* stay in community */ }
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

        // ---- JOIN FLOW ----
        composable("join") {
            JoinGameScreen(
                onBackToHome = { navController.navigate("home") },
                onEnterPin = { navController.navigate("join/pin") },
                onScanCode = { /* TODO: QR */ }
            )
        }
        composable("join/pin") {
            EnterPinScreen(
                onBackToHome = { navController.navigate("home") },
                onEnterGame = { navController.navigate("join/connecting") },
                onScanCode = { /* TODO */ }
            )
        }
        composable("join/connecting") {
            ConnectingScreen(
                onFinished = { navController.navigate("join/nickname") }
            )
        }
        composable("join/nickname") {
            NicknameScreen(
                onBackToHome = { navController.navigate("home") },
                onEnterNickname = { navController.navigate("join/connecting-start") }
            )
        }
        composable("join/connecting-start") {
            ConnectingScreen(
                onFinished = { navController.navigate("game/question/1") }
            )
        }

        // ---- GAME QUESTIONS ----
            composable("game/question/{id}") { backStackEntry ->
                val id: Int? = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val qIndex: Int = questions.indexOfFirst { question: Question -> question.id == id }
                if (qIndex >= 0) {
                    GameQuestionScreen(
                        question = questions[qIndex],
                        onClose = { navController.navigate("home") },
                        onNext = {
                            if (qIndex + 1 < questions.size) {
                                navController.navigate("game/question/${questions[qIndex + 1].id}")
                            } else {
                                navController.navigate("game/finished")
                            }
                        }
                    )
                }
            }

        composable("game/finished") {
            GameFinishedScreen(
                onBackToHome = { navController.navigate("home") }
            )
        }
    }
}
