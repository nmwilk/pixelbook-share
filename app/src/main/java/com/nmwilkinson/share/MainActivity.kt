package com.nmwilkinson.share

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preventSystemBarAppearingForPopups()

        setContentView(R.layout.activity_main)

        shareButton.setOnClickListener { share() }
    }

    private fun share() {
        val cachePath = File(cacheDir, "export")
        cachePath.mkdirs()
        val filePath = "$cachePath/sharedfile"
        val stream = FileOutputStream(filePath)
        Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, File(filePath))

        startActivity(shareImageIntent("Share", contentUri))
    }

    private fun AppCompatActivity.preventSystemBarAppearingForPopups() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun shareImageIntent(message: String, fileUri: Uri): Intent {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "image/*"
//        sharingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, fileUri)
        return Intent.createChooser(sharingIntent, message)
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
