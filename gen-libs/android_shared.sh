#!/bin/bash

# Currently, the target directory to generate the libraries is the directory the script is being run from
TARGET_DIR=`pwd`

# Android API to build for, this should be the minimum API of the Android project where the libraries will be used
ANDROID_API=${ANDROID_API:-21}

# ABIs to build for
ABIS=("arm64-v8a" "armeabi" "armeabi-v7a" "x86" "x86_64")

# Architectures corresponding to the ABI of the same index. 
ARCHS=("android64-aarch64" "android" "android-armeabi" "android-x86" "android64")

eval NDK=${ANDROID_NDK}

configure() {
  ABI=$1;

  # The root directory of the toolchain
  TOOLCHAIN_ROOT=${TARGET_DIR}/${ABI}-android-toolchain

  # Set ABI specific flags
  if [ "$ABI" == "arm64-v8a" ]; then
    export ARCH_CFLAGS=""
    export ARCH_LDFLAGS=""
    export TOOL="aarch64-linux-android"
    NDK_FLAGS="--arch=arm64"
  elif [ "$ABI" == "armeabi" ]; then
    export ARCH_CFLAGS="-mthumb"
    export ARCH_LDFLAGS=""
    export TOOL="arm-linux-androideabi"
    NDK_FLAGS="--arch=arm"
  elif [ "$ABI" == "armeabi-v7a" ]; then
    export ARCH_CFLAGS="-march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16 -mthumb -mfpu=neon"
    export ARCH_LDFLAGS="-march=armv7-a -Wl,--fix-cortex-a8"
    export TOOL="arm-linux-androideabi"
    NDK_FLAGS="--arch=arm"
  elif [ "$ABI" == "x86" ]; then
    export ARCH_CFLAGS="-march=i686 -mtune=intel -msse3 -mfpmath=sse -m32"
    export ARCH_LDFLAGS=""
    export TOOL="i686-linux-android"
    NDK_FLAGS="--arch=x86"
  elif [ "$ABI" == "x86_64" ]; then
    export ARCH_CFLAGS="-march=x86-64 -msse4.2 -mpopcnt -m64 -mtune=intel"
    export ARCH_LDFLAGS=""
    export TOOL="x86_64-linux-android"
    NDK_FLAGS="--arch=x86_64"
  fi;

  # Check if the toolchain has already been generated for this architecture, and if not, generate it
  [ -d ${TOOLCHAIN_ROOT} ] || python $NDK/build/tools/make_standalone_toolchain.py \
                                     --api ${ANDROID_API} \
                                     --stl libc++ \
                                     --install-dir=${TOOLCHAIN_ROOT} \
                                     --deprecated-headers \
                                     $NDK_FLAGS

  export TOOLCHAIN_PATH=${TOOLCHAIN_ROOT}/bin
  export NDK_TOOLCHAIN_BASENAME=${TOOLCHAIN_PATH}/${TOOL}
  export SYSROOT=${TOOLCHAIN_ROOT}/sysroot
  export CROSS_SYSROOT=$SYSROOT
  export CC=${NDK_TOOLCHAIN_BASENAME}-gcc
  export CXX=${NDK_TOOLCHAIN_BASENAME}-g++
  export LINK=${CXX}
  export LD=${NDK_TOOLCHAIN_BASENAME}-ld
  export AR=${NDK_TOOLCHAIN_BASENAME}-ar
  export RANLIB=${NDK_TOOLCHAIN_BASENAME}-ranlib
  export STRIP=${NDK_TOOLCHAIN_BASENAME}-strip
  export CPPFLAGS=${CPPFLAGS:-""}
  export LIBS=${LIBS:-""}
  export CFLAGS="${ARCH_CFLAGS} -fpic -ffunction-sections -funwind-tables -fstack-protector -fno-strict-aliasing -finline-limit=64"
  export CXXFLAGS="${CFLAGS} -std=c++11 -frtti -fexceptions"
  export LDFLAGS="${ARCH_LDFLAGS}"
  echo "**********************************************"
  echo "use ANDROID_API=${ANDROID_API}"
  echo "use NDK=${ANDROID_NDK}"
  echo "export NDK_TOOLCHAIN_BASENAME=${NDK_TOOLCHAIN_BASENAME}"
  echo "export SYSROOT=${SYSROOT}"
  echo "export CC=${CC}"
  echo "export CXX=${CXX}"
  echo "export LINK=${LINK}"
  echo "export LD=${LD}"
  echo "export AR=${AR}"
  echo "export RANLIB=${RANLIB}"
  echo "export STRIP=${STRIP}"
  echo "export CPPFLAGS=${CPPFLAGS}"
  echo "export CFLAGS=${CFLAGS}"
  echo "export CXXFLAGS=${CXXFLAGS}"
  echo "export LDFLAGS=${LDFLAGS}"
  echo "export LIBS=${LIBS}"
  echo "**********************************************"
}
