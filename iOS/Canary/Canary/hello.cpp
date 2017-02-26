#include "hello.hpp"
#include <iostream>
#include <SpotifyAPI.h>

void Hello::hello_cpp(const std::string& name)
{
    SpotifyAPI api = SpotifyAPI();
    std::cout << api.GetAlbum("0sNOF9WDwhWunNAHPD3Baj")->GetName() << std::endl;
}

