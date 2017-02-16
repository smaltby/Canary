#include <jni.h>
#include <curl/curl.h>
#include <string>
#include "SpotifyAPI.h"

extern "C" JNIEXPORT jstring JNICALL
Java_me_seanmaltby_canary_MainActivity_getMyDisplayName(JNIEnv *env, jobject thiz, jstring javaAccessToken)
{
    SpotifyAPI api = SpotifyAPI();
    const char *accessToken = env->GetStringUTFChars(javaAccessToken, JNI_FALSE);

    api.setAuthToken(std::string(accessToken));
    User me = *api.GetMe();

    env->ReleaseStringUTFChars(javaAccessToken, accessToken);
    return env->NewStringUTF(me.GetBirthdate().c_str());
}