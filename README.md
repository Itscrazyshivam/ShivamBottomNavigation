# ShivamBottomNavigation

**A modern, customizable and animated bottom navigation library for Android, built with Java.**

Create beautiful bottom navigation experiences with smooth animations, expandable selected items, customizable indicators, and flexible styling options.

[![JitPack](https://jitpack.io/v/Itscrazyshivam/ShivamBottomNavigation2.svg)](https://jitpack.io/#Itscrazyshivam/ShivamBottomNavigation2)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)
![Language](https://img.shields.io/badge/Language-Java-orange)
![Min SDK](https://img.shields.io/badge/Min%20SDK-21-blue)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

---

## Features

- **Smooth Animations** – Animated transitions when selecting navigation items.
- **Expandable Selection** – Selected navigation items expand automatically.
- **Animated Indicator** – A customizable indicator follows the selected item.
- **Menu Resource Support** – Define navigation items using Android menu XML.
- **Custom Icons** – Configure icon size, default color, and selected color.
- **Custom Typography** – Customize text size, visibility, and colors.
- **Custom Backgrounds** – Set navigation bar and indicator backgrounds.
- **Flexible Spacing** – Configure indicator margins and internal padding.
- **Runtime Customization** – Change navigation appearance and selection programmatically.
- **RTL Support** – Supports right-to-left layouts.

## Preview



## Installation

### Step 1. Add the JitPack repository

In your project's `settings.gradle` file:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2. Add the dependency

For the current GitHub repository `ShivamBottomNavigation`, use the library module coordinate:

```groovy
dependencies {
    implementation 'com.github.Itscrazyshivam:ShivamBottomNavigation:1.0.1'
}
```



## Quick Start

### 1. Create a menu resource

Create `res/menu/bottom_navigation_menu.xml`:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/nav_home"
        android:icon="@drawable/ic_home"
        android:title="Home" />

    <item
        android:id="@+id/nav_search"
        android:icon="@drawable/ic_search"
        android:title="Search" />

    <item
        android:id="@+id/nav_favorites"
        android:icon="@drawable/ic_favorite"
        android:title="Favorites" />

    <item
        android:id="@+id/nav_profile"
        android:icon="@drawable/ic_profile"
        android:title="Profile" />

</menu>
```

### 2. Add the navigation view to your layout

```xml
<com.shivam.bottomnavigation.ShivamBottomNavigation
    android:id="@+id/bottomNavigation"
    android:layout_width="match_parent"
    android:layout_height="64dp"

    app:cbnMenu="@menu/bottom_navigation_menu"

    app:cbnAnimationEnabled="true"

    app:cbnIconSize="24dp"
    app:cbnIconColor="#808080"
    app:cbnSelectedIconColor="#FFFFFF"

    app:cbnTextSize="12sp"
    app:cbnTextColor="#808080"
    app:cbnSelectedTextColor="#FFFFFF"
    app:cbnTextVisibility="selected"

    app:cbnBarColor="#191919"
    app:cbnBarRadius="32dp"

    app:cbnIndicatorColor="#E83E8C"
    app:cbnIndicatorRadius="100dp"

    app:cbnIndicatorMarginStart="4dp"
    app:cbnIndicatorMarginEnd="4dp"
    app:cbnIndicatorMarginTop="4dp"
    app:cbnIndicatorMarginBottom="4dp" />
```

Add the following namespace to your root layout:

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### 3. Handle navigation selection in Java

```java
ShivamBottomNavigation bottomNavigation =
        findViewById(R.id.bottomNavigation);

bottomNavigation.setOnItemSelectedListener(itemId -> {

    if (itemId == R.id.nav_home) {

        // Navigate to Home

    } else if (itemId == R.id.nav_search) {

        // Navigate to Search

    } else if (itemId == R.id.nav_favorites) {

        // Navigate to Favorites

    } else if (itemId == R.id.nav_profile) {

        // Navigate to Profile

    }

});
```

## Customization

### Text Visibility

Control how navigation titles are displayed.

| Value | Description |
|---|---|
| `none` | Hide all navigation titles |
| `selected` | Display only the selected item's title |
| `all` | Display all navigation titles |

### Available XML Attributes

| Attribute | Description |
|---|---|
| `cbnMenu` | Navigation menu resource |
| `cbnIconSize` | Navigation icon size |
| `cbnIconColor` | Default icon color |
| `cbnSelectedIconColor` | Selected icon color |
| `cbnTextSize` | Navigation text size |
| `cbnTextColor` | Default text color |
| `cbnSelectedTextColor` | Selected text color |
| `cbnTextVisibility` | Navigation title visibility |
| `cbnBarColor` | Navigation bar background color |
| `cbnBarRadius` | Navigation bar corner radius |
| `cbnBarBackground` | Custom navigation bar drawable |
| `cbnIndicatorColor` | Indicator color |
| `cbnIndicatorRadius` | Indicator corner radius |
| `cbnIndicatorBackground` | Custom indicator drawable |
| `cbnAnimationEnabled` | Enable or disable animations |
| `cbnIndicatorMarginStart` | Indicator start margin |
| `cbnIndicatorMarginTop` | Indicator top margin |
| `cbnIndicatorMarginEnd` | Indicator end margin |
| `cbnIndicatorMarginBottom` | Indicator bottom margin |
| `cbnIndicatorPaddingStart` | Indicator start padding |
| `cbnIndicatorPaddingTop` | Indicator top padding |
| `cbnIndicatorPaddingEnd` | Indicator end padding |
| `cbnIndicatorPaddingBottom` | Indicator bottom padding |

## Java API Reference

### Navigation Selection

```java
// Select an item by its menu ID
bottomNavigation.setSelectedItemId(R.id.nav_home);

// Get selected item ID
int selectedId = bottomNavigation.getSelectedItemId();

// Get selected item position
int position = bottomNavigation.getSelectedItemPosition();

// Select by position
bottomNavigation.setSelectedItemPosition(1);

// Get navigation item count
int count = bottomNavigation.getItemCount();
```

### Animation

```java
bottomNavigation.setAnimationEnabled(true);
```

### Icon Customization

```java
bottomNavigation.setIconSize(24);

bottomNavigation.setIconColor(Color.GRAY);

bottomNavigation.setSelectedIconColor(Color.WHITE);
```

### Text Customization

```java
// 0 = None, 1 = Selected, 2 = All
bottomNavigation.setTextVisibility(1);

bottomNavigation.setTextColor(Color.GRAY);

bottomNavigation.setSelectedTextColor(Color.WHITE);
```

### Background Customization

```java
bottomNavigation.setBarColor(Color.BLACK);

bottomNavigation.setBarBackground(drawable);

bottomNavigation.setIndicatorColor(Color.MAGENTA);

bottomNavigation.setIndicatorBackground(drawable);
```

### Update Navigation Menu

```java
bottomNavigation.setMenu(R.menu.bottom_navigation_menu);
```

**Note:** `getMenu()` returns a newly inflated menu instance. Modifying that instance does not automatically update the rendered navigation items. Use `setMenu()` to rebuild the navigation view.

## Requirements

| Requirement | Version |
|---|---|
| Android | 5.0 (API 21) or higher |
| Language | Java |
| Android Gradle Plugin | 8.7.3 |
| JDK | 17 or higher |
| Compile SDK | 35 or compatible |

## Sample Application

This repository includes a sample Android application demonstrating:

- XML-based navigation configuration.
- Navigation item selection.
- Indicator animations.
- Custom colors and backgrounds.
- Runtime customization.

## Building from Source

Clone the repository:

```bash
git clone https://github.com/Itscrazyshivam/ShivamBottomNavigation2.git
```

Open the project in Android Studio and sync Gradle.

Build the library:

```bash
gradle :shivambottomnavigation:assembleRelease
```

The generated AAR file will be available at:

```text
shivambottomnavigation/build/outputs/aar/
```

## Versioning and Releases

This project follows semantic versioning.

- `1.0.0` – Initial stable release.
- `1.0.1` – Bug fixes.
- `1.1.0` – New backward-compatible features.
- `2.0.0` – Major changes or breaking API updates.

## Contributing

Contributions, bug reports, feature requests, and suggestions are welcome.

To contribute:

1. Fork this repository.
2. Create a feature branch.
3. Implement your changes.
4. Submit a pull request.

Please include clear descriptions and screenshots or GIFs when submitting UI-related improvements.

## License

Copyright © 2026 Shivam Raj.

This project is licensed under the **Apache License 2.0**.

See the [LICENSE](LICENSE) file for details.

---

**Developed by Shivam Raj**

GitHub: [@Itscrazyshivam](https://github.com/Itscrazyshivam)

