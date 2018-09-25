package com.wanderer.journal

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostDB
import kotlinx.android.synthetic.main.activity_post.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var curPicPath: String = ""
    var time: String = ""
    var desc: String = ""
    var location: String = "abc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val postDB = PostDB.get(this)

        post_img.setOnClickListener {
            dispatchTakePictureIntent()
        }

        cancel_button.setOnClickListener {
            finish()
        }

        save_button.setOnClickListener {
            desc = description.text.toString()
            if (curPicPath.isBlank() || time.isBlank()) {
                Toast.makeText(this, "Image empty. Cannot save", Toast.LENGTH_SHORT).show()
            } else if (location.isBlank()) {
                Toast.makeText(this, "Cannot find location. Please check GPS", Toast.LENGTH_SHORT).show()
            } else if (desc.isBlank()) {
                Toast.makeText(this, "Description empty. Please fill", Toast.LENGTH_SHORT).show()
            } else {
                doAsync {
                    Log.d("Text", Post(time,curPicPath,desc,location).toString())
                    postDB.postDao().insert(Post(time, curPicPath, desc, location))
                    UI {
                        //Toast.makeText(applicationContext, "Save successful", Toast.LENGTH_SHORT).show()
                        Log.d("Successful", "Wonderful")
                        Log.d("YEET", postDB.postDao().getAll().toString())
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //Set datetime for pic - unique
            time = SimpleDateFormat("dd/MM/yyy HH:mm:ss").format(Date())

            //Preview image high resolution
            val imageBitmap = BitmapFactory.decodeFile(curPicPath)
            post_img.setImageBitmap(imageBitmap)
            Log.d("ActivityResult", "OK")
        }
    }

    private fun dispatchTakePictureIntent() {
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var imgFile: File = createImageFile()
        val imgURI: Uri = FileProvider.getUriForFile(this, "com.example.fileprovider", imgFile)

        if (myIntent.resolveActivity(packageManager) != null) {
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI)
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            Log.d("ResolveIntent", "OK")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        //Using datetime to avoid collision
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
