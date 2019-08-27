package ss.wallpad

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.UnitModelLoader
import com.bumptech.glide.module.LibraryGlideModule
import ss.wallpad.EmptyInputStream.read
import java.io.InputStream

/**
 * Replace [GlideUrl] model loading by ignoring the URL and returning [EmptyInputStream] for tests.
 */
class WallPadLibraryGlideModule : LibraryGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            object : ModelLoaderFactory<GlideUrl, InputStream> {
                override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
                    return object : ModelLoader<GlideUrl, InputStream> {
                        override fun buildLoadData(
                            model: GlideUrl,
                            width: Int,
                            height: Int,
                            options: Options
                        ): ModelLoader.LoadData<InputStream>? {
                            return UnitModelLoader.getInstance<InputStream>()
                                .buildLoadData(EmptyInputStream, width, height, options)
                        }

                        override fun handles(model: GlideUrl) = true
                    }
                }

                override fun teardown() {}
            })
    }
}

/**
 * Input stream where [read] immediately returns -1 to indicate the end of the stream.
 */
object EmptyInputStream : InputStream() {
    override fun read(): Int = -1
}
