# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

-keepnames class * extends androidx.startup.Initializer


# For Google drive
-keep class com.google.api.services.drive.** {*;}
-keep class com.google.api.client.** {*;}
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keep class com.google.** { *;}
-keep interface com.google.** { *;}
-dontwarn com.google.**

# Needed by google-http-client-android when linking against an older platform version
-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version
-dontwarn com.google.api.client.googleapis.extensions.android.**

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.piappstudio.pimodel.** {*;}

-keep class com.crashlytics.** { *;}
-keep public class * extends android.app.Activity