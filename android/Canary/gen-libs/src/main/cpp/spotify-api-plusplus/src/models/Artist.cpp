#include "Artist.h"

Artist::Artist(nlohmann::json artistJson) : ArtistSimple::ArtistSimple(artistJson)
{
    followers = std::shared_ptr<Followers>(new Followers(artistJson["followers"]));
    for(std::string genre : artistJson["genres"])
        genres.push_back(genre);
    for(nlohmann::json json : artistJson["images"])
        images.push_back(std::shared_ptr<Image>(new Image(json)));
    popularity = artistJson["popularity"];
}

std::shared_ptr<Followers> Artist::GetFollowers()
{
    return followers;
}

std::vector<std::string> Artist::GetGenres()
{
    return genres;
}

std::vector<std::shared_ptr<Image>> Artist::GetImages()
{
    return images;
}

int Artist::GetPopularity()
{
    return popularity;
}
