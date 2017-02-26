#ifndef CANARY_COMMANDPARSER_H
#define CANARY_COMMANDPARSER_H

#include <string>
#include <regex>

class CommandParser
{
public:
    CommandParser();

    void parse(std::string command, std::string accessToken);

private:
    std::regex playAlbumBy;
    std::regex playAlbum;

    std::regex playArtist;

    std::regex playPlaylist;

    std::regex playSongFromBy;
    std::regex playSongByFrom;
    std::regex playSongFrom;
    std::regex playSongBy;
    std::regex playSong;

    std::regex pause;
    std::regex resume;

    std::regex skip;
    std::regex shuffle;
    std::regex repeat;
};

#endif
