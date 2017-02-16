# Canary for Android
This is the Android sub project of Canary

## Building
The project is built using gradle. To build, it requires the Android SDK and Android NDK.

To rebuild the spotify-api-plusplus library, uncomment 'compile project(':gen-libs')' in 'app/build.gradle'.
To rebuild the curl and openssl libraries, I use https://github.com/leenjewel/openssl_for_ios_and_android.
However, these libraries shouldn't need to be rebuilt unless you're changing the API level, or need a bug
fix or new feature from a later version of either library.