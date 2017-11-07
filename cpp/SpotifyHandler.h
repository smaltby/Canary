#ifndef CANARY_SPOTIFYHANDLER_H
#define CANARY_SPOTIFYHANDLER_H

#include <string>
#include <spotify-api-plusplus/SpotifyAPI.h>

class SpotifyHandler
{
public:
    SpotifyHandler(std::string authToken);

    std::string playTrackFromBy(std::string song, std::string album, std::string artist);

    std::string playTrackFrom(std::string song, std::string album);

    std::string playTrackBy(std::string song, std::string artist);

    std::string playTrack(std::string song);

    std::string playAlbumBy(std::string album, std::string artist, bool shuffle);

    std::string playAlbum(std::string album, bool shuffle);

    std::string playArtist(std::string artist, bool shuffle);

    std::string playPlaylist(std::string playlist, bool shuffle);

    std::string pause();

    std::string resume();

    std::string next();

    std::string toggleShuffle(bool shuffle);

    std::string toggleRepeat(bool repeat);

private:
    std::string boolToString(bool b);
    
    SpotifyAPI api;
};

#endif
