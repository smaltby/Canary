#include <SpotifyHandler.h>
#include <NativeBridge.h>

SpotifyHandler::SpotifyHandler(std::string authToken)
{
    api = SpotifyAPI();
    api.setAuthToken(authToken);
}

void SpotifyHandler::playTrackFromBy(std::string song, std::string album, std::string artist)
{
    //Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album+" artist:"+artist);
    playUri(song);
}

void SpotifyHandler::playTrackFrom(std::string song, std::string album)
{

}

void SpotifyHandler::playTrackBy(std::string song, std::string artist)
{

}

void SpotifyHandler::playTrack(std::string song)
{

}

void SpotifyHandler::playAlbumBy(std::string album, std::string artist)
{

}

void SpotifyHandler::playAlbum(std::string album)
{

}

void SpotifyHandler::playArtist(std::string artist)
{

}

void SpotifyHandler::playPlaylist(std::string playlist)
{

}

void SpotifyHandler::pause()
{

}

void SpotifyHandler::resume()
{

}

void SpotifyHandler::next()
{

}

void SpotifyHandler::toggleShuffle(bool shuffle)
{

}

void SpotifyHandler::toggleRepeat(bool repeat)
{

}

























