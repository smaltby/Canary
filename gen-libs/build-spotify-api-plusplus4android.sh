#!/bin/bash

set -u

# android_shared.sh makes it so all of the build tools used are from the appropriate android toolchain
source ./android_shared.sh

# Specify library name and the destination directory for the generated libraries
LIB_NAME="spotify-api-plusplus"
LIB_DEST_DIR=${TARGET_DIR}/libs

configure_make() {
  ABI=$1;
  pushd "${LIB_NAME}"

  # configure() from the shared bash file sets up the specified architecture from the arguments of this function
  configure $*

  # Copy curl and openssl libraries and include files to the toolchain sysroot
  cp ${LIB_DEST_DIR}/${ABI}/lib/libssl.a ${SYSROOT}/usr/lib
  cp ${LIB_DEST_DIR}/${ABI}/lib/libcrypto.a ${SYSROOT}/usr/lib
  cp ${LIB_DEST_DIR}/${ABI}/lib/libcurl.a ${SYSROOT}/usr/lib
  cp -r ${LIB_DEST_DIR}/${ABI}/include/openssl ${SYSROOT}/usr/include
  cp -r ${LIB_DEST_DIR}/${ABI}/include/curl ${SYSROOT}/usr/include

  cmake -DANDROID=true \
        -DANDROID_PLATFORM=android-21 \
        -DANDROID_STL=c++_static \
        -DANDROID_TOOILCHAIN=clang \
        -DCMAKE_C_FLAGS="-std=c++11 -frtti -fexceptions" \
        -DCMAKE_TOOLCHAIN_FILE=$NDK/build/cmake/android.toolchain.cmake \
        -DANDROID_ABI=${ABI}
        .


  # make the library, and if the build suceeds, install it
  if make -j4; then
    make install

    # Copy the resulting libraries and include files to $OUTPUT_ROOT
    OUTPUT_ROOT=${TARGET_DIR}/../android/Canary/distribution/${ABI}
    [ -d ${OUTPUT_ROOT}/include ] || mkdir -p ${OUTPUT_ROOT}/include
    cp -r ${LIB_DEST_DIR}/${ABI}/include/curl ${OUTPUT_ROOT}/include

    [ -d ${OUTPUT_ROOT} ] || mkdir -p ${OUTPUT_ROOT}
    cp ${LIB_DEST_DIR}/${ABI}/lib/libcurl.a ${OUTPUT_ROOT}
  fi;
  popd;
}


# For each architecture, run configure_make()
for ((i=0; i < ${#ABIS[@]}; i++))
do
  if [[ $# -eq 0 ]] || [[ "$1" == "${ABIS[i]}" ]]; then
    configure_make "${ABIS[i]}"
  fi
done