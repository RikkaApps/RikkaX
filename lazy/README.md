# Lazy

![Maven Central](https://img.shields.io/maven-central/v/dev.rikka.rikkax.lazy/lazy)

Lazy initialization helper.

## Download

```groovy
dependencies {
    implementation "dev.rikka.rikkax.lazy:lazy:<version>"
}
```

### Dependencies included (If you have concerns about file size)

- `androidx.annotation:annotation`

## Introduction

The purpose library is to use Kotlin-like Lazy in Java only projects/libraries. The design and internal implementation is close to Kotlin Lazy.

If your are already using Kotlin, you don't need to use this library.

## Usage

This library has a `rikka.lazy.Lazy` interface and all implementations are its subclass.

There two implementation, `rikka.lazy.SynchronizedLazy` (thread-safe version using locks inside) and `rikka.lazy.UnsafeLazy` (no thread-safe version).

```java
// Create a UnsafeLazy instance
Lazy<Object> lazy = new UnsafeLazy<>(() -> new Object());

// Get the value
Object value = lazy.get();

// Check if the value is initialized
lazy.isInitialized();
```

```java
// Create a SynchronizedLazy instance
Lazy<Object> lazy = new SynchronizedLazy<>(() -> new Object());

// Create a SynchronizedLazy instance with a custom lock
Object lock = new Object();
Lazy<Object> lazy = new SynchronizedLazy<>(() -> new Object(), lock);
```
