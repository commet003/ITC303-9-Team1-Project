import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
//import com.google.android.exoplayer2.DefaultDataSourceFactory
//import com.google.android.exoplayer2.MediaItem
//import com.google.android.exoplayer2.SimpleExoPlayer
//import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(videoUri: Uri) {
//    val context = LocalContext.current
//    val exoPlayer = SimpleExoPlayer.Builder(context).build()
//    val dataSourceFactory = DefaultDataSourceFactory(context, "AppName")
//    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//        .createMediaSource(MediaItem.fromUri(videoUri))
//
//    // DisposableEffect and AndroidView setup here...
//
//    AndroidView(
//        factory = { context ->
//            PlayerView(context).apply {
//                player = exoPlayer
//            }
//        },
//        update = { view ->
//            view.player = exoPlayer
//        },
//    ) {
//        exoPlayer.prepare(mediaSource)
//        exoPlayer.playWhenReady = true
//    }
}
