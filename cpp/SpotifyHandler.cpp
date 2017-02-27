#include <SpotifyHandler.h>
#include <NativeBridge.h>

SpotifyHandler::SpotifyHandler(std::string authToken)
{
    api = SpotifyAPI();
    api.setAuthToken(authToken);
}

void SpotifyHandler::playTrackFromBy(std::string song, std::string album, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album+" artist:"+artist);
    if(tracks.GetTotal() > 0)
        NativePlayUri(tracks.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playTrackFrom(std::string song, std::string album)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album);
    if(tracks.GetTotal() > 0)
        NativePlayUri(tracks.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playTrackBy(std::string song, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" artist:"+artist);
    if(tracks.GetTotal() > 0)
        NativePlayUri(tracks.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playTrack(std::string song)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song);
    if(tracks.GetTotal() > 0)
        NativePlayUri(tracks.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playAlbumBy(std::string album, std::string artist)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album+" artist"+artist);
    if(albums.GetTotal() > 0)
        NativePlayUri(albums.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playAlbum(std::string album)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album);
    if(albums.GetTotal() > 0)
        NativePlayUri(albums.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playArtist(std::string artist)
{
    Pager<Artist> artists = api.SearchArtists("artist:"+artist);
    if(artists.GetTotal() > 0)
        NativePlayUri(artists.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::playPlaylist(std::string playlist)
{
    Pager<PlaylistSimple> playlists = api.SearchPlaylists(playlist);
    if(playlists.GetTotal() > 0)
        NativePlayUri(playlists.GetItems()[0].GetUri().c_str());
}

void SpotifyHandler::pause()
{
    NativePause();
}

void SpotifyHandler::resume()
{
    NativeResume();
}

void SpotifyHandler::next()
{
    NativeNext();
}

void SpotifyHandler::toggleShuffle(bool shuffle)
{
    NativeToggleShuffle(shuffle);
}

void SpotifyHandler::toggleRepeat(bool repeat)
{
    NativeToggleRepeat(repeat);
}
