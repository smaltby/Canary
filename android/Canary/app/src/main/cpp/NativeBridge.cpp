#include <NativeBridge.h>
#include <CommandParser.h>
#include <jni.h>
#include <string>

static JNIEnv *lastEnv;

extern "C" JNIEXPORT void JNICALL
Java_me_seanmaltby_canary_MainActivity_parse(JNIEnv *env, jobject thiz, jstring javaCommand, jstring javaAccessToken)
{
    lastEnv = env;

    const char * command = env->GetStringUTFChars(javaCommand, JNI_FALSE);
    const char * accessToken = env->GetStringUTFChars(javaAccessToken, JNI_FALSE);

    CommandParser parser;

    parser.parse(std::string(command), std::string(accessToken));

    env->ReleaseStringUTFChars(javaCommand, command);
    env->ReleaseStringUTFChars(javaAccessToken, accessToken);
}

void NativePlayUri(std::string uri)
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID playUriId = lastEnv->GetStaticMethodID(clazz, "playUri", "(Ljava/lang/String;)V");
    lastEnv->CallStaticVoidMethod(clazz, playUriId, lastEnv->NewStringUTF(uri.c_str()));
}

void NativePause()
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID pauseId = lastEnv->GetStaticMethodID(clazz, "pause", "()V");
    lastEnv->CallStaticVoidMethod(clazz, pauseId);
}

void NativeResume()
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID resumeId = lastEnv->GetStaticMethodID(clazz, "resume", "()V");
    lastEnv->CallStaticVoidMethod(clazz, resumeId);
}

void NativeNext()
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID nextId = lastEnv->GetStaticMethodID(clazz, "next", "()V");
    lastEnv->CallStaticVoidMethod(clazz, nextId);
}

void NativeToggleShuffle(bool shuffle)
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID toggleShuffleId = lastEnv->GetStaticMethodID(clazz, "toggleShuffle", "(Z)V");
    lastEnv->CallStaticVoidMethod(clazz, toggleShuffleId, shuffle);
}

void NativeToggleRepeat(bool repeat)
{
    jclass clazz = lastEnv->FindClass("me/seanmaltby/canary/NativeBridge");
    jmethodID toggleRepeatId = lastEnv->GetStaticMethodID(clazz, "toggleRepeat", "(Z)V");
    lastEnv->CallStaticVoidMethod(clazz, toggleRepeatId, repeat);
}