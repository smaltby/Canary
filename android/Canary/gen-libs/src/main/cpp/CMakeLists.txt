cmake_minimum_required(VERSION 3.5.1)

set(CMAKE_VERBOSE_MAKEFILE on)

set(shared_codebase_DIR ${CMAKE_SOURCE_DIR}/../../../../../../cpp)
set(libs_DIR ${CMAKE_SOURCE_DIR}/../../../../distribution)

set(lib_build_DIR $ENV{HOME}/tmp)
file(MAKE_DIRECTORY ${lib_build_DIR})

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

set(LIBS ${LIBS} android curl ssl crypto z log)

add_subdirectory(${shared_codebase_DIR}/spotify-api-plusplus ${lib_build_DIR}/spotify-api-plusplus)

set_target_properties(spotify-api-plusplus
                      PROPERTIES
                      LIBRARY_OUTPUT_DIRECTORY
                      "${libs_DIR}/${ANDROID_ABI}")

target_include_directories(spotify-api-plusplus PRIVATE
                           ${libs_DIR}/${ANDROID_ABI}/include
)
