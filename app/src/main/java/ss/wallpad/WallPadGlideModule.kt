package ss.wallpad

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.module.LibraryGlideModule

/**
 * Custom Glide app and library modules. We don't need to do any
 * customizations here but we do need to set them up in the main
 * application and sources to be overridable in instrumentation tests.
 */
@GlideModule
class WallPadGlideModule : AppGlideModule()

@GlideModule
class WallPadLibraryGlideModule : LibraryGlideModule()
