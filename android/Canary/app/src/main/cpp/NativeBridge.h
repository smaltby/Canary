#ifndef CANARY_NATIVEBRIDGE_H
#define CANARY_NATIVEBRIDGE_H

#include <string>
#include <jni.h>

void NativePlayUri(std::string uri);

void NativePause();

void NativeResume();

void NativeNext();

void NativeToggleShuffle(bool shuffle);

void NativeToggleRepeat(bool repeat);

#endif
