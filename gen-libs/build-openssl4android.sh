#!/bin/bash

set -u

# android_shared.sh makes it so all of the build tools used are from the appropriate android toolchain
source ./android_shared.sh

# Specify library name and the destination directory for the generated libraries
LIB_NAME="openssl-1.1.0f"
LIB_DEST_DIR=${TARGET_DIR}/libs

configure_make() {
  ABI=$1; ARCH=$2;
  pushd "${LIB_NAME}"

  # configure() from the shared bash file sets up the specified architecture from the arguments of this function
  configure $*

  # Configure openssl for the architecture. Build the library statically, and find zlib from the toolchain sysroot
  ./Configure $ARCH \
              --prefix=${LIB_DEST_DIR}/${ABI} \
              --with-zlib-include=$SYSROOT/usr/include \
              --with-zlib-lib=$SYSROOT/usr/lib \
              zlib \
              no-asm \
              no-shared \
              no-unit-test

  PATH=$TOOLCHAIN_PATH:$PATH
  make clean
  
  # make the library, and if the build suceeds, install it
  if make -j4; then
    make install_sw
    make install_ssldirs

    # Copy the resulting libraries and include files to $OUTPUT_ROOT
    OUTPUT_ROOT=${TARGET_DIR}/../android/Canary/distribution/${ABI}
    [ -d ${OUTPUT_ROOT}/include ] || mkdir -p ${OUTPUT_ROOT}/include
    cp -r ${LIB_DEST_DIR}/${ABI}/include/openssl ${OUTPUT_ROOT}/include

    [ -d ${OUTPUT_ROOT} ] || mkdir -p ${OUTPUT_ROOT}
    cp ${LIB_DEST_DIR}/${ABI}/lib/libcrypto.a ${OUTPUT_ROOT}
    cp ${LIB_DEST_DIR}/${ABI}/lib/libssl.a ${OUTPUT_ROOT}
  fi;
  popd

}

# For each architecture, run configure_make()
for ((i=0; i < ${#ABIS[@]}; i++))
do
  if [[ $# -eq 0 ]] || [[ "$1" == "${ABIS[i]}" ]]; then
    configure_make "${ABIS[i]}" "${ARCHS[i]}"
  fi
done
