#include <CommandParser.h>
#include <SpotifyHandler.h>

void parse(std::string command, std::string accessToken)
{
    SpotifyHandler handler = SpotifyHandler(accessToken);
    handler.playTrackFromBy(command, command, command);
}

