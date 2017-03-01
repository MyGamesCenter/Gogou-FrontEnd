# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/grace/Android/Sdk/tools/proguard/proguard-android.txt
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
-target 1.6
-dontobfuscate
-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dump ../bin/class_files.txt
-printseeds ../bin/seeds.txt
-printusage ../bin/unused.txt
-printmapping ../bin/mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses
-keepattributes Signature
-keepattributes *Annotation*

-keepclasseswithmembers class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# There's no way to keep all @Observes methods, so use the On*Event convention to identify event handlers
-keepclassmembers class * {
    void *(**On*Event);
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keepclassmembers class cn.trinea.android.** { *; }

### Jackson SERIALIZER SETTINGS
-keepclassmembers,allowobfuscation class * {
    @org.codehaus.jackson.annotate.* <fields>;
    @org.codehaus.jackson.annotate.* <init>(...);
}
### Simple XML SERIALIZER SETTINGS
-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}

-keepclassmembers public class org.springframework {
    public *;
}

-dontwarn org.eclipse.jdt.annotation.**
-dontwarn cn.trinea.android.**
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.persistence.**
-dontwarn com.alipay.**
-dontwarn android.support.v7.**
-dontwarn android.support.v4.**
-dontwarn roboguice.**
-dontwarn com.davemorrissey.**
-dontwarn org.roboguice.**
-dontwarn com.octo.android.robospice.**


-keep class **$Properties
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep class com.google.inject.**
-keepclassmembers class * {
    @com.google.inject.Inject <init>(...);
    @com.google.inject.Inject <fields>;
    @javax.annotation.Nullable <fields>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep public class roboguice.**
-keep class roboguice.** { *; }
-keep class org.roboguice.** { *; }
-keep class com.ultrapower.** {*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent{*;}
-keep class cn.trinea.android.** { *; }
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class info.gogou.gogou.AnnotationDatabaseImpl
-keep class info.gogou.gogou.model.** { *; }