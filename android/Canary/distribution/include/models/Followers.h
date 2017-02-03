#ifndef SPOTIFY_PLUSPLUS_FOLLOWERS_H
#define SPOTIFY_PLUSPLUS_FOLLOWERS_H

#include <string>
#include "utils/json.h"

class Followers
{
public:
    Followers(nlohmann::json followersJson);

    std::string GetHref();
    int GetTotal();

private:
    std::string href;
    int total;
};


#endif
