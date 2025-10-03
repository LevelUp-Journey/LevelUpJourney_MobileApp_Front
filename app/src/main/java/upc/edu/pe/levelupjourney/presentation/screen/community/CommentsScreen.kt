package upc.edu.pe.levelupjourney.presentation.screen.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import upc.edu.pe.levelupjourney.R
import upc.edu.pe.levelupjourney.domain.model.Post
import upc.edu.pe.levelupjourney.domain.model.Comment

@Composable
fun CommentsScreen(
    post: Post,
    comments: MutableList<Comment>,
    onBack: () -> Unit,
    onAddComment: (String) -> Unit
) {
    var newComment by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }

            Divider()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(comments) { comment ->
                    Column(Modifier.padding(vertical = 8.dp)) {
                        Text(comment.user, style = MaterialTheme.typography.bodyMedium)
                        Text(comment.text, style = MaterialTheme.typography.bodySmall)
                    }
                    Divider()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    placeholder = { Text("Post your reply") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newComment.isNotBlank()) {
                        onAddComment(newComment)
                        newComment = ""
                    }
                }) {
                    Text("Post")
                }
            }
        }
    }
}
