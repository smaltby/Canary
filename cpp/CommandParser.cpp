#include <CommandParser.h>
#include <SpotifyHandler.h>

#include <android/log.h>
#include<map>

#include <spotify-api-plusplus/utils/SpotifyException.h>
#include <spotify-api-plusplus/utils/CurlException.h>
#include <curl/curl.h>

std::string data;

std::map<std::string, std::string> parse_json(std::string json);

CommandParser::CommandParser()
{
    playAlbumBy = std::regex("^(play|shuffle) album (.+) by (.+)$");
    playAlbum = std::regex("^(play|shuffle) album (.+)$");

    playArtist = std::regex("^(play|shuffle) artist (.+)$");

    playPlaylist = std::regex("^(play|shuffle) playlist (.+)$");

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

static size_t writeCallback(char* buf, size_t size, size_t nmemb, void* up)
{ //callback must have this declaration
    //buf is a pointer to the data that curl has for us
    //size*nmemb is the size of the buffer

    for (int c = 0; c<size*nmemb; c++)
    {
        data.push_back(buf[c]);
    }
    return size*nmemb; //tell curl how many bytes we handled
}

std::string ReplaceAll(std::string str, const std::string& from, const std::string& to)
{
    size_t start_pos = 0;
    while((start_pos = str.find(from, start_pos)) != std::string::npos)
    {
        str.replace(start_pos, from.length(), to);
        start_pos += to.length();
    }
    return str;
}

std::string CommandParser::parse(std::string command, std::string accessToken)
{
    SpotifyHandler handler = SpotifyHandler(accessToken);

    __android_log_print(ANDROID_LOG_DEBUG, "HelloNDK!", "TEST LOG");

    CURL* curl; //our curl object

    curl_global_init(CURL_GLOBAL_ALL); //pretty obvious
    curl = curl_easy_init();

    std::string url_string = "https://api.wit.ai/message?v=20171114&q=";

    command = ReplaceAll(command, " ", "%20");
    command = ReplaceAll(command, "'", "%27");

    url_string = url_string + command;

    curl_easy_setopt(curl, CURLOPT_URL, url_string.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, &writeCallback);
    curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L); //tell curl to output its progress

    std::string AUTHTOKEN = "7THP3K667WLG65HPZKV27FFCZ2Q5E3X4"; //this is bad, put this in a file, here for testing
    std::string header = "Authorization: Bearer " + AUTHTOKEN;
    struct curl_slist *headers = NULL;
    headers = curl_slist_append(headers, header.c_str());
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

    curl_easy_perform(curl);

    curl_easy_cleanup(curl);
    curl_global_cleanup();

    std::map<std::string, std::string> wit_ai_data = parse_json(data);

    if(wit_ai_data["intent"] == "playAlbumBy")
        return handler.playAlbumBy(wit_ai_data["album"], wit_ai_data["artist"], wit_ai_data["playback"] == "shuffle");
    else if(wit_ai_data["intent"] == "playAlbum")
        return handler.playAlbum(wit_ai_data["album"], wit_ai_data["playback"] == "shuffle");
    else if(wit_ai_data["intent"] == "playArtist")
        return handler.playArtist(wit_ai_data["artist"], wit_ai_data["playback"] == "shuffle");
    else if(wit_ai_data["intent"] == "playPlaylist")
        return handler.playPlaylist(wit_ai_data["playlist"], wit_ai_data["playback"] == "shuffle");
    else if(wit_ai_data["intent"] == "playSongFromBy")
        return handler.playTrackFromBy(wit_ai_data["song"], wit_ai_data["album"], wit_ai_data["artist"]);
    else if(wit_ai_data["intent"] == "playSongFrom")
        return handler.playTrackFrom(wit_ai_data["song"], wit_ai_data["album"]);
    else if(wit_ai_data["intent"] == "playTrackBy")
        return handler.playTrackBy(wit_ai_data["song"], wit_ai_data["artist"]);
    else if(wit_ai_data["intent"] == "playSong")
        return handler.playTrack(wit_ai_data["song"]);
    else if(wit_ai_data["intent"] == "pause")
        return handler.pause();
    else if(wit_ai_data["intent"] == "resume")
        return handler.resume();
    else if(wit_ai_data["intent"] == "skip")
        return handler.next();
    else if(wit_ai_data["intent"] == "shuffle")
        return handler.toggleShuffle(wit_ai_data["playback"] == "shuffle");
    else if(wit_ai_data["intent"] == "repeat")
        return handler.toggleShuffle(wit_ai_data["playback"] == "repeat");


    return "error invalid input";
}

std::map<std::string, std::string> parse_json(std::string json) {
    std::map<std::string,std::string> data;

    int i = json.find("{", json.find("entities")) + 12;

    //get all keys from entities
    while(i < json.length()) {
        std ::string key = "";
        while(json[i] != '"')
            key+= json[i];
            i++;

        std::string value = "";
        i = json.find("value", i) + 7;
        while(json[i] != '"') {
            value += json[i];
        }

        i = json.find("]", i) + 3;

        //stop finding keys when we run out.
        //we run out when there are no more new dictionaries left
        if(json.find("{") == std::string::npos) {
            break;
        }
    }
};




