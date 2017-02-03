cmake_minimum_required(VERSION 3.5.1)

set(CMAKE_VERBOSE_MAKEFILE on)

set(lib_src_DIR ${CMAKE_CURRENT_SOURCE_DIR})

set(lib_build_DIR $ENV{HOME}/tmp)
file(MAKE_DIRECTORY ${lib_build_DIR})

set(libs_DIR ${CMAKE_SOURCE_DIR}/../../../../distribution)

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

add_subdirectory(${lib_src_DIR}/spotify-api-plusplus ${lib_build_DIR}/spotify-api-plusplus)