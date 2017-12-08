#!/bin/bash

set -u

# android_shared.sh makes it so all of the build tools used are from the appropriate android toolchain
source ./android_shared.sh

# Specify library name and the destination directory for the generated libraries
LIB_NAME="curl-7.56.1"
LIB_DEST_DIR=${TARGET_DIR}/libs

configure_make() {
  ABI=$1;
  pushd "${LIB_NAME}"

  # configure() from the shared bash file sets up the specified architecture from the arguments of this function
  configure $*

  # Copy openssl libraries and include files to the toolchain sysroot, so curl when find them while being built later
  cp ${TARGET_DIR}/../output/android/openssl-${ABI}/lib/libssl.a ${SYSROOT}/usr/lib
  cp ${TARGET_DIR}/../output/android/openssl-${ABI}/lib/libcrypto.a ${SYSROOT}/usr/lib
  cp -r ${LIB_DEST_DIR}/${ABI}/include/openssl ${SYSROOT}/usr/include

  # Configure cURL for the architecture. Build the library statically, disable unnecessary features
  mkdir -p ${LIB_DEST_DIR}/${ABI}
  ./configure --prefix=${LIB_DEST_DIR}/${ABI} \
              --with-sysroot=${SYSROOT} \
              --host=${TOOL} \
              --with-ssl=/usr \
              --enable-ipv6 \
              --enable-static \
              --enable-threaded-resolver \
              --disable-dict \
              --disable-gopher \
              --disable-ldap --disable-ldaps \
              --disable-manual \
              --disable-pop3 --disable-smtp --disable-imap \
              --disable-rtsp \
              --disable-shared \
              --disable-smb \
              --disable-telnet \
              --disable-verbose

  make clean

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