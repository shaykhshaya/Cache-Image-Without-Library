package com.shaya.cacheImage

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.shaya.cacheImage.common.getResizeBitmap
import com.shaya.cacheImage.data.CacheRepositoryImpl
import com.shaya.cacheImage.database.CacheDatabase
import com.shaya.cacheImage.database.DatabaseName
import com.shaya.cacheImage.model.CacheModel
import com.shaya.cacheImage.network.ImageDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CacheImageBuilder {

    private var database: CacheDatabase? = null
    private val cacheRepository by lazy {
        database?.dao?.let { CacheRepositoryImpl(it) }
    }
    private val lruCache = mutableMapOf<String, Uri>()

    private fun initDatabase(context: Context): CacheDatabase =
        Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            DatabaseName
        ).build().also {
            database = it
        }

    private fun insertCache(
        url: String,
        uri: Uri,
        scope: LifecycleCoroutineScope
    ) = scope.launch(Dispatchers.IO) {
        cacheRepository?.save(
            CacheModel(
                url,
                uri.toString()
            )
        )
    }


    fun load(
        context: Context,
        url: String,
        imageView: ImageView,
        callback: Callback
    ) {
        if (database == null) initDatabase(context)

        callback.onLoading()

        imageView.doOnAttach {

            val scope = imageView.findViewTreeLifecycleOwner()?.lifecycleScope

            scope?.launch {

                val onUri = { uri: Uri ->
                    imageView.doOnPreDraw {
                        scope.launch {
                            val bitmap = withContext(Dispatchers.Default) {
                                getResizeBitmap(
                                    context = context,
                                    uri = uri,
                                    reqHeight = it.height,
                                    reqWidth = it.width
                                )
                            }
                            imageView.setImageBitmap(bitmap)
                            callback.onLoaded()
                        }
                    }
                }

                val cachedMapUri = lruCache[url]
                if (cachedMapUri != null) {
                    onUri(cachedMapUri)
                    return@launch
                }


                val cachedUri = cacheRepository?.get(url)?.first()?.uri
                if (!cachedUri.isNullOrEmpty()) {
                    val uri = cachedUri.toUri()
                    lruCache[url] = uri
                    onUri(uri)
                    return@launch
                }

                onLifeCycleAwareUrlToUri(
                    context = context,
                    scope = scope,
                    url = url,
                    onUri = { uri ->
                        lruCache[url] = uri
                        onUri(uri)
                    }
                )
            }
        }

        imageView.doOnDetach {
            imageView.setImageURI(null)
        }
    }


    private fun onLifeCycleAwareUrlToUri(
        context: Context,
        scope: LifecycleCoroutineScope,
        url: String,
        onUri: (Uri) -> Unit
    ) = scope.launch {

        ImageDownloader(context).downloadImage(
            url = url,
            scope = scope,
            onDownloaded = {
                onUri(it)
                insertCache(url, it, this)
            }
        )
    }


    class with(val context: Context) {

        private var url: String = ""
        private lateinit var callback: Callback

        fun url(url: String) = apply {
            this.url = url
        }

        fun callback(callback: Callback) = apply {
            this.callback = callback
        }


        fun into(imageView: ImageView) = load(
            context = context,
            url = url,
            imageView = imageView,
            callback = callback,
        )
    }

    interface Callback {
        fun onLoading()
        fun onLoaded()
    }

}

