#ifndef NativeBridge_h
#define NativeBridge_h

#include <string>

void NativePlayUri(std::string uri);

void NativePause();

void NativeResume();

void NativeNext();

void NativeToggleShuffle(bool shuffle);

void NativeToggleRepeat(bool repeat);

#endif
