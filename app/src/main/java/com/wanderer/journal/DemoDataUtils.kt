package com.wanderer.journal

import android.content.Context
import android.os.Environment
import com.wanderer.journal.dataStorage.Location
import com.wanderer.journal.dataStorage.Post
import com.wanderer.journal.dataStorage.PostDB
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DemoDataUtils(private val appContext: Context) {
    private val postDB: PostDB = PostDB.get(appContext)

    fun setupDemoData() {
        saveDemoImageToStorage(R.raw.athens, "athens")
        saveDemoImageToStorage(R.raw.hcmc, "hcmc")
        saveDemoImageToStorage(R.raw.kannelmaki, "kannelmaki")
        saveDemoImageToStorage(R.raw.london, "london")
        saveDemoImageToStorage(R.raw.paris, "paris")
        saveDemoImageToStorage(R.raw.phu_quoc, "phu_quoc")
        saveDemoImageToStorage(R.raw.santorini, "santorini")
        saveDemoImageToStorage(R.raw.suomenlinna, "suomenlinna")
        saveDemoImageToStorage(R.raw.tokyo, "tokyo")
        saveDemoImageToStorage(R.raw.vatican, "vatican")

        addDemosToDatabase()

    }

    private fun saveDemoImageToStorage(resourceId: Int, name: String) {
        //val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        val file = File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$name.jpg")

        try {
            val inputStream = appContext.resources.openRawResource(resourceId)
            val outputStream = FileOutputStream(file)
            val buff = ByteArray(1024)
            var read = inputStream.read(buff)
            try {
                while (read > 0) {
                    outputStream.write(buff, 0, read)
                    read = inputStream.read(buff)
                }
            } finally {
                inputStream.close()
                outputStream.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun addDemosToDatabase() {
        val generalPath = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).path

        val athensLocation = Location(0, "15/06/2018 - 09:31:00", "37.975556", "23.721111", "Thiseio", "Athens", "Greece")
        val athensPost = Post("15/06/2018 - 10:31:00", "$generalPath/athens.jpg", "A day in Athens", "28${MainActivity.DEGREE}C - Sunny", athensLocation)
        postDB.postDao().insert(athensPost)

        val hcmcLocation = Location(0, "08/01/2018 - 21:00:00", "10.775556", "106.685833", "District 3", "Ho Chi Minh city", "Vietnam")
        val hcmcPost = Post("08/01/2018 - 21:00:00", "$generalPath/hcmc.jpg", "Dinner in Ho Chi Minh city", "29${MainActivity.DEGREE}C - Cloudy", hcmcLocation)
        postDB.postDao().insert(hcmcPost)

        val kannelmakiLocation = Location(0, "08/06/2018 - 15:08:00", "60.236667", "24.890000", "Kannelmäki", "Helsinki", "Finland")
        val kannelmakiPost = Post("08/06/2018 - 15:08:00", "$generalPath/kannelmaki.jpg", "A stall selling berries from local farms", "21${MainActivity.DEGREE}C - Partly Cloudy", kannelmakiLocation)
        postDB.postDao().insert(kannelmakiPost)

        val londonLocation = Location(0, "27/07/2017 - 13:05:00", "51.517500", "0.155000", "Rylands Estate", "London", "United Kingdom")
        val londonPost = Post("27/07/2017 - 13:05:00", "$generalPath/london.jpg", "A church on London street", "16${MainActivity.DEGREE}C - Drizzling rain", londonLocation)
        postDB.postDao().insert(londonPost)

        val parisLocation = Location(0, "24/12/2016 - 10:27:22", "48.858393", "2.294479", "Gros-Caillou", "Paris", "France")
        val parisPost = Post("24/12/2016 - 10:27:22", "$generalPath/paris.jpg", "The Eiffel Tower looks magnificent, even more than I expected", "13${MainActivity.DEGREE}C - Heavily cloudy", parisLocation)
        postDB.postDao().insert(parisPost)

        val phuQuocLocation = Location(0, "15/01/2018 - 10:49:00", "10.33027", "103.851389", "Duong Dong", "Phu Quoc", "Vietnam")
        val phuQuocPost = Post("15/01/2018 - 10:49:00", "$generalPath/phu_quoc.jpg", "A single red flower blooms on the greenest background", "33${MainActivity.DEGREE}C - Sunny", phuQuocLocation)
        postDB.postDao().insert(phuQuocPost)

        val santoriniLocation = Location(0, "15/06/2018 - 14:47:12", "36.461389", "25.376111", "Oia", "Oia Municipal Unit", "Greece")
        val santoriniPost = Post("15/06/2018 - 15:47:12", "$generalPath/santorini.jpg", "The infamous spot that appears on every leaflet of Santorini", "26${MainActivity.DEGREE}C - Clear sky", santoriniLocation)
        postDB.postDao().insert(santoriniPost)

        val suomenlinnaLocation = Location(0, "15/06/2017 - 16:10:42", "60.143056", "24.980833", "Suomenlinna", "Helsinki", "Finland")
        val suomenlinnaPost = Post("15/06/2017 - 16:10:42", "$generalPath/suomenlinna.jpg", "What to do in front of such a breath-taking view?!", "26${MainActivity.DEGREE}C - Clear", suomenlinnaLocation)
        postDB.postDao().insert(suomenlinnaPost)

        val tokyoLocation = Location(0, "22/02/2018 - 13:44:37", "35.660000", "139.699722", "Shibuya", "Tokyo", "Japan")
        val tokyoPost = Post("22/02/2018 - 13:44:37", "$generalPath/tokyo.jpg", "The most crowded cross-walk in the busiest district of Tokyo", "16${MainActivity.DEGREE}C - Light rain", tokyoLocation)
        postDB.postDao().insert(tokyoPost)

        val vaticanLocation = Location(0, "24/12/2016 - 15:58:22", "41.901808", "12.455850", "Piazza Retta", "Vatican City", "Vatican City")
        val vaticanPost = Post("24/12/2016 - 15:58:22", "$generalPath/vatican.jpg", "The Pontifical Swiss Guard guarding in Vatican City", "14${MainActivity.DEGREE}C - Partly cloudy", vaticanLocation)
        postDB.postDao().insert(vaticanPost)
    }
}