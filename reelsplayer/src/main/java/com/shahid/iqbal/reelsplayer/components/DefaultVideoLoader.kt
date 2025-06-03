package com.shahid.iqbal.reelsplayer.components

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
 * Created by Shahid Iqbal on 7/20/2024.
 */

/**
 * A composable function that displays a default loading indicator for video content.
 * This is typically used while the video is buffering or loading.
 *
 * @param modifier A [Modifier] to apply to this layout.
 * @param progressColor The color of the progress indicator.
 * @param strokeWidth The width of the stroke for the progress indicator.
 */

@Composable
fun DefaultVideoLoader(
    modifier: Modifier = Modifier,
    progressColor: Color = Color.White,
    strokeWidth: Dp = 5.dp,
    videoUrl: String? = null
) {
    Log.d("checkingThumbnails", "DefaultVideoLoader: ${videoUrl}")

    var thumbnail by remember(videoUrl) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit){
        if (videoUrl!!.isNotEmpty()) {
            thumbnail= getVideoThumbnail(videoUrl)
            Log.d("checkingThumbnails", "extractVideoThumbnail: ${getVideoThumbnail(videoUrl)}")
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = thumbnail,
            contentDescription = "Video thumbnail",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        CircularProgressIndicator(
            modifier = modifier
                .align(alignment = Alignment.Center)
                .size(50.dp),
            color = progressColor,
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round
        )
    }


}


suspend fun getVideoThumbnail(url: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        Log.d("checkingThumbnails", "getVideoThumbnail: trying")

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(url, HashMap()) // Needed for network URL
        val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST) // 1 second
        retriever.release()
        Log.d("checkingThumbnails", "getVideoThumbnail: ${bitmap}")

        bitmap

    } catch (e: Exception) {
        Log.d("checkingThumbnails", "getVideoThumbnail: catching")

        e.printStackTrace()
        null
    }
}