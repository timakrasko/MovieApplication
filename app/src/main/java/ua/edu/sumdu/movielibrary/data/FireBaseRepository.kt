package ua.edu.sumdu.movielibrary.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import ua.edu.sumdu.movielibrary.R
import ua.edu.sumdu.movielibrary.domain.Movie
import java.io.ByteArrayOutputStream

class FireBaseRepository {

    val fs = Firebase.firestore
    val storage = Firebase.storage.reference.child("images")

    fun getImageTask(context: Context): UploadTask{
        return storage.child("lotr.jpeg").putBytes(
            bitmapToByteArray(context)
        )
    }

    fun getMovies() = fs.collection("movies")

    private fun bitmapToByteArray(context: Context): ByteArray{
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lotr)
        val boas = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas)
        return boas.toByteArray()
    }

    fun saveMovie(movie: Movie){
        fs.collection("movies")
            .document()
            .set(
                movie
            )
    }
}