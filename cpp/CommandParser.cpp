#include <CommandParser.h>
#include <SpotifyHandler.h>
#include <utils/SpotifyException.h>
#include <utils/CurlException.h>

CommandParser::CommandParser()
{
    playAlbumBy = std::regex("^play album (.+) by (.+)$");
    playAlbumBy = std::regex("^play album (.+) by (.+)$");
    playAlbum = std::regex("^play album (.+)$");

    playArtist = std::regex("^play artist (.+)$");

    playPlaylist = std::regex("^play playlist (.+)$");

    playSongFromBy = std::regex("^play (.+) from (.+) by (.+)$");
    playSongByFrom = std::regex("^play (.+) by (.+) from (.+)$");
    playSongFrom = std::regex("^play (.+) from (.+)$");
    playSongBy = std::regex("^play (.+) by (.+)$");
    playSong = std::regex("^play (.+)$");

    pause = std::regex("^(pause|paws)$");
    resume = std::regex("^(resume)$");

    skip = std::regex("^(skip|next)$");
    shuffle = std::regex("^shuffle (on|off)$");
    repeat = std::regex("^repeat (on|off)$");
}

std::string CommandParser::parse(std::string command, std::string accessToken)
{
    SpotifyHandler handler = SpotifyHandler(accessToken);

    std::smatch matcher;

    try
    {
        if (std::regex_search(command, matcher, playAlbumBy))
            return handler.playAlbumBy(matcher[1].str(), matcher[2].str());

        if (std::regex_search(command, matcher, playAlbum))
            return handler.playAlbum(matcher[1].str());

        if (std::regex_search(command, matcher, playArtist))
            return handler.playArtist(matcher[1].str());

        if (std::regex_search(command, matcher, playPlaylist))
            return handler.playPlaylist(matcher[1].str());

        if (std::regex_search(command, matcher, playSongFromBy))
            return handler.playTrackFromBy(matcher[1].str(), matcher[2].str(), matcher[3].str());

        if (std::regex_search(command, matcher, playSongByFrom))
            return handler.playTrackFromBy(matcher[1].str(), matcher[3].str(), matcher[2].str());

        if (std::regex_search(command, matcher, playSongFrom))
            return handler.playTrackFrom(matcher[1].str(), matcher[2].str());

        if (std::regex_search(command, matcher, playSongBy))
            return handler.playTrackBy(matcher[1].str(), matcher[2].str());

        if (std::regex_search(command, matcher, playSong))
            return handler.playTrack(matcher[1].str());

        if (std::regex_search(command, matcher, pause))
            return handler.pause();

        if (std::regex_search(command, matcher, resume))
            return handler.resume();

        if (std::regex_search(command, matcher, skip))
            return handler.next();

        if (std::regex_search(command, matcher, shuffle))
            return handler.toggleShuffle(matcher[1].str() == "on");

        if (std::regex_search(command, matcher, repeat))
            return handler.toggleRepeat(matcher[1].str() == "on");
    } catch(SpotifyException e)
    {
        return "error error, " + std::string(e.what());
    } catch(CurlException e)
    {
        return "error error, " + std::string(e.what());
    }

    return "error invalid input";
}

