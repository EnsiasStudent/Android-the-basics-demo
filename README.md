 # Basic Android App
 **In this project we will see a variety of tools as:** 
 - **Recycle View**
 - **Item Decorator**
 - **Room Database**
 - **Image Conversion**
 - **Google Maps API**
 
## Image Conversion in Base64 Format :
**In this part we will be using two functions and use them in the appropriate place :**
- When I get pictures from the database, I decode them using this function :
```
public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
```
- When I want to store pictures in the database, I code them in Base64 using this function :
```
public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
```


