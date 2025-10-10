package upc.edu.pe.levelupjourney.presentation.screen.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import upc.edu.pe.levelupjourney.R
import upc.edu.pe.levelupjourney.community.domain.model.Post

@Composable
fun CommunityScreen(
    posts: List<Post>,
    onPostSelected: (Post) -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onJoinClick: () -> Unit,
    onCommunityClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(2) } // Community activo

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile"
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = R.drawable.logo,
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 4.dp)
                    )
                    Text("Level Up Journey", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onHomeClick()
                    },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onJoinClick()
                    },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = "Join") },
                    label = { Text("Join") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onCommunityClick()
                    },
                    icon = { Icon(Icons.Outlined.Group, contentDescription = "Community") },
                    label = { Text("Community") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onLikeClick = {
                        post.liked = !post.liked
                        post.likes = if (post.liked) post.likes + 1 else post.likes - 1
                    },
                    onCommentClick = { onPostSelected(post) }
                )
            }
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // user info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "User",
                modifier = Modifier.padding(end = 8.dp)
            )
            Column {
                Text(post.user, style = MaterialTheme.typography.bodyMedium)
                Text(post.description, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(8.dp))

        // placeholder image
        AsyncImage(
            model = R.drawable.ic_launcher_foreground,
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(Modifier.height(8.dp))

        // likes and comments
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (post.liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.liked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text("${post.likes}")

            Spacer(Modifier.width(16.dp))

            IconButton(onClick = onCommentClick) {
                Icon(
                    imageVector = Icons.Filled.AddComment,
                    contentDescription = "Comments"
                )
            }
            Text("${post.comments}")
        }
    }
}
