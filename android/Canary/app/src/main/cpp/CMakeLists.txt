
#
# Copyright (C) The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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

add_library(hello-libs SHARED
            hello-libs.cpp)

target_include_directories(hello-libs PRIVATE
                           ${libs_DIR}/${ANDROID_ABI}/include
                           ${libs_DIR}/include
                           )

target_link_libraries(hello-libs
                      spotify-api-plusplus
                      android
                      curl
                      ssl
                      crypto
                      z
                      log)
