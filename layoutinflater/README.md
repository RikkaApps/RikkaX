# LayoutInflater

![Maven Central](https://img.shields.io/maven-central/v/dev.rikka.rikkax.layoutinflater/layoutinflater)

Adding custom attributes for views usually requires extending View classes. It is highly invasive and hard to extend.

To solve the problem, this library provides `LayoutInflaterFactory` class. It implements `android.view.LayoutInflater.Factory2` interface which is called when views are created. So that we can get in touch with attributes from XML and do our works.

## Usage

Call `getLayoutInflater().setFactory2(new LayoutInflaterFactory())` in `Activity#onCreate`. If you are using AppCompat, use the constructor `LayoutInflaterFactory(AppCompatDelegate)`.

Extend this class or use `LayoutInflaterFactory#addOnViewCreatedListener` to add your works.

Example of using Insets library:

```java
getLayoutInflater().factory2 = new LayoutInflaterFactory(getDelegate())
    .addOnViewCreatedListener(WindowInsetsHelper.LISTENER);
```
