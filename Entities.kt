name: Build Maher Focus APK

on:
  workflow_dispatch:
  push:
    branches: [ "main", "master" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Télécharger le code
        uses: actions/checkout@v4

      - name: Installer Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - name: Installer Android SDK
        uses: android-actions/setup-android@v3

      - name: Installer Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: 8.9

      - name: Compiler l'APK debug
        run: gradle :app:assembleDebug --no-daemon --stacktrace

      - name: Publier l'APK téléchargeable
        uses: actions/upload-artifact@v4
        with:
          name: MaherFocus-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
