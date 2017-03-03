#include <SpotifyHandler.h>

SpotifyHandler::SpotifyHandler(std::string authToken)
{
    api = SpotifyAPI();
    api.setAuthToken(authToken);
}

std::string SpotifyHandler::playTrackFromBy(std::string song, std::string album, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album+" artist:"+artist);
    if(tracks.GetTotal() > 0)
        return "playuri " + tracks.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playTrackFrom(std::string song, std::string album)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" album:"+album);
    if(tracks.GetTotal() > 0)
        return "playuri " + tracks.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playTrackBy(std::string song, std::string artist)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song+" artist:"+artist);
    if(tracks.GetTotal() > 0)
        return "playuri " + tracks.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playTrack(std::string song)
{
    Pager<Track> tracks = api.SearchTracks("track:"+song);
    if(tracks.GetTotal() > 0)
        return "playuri " + tracks.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playAlbumBy(std::string album, std::string artist)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album+" artist"+artist);
    if(albums.GetTotal() > 0)
        return "playuri " + albums.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playAlbum(std::string album)
{
    Pager<AlbumSimple> albums = api.SearchAlbums("album:"+album);
    if(albums.GetTotal() > 0)
        return "playuri " + albums.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playArtist(std::string artist)
{
    Pager<Artist> artists = api.SearchArtists("artist:"+artist);
    if(artists.GetTotal() > 0)
        return "playuri " + artists.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::playPlaylist(std::string playlist)
{
    Pager<PlaylistSimple> playlists = api.SearchPlaylists(playlist);
    if(playlists.GetTotal() > 0)
        return "playuri " + playlists.GetItems()[0].GetUri();
    return "error no results";
}

std::string SpotifyHandler::pause()
{
    return "pause";
}

std::string SpotifyHandler::resume()
{
    return "resume";
}

std::string SpotifyHandler::next()
{
    return "next";
}

std::string SpotifyHandler::toggleShuffle(bool shuffle)
{
    return "shuffle " + shuffle;
}

std::string SpotifyHandler::toggleRepeat(bool repeat)
{
    return "repeat " + repeat;
}
