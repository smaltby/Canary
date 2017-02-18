#include <NativeBridge.h>
#include <CommandParser.h>
#include <jni.h>
#include <string>

static JNIEnv *env;

extern "C" JNIEXPORT void JNICALL
Java_me_seanmaltby_canary_MainActivity_initEnv(JNIEnv *newEnv, jobject thiz)
{
    env = newEnv;
}

extern "C" JNIEXPORT void JNICALL
Java_me_seanmaltby_canary_MainActivity_parse(JNIEnv *env, jobject thiz, jstring javaCommand, jstring javaAccessToken)
{
    const char * command = env->GetStringUTFChars(javaCommand, JNI_FALSE);
    const char * accessToken = env->GetStringUTFChars(javaAccessToken, JNI_FALSE);

    parse(std::string(command), std::string(accessToken));

    env->ReleaseStringUTFChars(javaCommand, command);
    env->ReleaseStringUTFChars(javaAccessToken, accessToken);
}

void playUri(std::string uri)
{
    jclass clazz = env->FindClass("me/seanmaltby/canary/MainActivity");
    jmethodID playUriId = env->GetStaticMethodID(clazz, "playUri", "(Ljava/lang/String;)V");
    env->CallStaticVoidMethod(clazz, playUriId, env->NewStringUTF(uri.c_str()));
}