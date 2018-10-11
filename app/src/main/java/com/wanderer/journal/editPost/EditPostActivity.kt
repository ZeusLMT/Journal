package com.wanderer.journal.editPost

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.wanderer.journal.MainActivity
import com.wanderer.journal.R
import com.wanderer.journal.dataStorage.Location
import com.wanderer.journal.dataStorage.Post
import com.wanderer.journal.dataStorage.PostDB
import kotlinx.android.synthetic.main.activity_edit_post.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync

class EditPostActivity : AppCompatActivity(), View.OnClickListener {
    private var imgPath =""
    private var time: String = ""
    private var desc: String = ""
    private var weather: String = ""
    private var location: Location ?= null
    private val postDB = PostDB.get(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        changeTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        time = intent.getStringExtra("timestamp")
        Log.d("editPost", time)
        onGetPost(time)
        edit_save_button.setOnClickListener(this)
        edit_cancel_button.setOnClickListener(this)
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
            R.id.edit_save_button -> {
                if(edit_description.text.toString().isBlank()){
                    Toast.makeText(this, getString(R.string.toast_empty_desc), Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("editSaveOnClick", edit_description.text.toString())
                    val updatedPost = Post(time, imgPath, edit_description.text.toString(), weather, location!!)
                    doAsync {
                        postDB.postDao().update(updatedPost)
                        UI{
                            val intent = Intent(baseContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            R.id.edit_cancel_button -> {
                finish()
            }
        }
    }

    private fun onGetPost(time: String){
        doAsync {
            val curPost = postDB.postDao().getSinglePost(time)
            UI{
                desc = curPost.description
                location = curPost.location
                imgPath = curPost.image
                weather = curPost.weather
                Log.d("OnGetPost", imgPath)

                val imgBitmap = squareCropImg(BitmapFactory.decodeFile(imgPath))
                edit_post_img.setImageBitmap(imgBitmap)
                edit_description.setText(desc, TextView.BufferType.EDITABLE)
            }
        }
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
}
