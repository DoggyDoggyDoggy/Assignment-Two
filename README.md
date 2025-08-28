# SimpleGallery — A minimal Android photo gallery (no third-party libs)

A small sample Android gallery app that shows all photos on the device in a scrollable grid and opens a single photo in a separate view with pinch-to-zoom.  
Built with Jetpack Compose, using only Android SDK APIs (`MediaStore`, `BitmapFactory`, etc.) — **no Coil / Glide / Picasso / Fresco / PhotoPicker**.

***

### Key features

- Grid of device photos implemented with `LazyVerticalGrid`.  
- Thumbnails loaded on background coroutines (`Dispatchers.IO`) via `LaunchedEffect`.  
- Simple in-memory thumbnail `LruCache` to reduce re-decoding.  
- Single-photo activity with a higher-resolution image and pinch-to-zoom (Compose gestures / `transformable`).  
- Proper orientation handling (rotation) and safe loading for large images (uses `inSampleSize`).  
- Re-queries `MediaStore` on resume so newly added/removed photos are reflected.
