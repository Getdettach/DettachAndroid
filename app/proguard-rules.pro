# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\vivek_e\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings

# Sugar ORM
# Ensures entities remain un-obfuscated so table and columns are named correctly
-keep class com.wwc.jajing.domain.entity.** { *; }

#-keep class * extends io.realm.RealmObject

# Realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *

-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }

-keep class io.realm.internal.KeepMember
-keep @io.realm.internal.KeepMember class * { @io.realm.internal.KeepMember *; }

-keep class io.realm.RealmCollection
-keep class io.realm.OrderedRealmCollection
-keepclasseswithmembernames class io.realm.** {
    *;
}