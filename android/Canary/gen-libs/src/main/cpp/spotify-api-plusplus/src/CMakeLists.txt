file(GLOB_RECURSE SPOTIFY_SRC_H
        "*.h"
        )

file(GLOB_RECURSE SPOTIFY_SRC_CPP
        "*.cpp"
        )

set(SPOTIFY_SRC ${SPOTIFY_SRC_H} ${SPOTIFY_SRC_CPP})

add_library(spotify-api-plusplus SHARED ${SPOTIFY_SRC})
set_target_properties(spotify-api-plusplus
                      PROPERTIES
                      LIBRARY_OUTPUT_DIRECTORY
                      "${libs_DIR}/${ANDROID_ABI}")

target_include_directories(spotify-api-plusplus PRIVATE
                           ${libs_DIR}/${ANDROID_ABI}/include
                           )
target_link_libraries(spotify-api-plusplus
                      android
                      curl
                      ssl
                      crypto
                      z
                      log)

install(TARGETS spotify-api-plusplus DESTINATION bin)
install(FILES ${SPOTIFY_SRC_H} DESTINATION include)
