#include <CommandParser.h>
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_me_seanmaltby_canary_MainActivity_parse(JNIEnv *env, jobject thiz, jstring javaCommand, jstring javaAccessToken)
{
    const char * command = env->GetStringUTFChars(javaCommand, JNI_FALSE);
    const char * accessToken = env->GetStringUTFChars(javaAccessToken, JNI_FALSE);

    CommandParser parser;

    std::string result = parser.parse(std::string(command), std::string(accessToken));

    env->ReleaseStringUTFChars(javaCommand, command);
    env->ReleaseStringUTFChars(javaAccessToken, accessToken);

    return env->NewStringUTF(result.c_str());
}