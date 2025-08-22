package denys.diomaxius.assignment_two.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GalleryScreen(
    viewModel: GalleryScreenViewModel
) {
    Scaffold(
        topBar = {
            TopBar()
        }
    ) { innerPadding->
        Content(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        items(100) { index ->
            Text(
                text = "Item $index",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}