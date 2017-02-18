#ifndef CANARY_SPOTIFYHANDLER_H
#define CANARY_SPOTIFYHANDLER_H

#include <string>
#include <SpotifyAPI.h>

class SpotifyHandler
{
public:
    SpotifyHandler(std::string authToken);

    void playTrackFromBy(std::string song, std::string album, std::string artist);

    void playTrackFrom(std::string song, std::string album);

    void playTrackBy(std::string song, std::string artist);

    void playTrack(std::string song);

    void playAlbumBy(std::string album, std::string artist);

    void playAlbum(std::string album);

    void playArtist(std::string artist);

    void playPlaylist(std::string playlist);

    void pause();

    void resume();

    void next();

    void toggleShuffle(bool shuffle);

    void toggleRepeat(bool repeat);

private:
    SpotifyAPI api;
};

#endif