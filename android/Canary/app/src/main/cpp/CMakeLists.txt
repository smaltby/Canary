cmake_minimum_required(VERSION 3.5.1)

# configure import libs
set(libs_DIR ${CMAKE_SOURCE_DIR}/../../../../distribution)

add_library(spotify-api-plusplus SHARED IMPORTED)
set_target_properties(spotify-api-plusplus PROPERTIES IMPORTED_LOCATION
    ${libs_DIR}/${ANDROID_ABI}/libspotify-api-plusplus.so)

add_library(crypto STATIC IMPORTED)
set_target_properties(crypto PROPERTIES IMPORTED_LOCATION
    ${libs_DIR}/${ANDROID_ABI}/libcrypto.a)

add_library(ssl STATIC IMPORTED)
set_target_properties(ssl PROPERTIES IMPORTED_LOCATION
    ${libs_DIR}/${ANDROID_ABI}/libssl.a)

add_library(curl STATIC IMPORTED)
set_target_properties(curl PROPERTIES IMPORTED_LOCATION
    ${libs_DIR}/${ANDROID_ABI}/libcurl.a)

set(LOCAL_LDLIBS, '-lz')

# build application's shared lib
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

add_library(main SHARED
            main.cpp)

target_include_directories(main PRIVATE
                           ${libs_DIR}/${ANDROID_ABI}/include
                           ${libs_DIR}/include
                           )

target_link_libraries(main
                      spotify-api-plusplus
                      android
                      curl
                      ssl
                      crypto
                      z
                      log)
