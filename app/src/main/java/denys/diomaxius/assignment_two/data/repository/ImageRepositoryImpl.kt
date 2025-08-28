package denys.diomaxius.assignment_two.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.LruCache
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import denys.diomaxius.assignment_two.utils.applyExifRotation
import denys.diomaxius.assignment_two.utils.calculateInSampleSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class ImageRepositoryImpl(private val context: Context) : ImageRepository {
    private val resolver: ContentResolver get() = context.contentResolver
    private val thumbnailCache: LruCache<String, Bitmap>

    private val decodeSemaphore = Semaphore(2)

    //Basic settings for working with memory.
    // Allocates memory for the application
    init {
        val maxMemoryKb = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSizeKb = maxMemoryKb / 8

        thumbnailCache = object : LruCache<String, Bitmap>(cacheSizeKb) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    override suspend fun clearThumbnailCache() = withContext(Dispatchers.IO) {
        Log.i("Cache", "Clear Cache")
        thumbnailCache.evictAll()
    }

    //Reads all the pictures from the phone and returns list of pictures.
    //Default path for images.
    override suspend fun loadImageUris(): List<ImageItem> = withContext(Dispatchers.IO) {
        val uris = mutableListOf<ImageItem>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        resolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                uris += ImageItem(id = id, uri = contentUri)
            }
        }
        uris
    }

    //Tries to read/get thumbnails from memory.
    //If there are none in memory, creates a new thumbnail and saves it to memory.
    //Returns thumbnails
    override suspend fun loadThumbnail(
        uri: Uri,
        reqWidth: Int,
        reqHeight: Int,
    ): Bitmap? =
        decodeSemaphore.withPermit {
            withContext(Dispatchers.IO) {
                Log.d("ThumbPerf", "start decode $uri on ${Thread.currentThread().name}")

                val key = "${uri}_${reqWidth}_${reqHeight}"

                thumbnailCache.get(key)?.let {
                    Log.i("Cache", "Took photo from cache")
                    return@withContext it
                }

                try {
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    context.contentResolver.openInputStream(uri).use { input ->
                        BitmapFactory.decodeStream(input, null, options)
                    }

                    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
                    options.inJustDecodeBounds = false

                    val bitmap = context.contentResolver.openInputStream(uri).use { input ->
                        BitmapFactory.decodeStream(input, null, options)
                    }

                    val rotated = bitmap?.let { applyExifRotation(context, uri, it) }

                    rotated?.let {
                        thumbnailCache.put(key, it)
                        Log.i("Cache", "Put photo to cache")
                    }

                    Log.d("ThumbPerf", "end decode $uri size=${rotated?.width}x${rotated?.height}")

                    rotated
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
}