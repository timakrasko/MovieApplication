package ua.edu.sumdu.movielibrary.data

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import ua.edu.sumdu.movielibrary.domain.Movie


class FireBaseRepositoryTest {
    @Test
    fun test1() {
        val mockFirestore = mock<FireBaseRepository>();
        val mockCollection = mock(CollectionReference::class.java)
        val mockDocument = mock(DocumentReference::class.java)

        // Налаштовуємо моки
        `when`(mockFirestore.fs.collection("movies")).thenReturn(mockCollection)
        `when`(mockCollection.document()).thenReturn(mockDocument)

        // Створюємо об'єкт класу з замоканим Firestore

        // Викликаємо метод saveMovie
        val url = "http://example.com/movie.jpg"
        mockFirestore.saveMovie(url)

        // Перевіряємо, що виклики виконуються правильно
        verify(mockFirestore).fs.collection("movies") // Перевіряємо виклик collection("movies")
        verify(mockCollection).document()          // Перевіряємо виклик document()
        verify(mockDocument).set(
            Movie(
                "The Lord of The Rings",
                "desc1",
                url,
                "Piter Jackson",
                listOf("Fantasy", "Drama", "Action")
            )
        )
    }

}