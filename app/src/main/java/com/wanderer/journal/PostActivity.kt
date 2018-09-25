package com.wanderer.journal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_post.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var curPicPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        post_img.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = BitmapFactory.decodeFile(curPicPath)

            post_img.setImageBitmap(imageBitmap)
            Log.d("ActivityResult", "OK")
        }
    }

    private fun dispatchTakePictureIntent(){
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var imgFile: File = createImageFile()
        val imgURI: Uri = FileProvider.getUriForFile(this, "com.example.fileprovider", imgFile)

        if(myIntent.resolveActivity(packageManager) != null){
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI)
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            Log.d("ResolveIntent", "OK")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            curPicPath = absolutePath
            Log.d("PicStoragePath", curPicPath)
        }
    }

}
