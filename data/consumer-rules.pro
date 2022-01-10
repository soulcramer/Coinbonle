-keep class app.coinbonle.data.local.** { *; }
-keep class app.coinbonle.data.remote.** { *; }

# Retrofit temp fixes for full mode
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
