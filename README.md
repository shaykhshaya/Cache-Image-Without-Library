# Image Display and Cache


A Cache Image app capable of downloading images and displaying them in an imageView without relying on any third-party libraries. Additionally, a separate module named 'cacheimage' has been added to efficiently handle image loading within the application.


https://github.com/shaykhshaya/Cache-Image-Without-Library/assets/119520622/b50edcf5-9d82-4ac6-827c-51c90e475132


## Usage:

```kotlin
            CacheImageBuilder
                .with(context)
                .url(//provide image url)
                .callback(object : CacheImageBuilder.Callback {
                    override fun onLoading() {
                        //perform action once loading starts
                    }
                    override fun onLoaded() {
                        //perform action after loading
                    }
                })
                .into(//provide view in which image to be loaded)
        
```







