package ua.edu.sumdu.movielibrary.navigation

//@Serializable
//object MovieGraph
//
//@Composable
//fun MovieNavHostt(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    NavHost(
//        navController = navController,
//        startDestination = MovieGraph,
//        modifier = modifier
//    ) {
//        movieGraph(navController)
//    }
//}
//
//fun NavGraphBuilder.movieGraph(navController: NavController) {
//    navigation<MovieGraph>(startDestination = MovieListObject) {
//        composable<MovieListObject> {
//            MovieListScreen { navData ->
//                navController.navigate(navData)
//            }
//        }
//
//        composable<MovieDto> { backStackEntry ->
//            val movie = backStackEntry.toRoute<MovieDto>()
//            MovieScreen(movie)
//        }
//    }
//}
//
//@Composable
//fun MovieListScreen(
//    onNavigationToMovie: (MovieDto) -> Unit
//) {
//    val context = LocalContext.current
//    val fireBaseRepository = FireBaseRepository()
//
//    Button(onClick = {
//        val task = fireBaseRepository.getImageTask(context)
//        task.addOnSuccessListener { uploadTask ->
//            uploadTask.metadata?.reference
//                ?.downloadUrl?.addOnCompleteListener{ uriTask ->
//                    fireBaseRepository.saveMovie(Movie())
//            }
//        }
//    }) { Text(text = "Add") }
//
//    val list = remember {
//        mutableStateOf(emptyList<Movie>())
//    }
//
//    val listener = fireBaseRepository.getMovies().addSnapshotListener { snapShot, exception ->
//        list.value = snapShot?.toObjects(Movie::class.java) ?: emptyList()
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f)
//        ) {
//            items(list.value) { movie ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp),
//                    onClick = {
//                        onNavigationToMovie(movie.toMovieDto())
//                    }
//                ) {
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        AsyncImage(model = movie.imageUrl , contentDescription = null,
//                            modifier = Modifier.height(100.dp).width(100.dp))
//                        Text(text = movie.title)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MovieScreen(movie: MovieDto) {
//    Text(text = "Movie ${movie.title}, ${movie.director}")
//}


