package ua.edu.sumdu.movielibrary.presentation.movie_create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.domain.Genre
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieCreateScreen() {
    val viewModel: MovieCreateViewModel = koinViewModel()
    val movieState by viewModel.state.collectAsStateWithLifecycle()

    val allGenres = Genre.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Create Movie",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = movieState.title,
            onValueChange = { viewModel.onTitleChange(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = movieState.director,
            onValueChange = { viewModel.onDirectorChange(it) },
            label = { Text("Director") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = movieState.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = movieState.releaseYear,
            onValueChange = viewModel::onReleaseYearChange,
            label = { Text("Release Year") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Genres",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GenreDropdownSelector(
            allGenres = allGenres,
            selectedGenres = movieState.selectedGenres,
            onGenreSelected = viewModel::onGenreSelected
        )
        if (movieState.selectedGenres.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                movieState.selectedGenres.forEach { genre ->
                    Chip(
                        text = genre.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        onClick = { viewModel.onGenreSelected(genre) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ImagePicker(
            currentImageUri = movieState.imageUri,
            onImageSelected = viewModel::onImageSelected
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                val movie = MovieDto(
                    title = movieState.title,
                    director = movieState.director,
                    genres = movieState.selectedGenres.map { it.name },
                    releaseYear = movieState.releaseYear,
                    imageUrl = movieState.imageUri.toString()
                )

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Create Movie")
        }
    }
}

@Composable
fun ImagePicker(
    currentImageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentImageUri != null) {
            AsyncImage(
                model = currentImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Image",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
    }
}

@Composable
fun GenreDropdownSelector(
    allGenres: List<Genre>,
    selectedGenres: List<Genre>,
    onGenreSelected: (Genre) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val columns = 3
    val rows = (allGenres.size + columns - 1) / columns

    Box {
        Button(onClick = { expanded = true }) {
            Text("Select Genres")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (rowIndex in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (columnIndex in 0 until columns) {
                            val genreIndex = rowIndex * columns + columnIndex
                            if (genreIndex < allGenres.size) {
                                val genre = allGenres[genreIndex]
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            onGenreSelected(genre)
                                        }
                                ) {
                                    Checkbox(
                                        checked = selectedGenres.contains(genre),
                                        onCheckedChange = null
                                    )
                                    Text(
                                        text = genre.name.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                        modifier = Modifier.padding(start = 8.dp),
                                        fontSize = 15.sp
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}