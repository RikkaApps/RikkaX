# AppCompat

![Maven Central](https://img.shields.io/maven-central/v/dev.rikka.rikkax.appcompat/appcompat)

Modified version of AndroidX AppCompat. Changes are following.

* Remove `updateStatusGuard` (`updateStatusGuard` draws hardcoded color, white or black, in the area of status bar when ActionMode is shown)
* Do nothing when night mode is `MODE_NIGHT_UNSPECIFIED`
* `SupportMenuInflater` accepts android attribute
* Allow `setSupportActionBar` not only in Activity
* Let AlertDialog#setView(View,int,int,int) use margin
* ~~Remove image resources that never be used on 23+~~ (Not yet implemented for new version)

## Download

```groovy
dependencies {
    implementation "dev.rikka.rikkax.appcompat:appcompat:<version>"
}
```

## Usage

To use this modified AppCompat, the original version of AppCompat needs to be removed.

```groovy
configurations.all {
    exclude group: 'androidx.appcompat', module: 'appcompat'
}
```
