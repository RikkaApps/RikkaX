-keep class rikka.annotation.KeepAllowObfuscation

-keep,allowobfuscation @rikka.annotation.KeepAllowObfuscation class * {*;}

-keepclasseswithmembers,allowobfuscation class * {
    @rikka.annotation.KeepAllowObfuscation <methods>;
}

-keepclasseswithmembers,allowobfuscation class * {
    @rikka.annotation.KeepAllowObfuscation <fields>;
}

-keepclasseswithmembers,allowobfuscation class * {
    @rikka.annotation.KeepAllowObfuscation <init>(...);
}