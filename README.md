# JOURNAL
### Description
A dynamic on-the-go journal with the purpose of saving your thoughts and your moments during your journey.
### Features
- Capture and save photos with location
- Fetch current weather information from web service and attach to each entry
- Add journal entries
- View journal entries with grid or list view
- Single entry view provides various further actions (edit, delete, view in gallery...)
- Theme personalization
- Localization (*en* and *vi*)
- Map view
- Passcode login
- Share to social network (under development)
### APIs used
 - **Reverse geocoding** service from [Open Street Map]
 - **Weather** service from [Open Weather Map]
### Screenshots
**Permission screen**
<br/>
<img src="https://user-images.githubusercontent.com/25374992/46723032-8ccda380-cc7f-11e8-92ea-37928d5583fd.png" height="400">

**Grid view**
<br/>
<img src="https://user-images.githubusercontent.com/25374992/46723047-948d4800-cc7f-11e8-95c3-49055139d4b3.png" height="400">

**Map view**
<br/>
<img src="https://user-images.githubusercontent.com/25374992/46723056-9951fc00-cc7f-11e8-83da-c1f8eda6a38c.png" height="400">

**Dark mode**
<br/>
<img src="https://user-images.githubusercontent.com/25374992/46723060-9c4cec80-cc7f-11e8-90ac-029bf260d8ca.png" height="400">

**Single post view**
<br/>
<img src="https://user-images.githubusercontent.com/25374992/46723065-9f47dd00-cc7f-11e8-9020-c66141521ba9.png" height="400">

[More screenshots](https://users.metropolia.fi/~tuanl/Journal_screenshots/)
### Android technical features
 - Fused Location Provider
 - Room SQLite persistent data
 - Shared Preferences (with PreferenceFragment)
 - Map Fragment from Google Map
### Install
 - Clone from github
 - Open with Android Studio (prefereably >= 3.0)
 - **Important:** *Acquire your own key from Google Maps and Open Weather Map first then modify 'google_maps_api.xml' and 'open_weather_api.xml'*
 - Run app in emulator or actual Android device
### Code snippets:
**"Post" entity for Room SQL**
```kotlin
@Entity
data class Post(
    @PrimaryKey
    val time: String,
    val image: String,
    val description: String,
    val weather: String,
    @Embedded
    val location: Location): Comparable<Post> {

    override fun compareTo(other: Post): Int {
        val spd = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss")
        val thisTime = spd.parse(this.time)
        val otherTime = spd.parse(other.time)

        return otherTime.compareTo(thisTime)
    }

    override fun toString(): String = "$time $image $description $location $weather"
}
```
**Request location permission**
``` kotlin
    private fun handlePermissionScreen() {
        //Explain and request permission for Location
        setContentView(R.layout.splash_permission)
        contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in))

        imageButton_accept.setOnClickListener {
            //Request location permission
            if ((ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else startTimeline()
        }
    }
```
### Team members
 - Thanh Tran (congthanhptnk)
 - Tuan Le (ZeusLMT)

[Open Weather Map]: <https://openweathermap.org/current>
[Open Street Map]: <https://wiki.openstreetmap.org/wiki/Nominatim>
