package com.csu_itc303_team1.adhdtaskmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class ADHDTaskManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_splash)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.your_video_file_name)
        videoView.setVideoURI(uri)

        videoView.setOnCompletionListener {
            // Start MainActivity when video finishes
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Close this activity
        }

        videoView.start()
    }
}