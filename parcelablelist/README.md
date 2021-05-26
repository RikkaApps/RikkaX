# ParcelableList

![Maven Central](https://img.shields.io/maven-central/v/dev.rikka.rikkax.parcelablelist/parcelablelist)

Binder has total 1MB buffer size across the process (64KB for each Binder thread).
When transferring a large list of Parcelable objects (e.g., `getInstalledPackages` with flags like `GET_ACTIVITIES`), 
it's easy to exceed this limit (`TransactionTooLargeException`). Therefore, the framework has created `android.content.pm.ParceledListSlice` which split the list into multiple transactions.

This library has `ParcelableListSlice` and `StringListSlice` class which is similar to the framework classes.

There is also a base class, `BaseParcelableListSlice`, for extension to transfer non-Parcelable objects. 

## Download

```groovy
dependencies {
    implementation "dev.rikka.rikkax.parcelablelist:parcelablelist:1.0.0"
}
```
