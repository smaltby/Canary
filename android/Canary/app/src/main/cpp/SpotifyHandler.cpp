#include <SpotifyHandler.h>
#include <NativeBridge.h>

std::string BoolToString(bool b)
{
    return b ? "true" : "false";
}

SpotifyHandler::SpotifyHandler(std::string authToken)
{
    api = SpotifyAPI();
    api.setAuthToken(authToken);
}

void SpotifyHandler::playTrackFromBy(std::string song, std::string album, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album+" artist:"+artist);
    NativePlayUri(tracks.GetItems()[0].GetUri());
}

void SpotifyHandler::playTrackFrom(std::string song, std::string album)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album);
    NativePlayUri(tracks.GetItems()[0].GetUri());
}

void SpotifyHandler::playTrackBy(std::string song, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" artist:"+artist);
    NativePlayUri(tracks.GetItems()[0].GetUri());
}

void SpotifyHandler::playTrack(std::string song)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song);
    NativePlayUri(tracks.GetItems()[0].GetUri());
}

void SpotifyHandler::playAlbumBy(std::string album, std::string artist)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album+" artist"+artist);
    NativePlayUri(albums.GetItems()[0].GetUri());
}

void SpotifyHandler::playAlbum(std::string album)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album);
    NativePlayUri(albums.GetItems()[0].GetUri());
}

void SpotifyHandler::playArtist(std::string artist)
{
    Pager<Artist> artists = api.SearchArtists("artist:"+artist);
    NativePlayUri(artists.GetItems()[0].GetUri());
}

void SpotifyHandler::playPlaylist(std::string playlist)
{
    Pager<PlaylistSimple> playlists = api.SearchPlaylists(playlist);
    NativePlayUri(playlists.GetItems()[0].GetUri());
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
