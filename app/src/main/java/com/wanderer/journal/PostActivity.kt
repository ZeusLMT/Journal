package com.wanderer.journal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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

class PostActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private var curPicPath: String = ""
    private var time: String = ""
    private var desc: String = ""
    private var location: String = "abc"
    private val postDB = PostDB.get(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        changeTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }

        post_img.setOnClickListener(this)
        cancel_button.setOnClickListener(this)
        save_button.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //Set datetime for pic - unique
            time = SimpleDateFormat("dd/MM/yyy HH:mm:ss").format(Date())

            //Preview image high resolution
            val imageBitmap = squareCropImg(BitmapFactory.decodeFile(curPicPath))
            post_img.setImageBitmap(imageBitmap)
            Log.d("ActivityResult", "OK")
        }
    }

    private fun dispatchTakePictureIntent() {
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imgFile: File = createImageFile()
        val imgURI: Uri = FileProvider.getUriForFile(this, "com.example.fileprovider", imgFile)

        if (myIntent.resolveActivity(packageManager) != null) {
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI)
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            Log.d("ResolveIntent", "OK")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d("wee", "wee")
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

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun squareCropImg(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH
        return Bitmap.createBitmap(original, cropW, cropH, newWidth, newHeight)
    }

    private fun changeTheme() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        //Change app theme accordingly to user settings
        when (sp.getString(getString(R.string.prefs_key_app_theme), "LIGHT")) {
            "LIGHT" -> setTheme(R.style.AppTheme_PastelLight)
            "DARK" -> setTheme(R.style.AppTheme_PastelDark)
        }
    }

    override fun onClick(p0: View?){
        when(p0?.id){
            R.id.post_img -> {
                if(isExternalStorageWritable()){
                    dispatchTakePictureIntent()
                } else{
                    Toast.makeText(this, "Storage space not available", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cancel_button -> {
                finish()
            }
            R.id.save_button -> {
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
                        UI{
                            finish()
                        }
                    }
                }
            }
        }
    }
}
