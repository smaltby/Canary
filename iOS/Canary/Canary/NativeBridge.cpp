#include "NativeBridge.h"
#include <iostream>

void NativePlayUri(std::string uri)
{
    std::cout << "PLAY URI: " << uri << std::endl;
}

void NativePause()
{
    std::cout << "PAUSE" << std::endl;
}

void NativeResume()
{
    std::cout << "RESUME" << std::endl;
}

void NativeNext()
{
    std::cout << "NEXT" << std::endl;
}

void NativeToggleShuffle(bool shuffle)
{
    std::cout << "SHUFFLE" << std::endl;
}

void NativeToggleRepeat(bool repeat)
{
    std::cout << "REPEAT" << std::endl;
}
