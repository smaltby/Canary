/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <jni.h>
#include <curl/curl.h>
#include <string>
#include "SpotifyAPI.h"

static size_t WriteCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
    ((std::string*)userp)->append((char*)contents, size * nmemb);
    return size * nmemb;
}

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