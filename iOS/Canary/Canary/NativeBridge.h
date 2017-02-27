#ifndef NativeBridge_C_Interface_h
#define NativeBridge_C_Interface_h

void NativePlayUri(const char* uri);

void NativePause();

void NativeResume();

void NativeNext();

void NativeToggleShuffle(bool shuffle);

void NativeToggleRepeat(bool repeat);

#endif
