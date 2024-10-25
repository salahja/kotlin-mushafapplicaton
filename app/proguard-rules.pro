# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep LeakCanary classes to avoid them from being obfuscated or removed
-keep class leakcanary.** { *; }
-keep class com.ibm.icu.text.Collator { *; }
# LeakCanary relies on weak references, which ProGuard may strip out
-keepclassmembers class * extends java.lang.ref.WeakReference {
    <init>(...);
}

# Keep the class that LeakCanary uses for memory analysis
-keep class com.squareup.leakcanary.** { *; }

# LeakCanary uses the AndroidX Fragment and lifecycle observers, so make sure they are not obfuscated
-keep class androidx.fragment.app.Fragment { *; }
-keep class androidx.lifecycle.* { *; }

# Prevent ProGuard from removing obfuscation for classes that may interact with LeakCanary
-dontwarn leakcanary.**
