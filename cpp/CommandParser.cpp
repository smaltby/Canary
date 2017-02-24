#include <CommandParser.h>
#include <SpotifyHandler.h>

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

    pause = std::regex("^(pause)$");
    resume = std::regex("^(resume)$");

    skip = std::regex("^(skip|next)$");
    shuffle = std::regex("^shuffle (on|off)$");
    repeat = std::regex("^repeat (on|off)$");
}

void CommandParser::parse(std::string command, std::string accessToken)
{
    SpotifyHandler handler = SpotifyHandler(accessToken);

    std::smatch matcher;

    if(std::regex_search(command, matcher, playAlbumBy))
    {
        handler.playAlbumBy(matcher[1].str(), matcher[2].str());
        return;
    }

    if(std::regex_search(command, matcher, playAlbum))
    {
        handler.playAlbum(matcher[1].str());
        return;
    }

    if(std::regex_search(command, matcher, playArtist))
    {
        handler.playArtist(matcher[1].str());
        return;
    }

    if(std::regex_search(command, matcher, playPlaylist))
    {
        handler.playPlaylist(matcher[1].str());
        return;
    }

    if(std::regex_search(command, matcher, playSongFromBy))
    {
        handler.playTrackFromBy(matcher[1].str(), matcher[2].str(), matcher[3].str());
        return;
    }

    if(std::regex_search(command, matcher, playSongByFrom))
    {
        handler.playTrackFromBy(matcher[1].str(), matcher[3].str(), matcher[2].str());
        return;
    }

    if(std::regex_search(command, matcher, playSongFrom))
    {
        handler.playTrackFrom(matcher[1].str(), matcher[2].str());
        return;
    }

    if(std::regex_search(command, matcher, playSongBy))
    {
        handler.playTrackBy(matcher[1].str(), matcher[2].str());
        return;
    }

    if(std::regex_search(command, matcher, playSong))
    {
        handler.playTrack(matcher[1].str());
        return;
    }

    if(std::regex_search(command, matcher, pause))
    {
        handler.pause();
        return;
    }

    if(std::regex_search(command, matcher, resume))
    {
        handler.resume();
        return;
    }

    if(std::regex_search(command, matcher, skip))
    {
        handler.next();
        return;
    }

    if(std::regex_search(command, matcher, shuffle))
    {
        handler.toggleShuffle(matcher[1].str() == "on");
        return;
    }

    if(std::regex_search(command, matcher, repeat))
    {
        handler.toggleRepeat(matcher[1].str() == "on");
    }
}

