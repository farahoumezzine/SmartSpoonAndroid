SmartSpoon

Overview: SmartSpoon is an Android application that combines traditional recipes with AI technology to help users in the kitchen. The app features a collection of classic recipes, an AI-powered recipe generator, and voice command support.

Features: 
1. Recipe Collection : -Detailed ingredients and instructions
-Recipe filtering (Vegetarian, Spicy)
-Recipe ratings and cooking times
-Visual recipe cards with images

2. AI Recipe Generator : -Input available ingredients and generate custom recipes 
-Real-time recipe generation


3. Voice Commands : -Add ingredients hands-free

4. Camera Integration : -Scan text ingredients using device camera

Installation : Configure requirements:
Minimum SDK: API 24 (Android 7.0)
Target SDK: API 34 (Android 14)
Kotlin version: 1.8.0+

Permissions Required
<!-- Add to AndroidManifest.xml -->

<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />

<!-- Add to build.gradle -->

dependencies {
    // CameraX
    def camerax_version = "1.2.0"
    implementation "androidx.camera:camera-core:$camerax_version"
    implementation "androidx.camera:camera-camera2:$camerax_version"
    implementation "androidx.camera:camera-lifecycle:$camerax_version"
    implementation "androidx.camera:camera-view:$camerax_version"

    // ML Kit for barcode scanning
    implementation 'com.google.mlkit:barcode-scanning:17.0.2'
    
    // ML Kit for text recognition
    implementation 'com.google.mlkit:text-recognition:16.0.0'
}
