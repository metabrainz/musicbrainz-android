# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.10.2)

# Project name
project("ktaglib")

add_subdirectory("../../../../taglib/taglib" taglib)

add_library(ktaglib SHARED ktaglib.cpp)

find_library(log-lib log)
target_link_libraries(ktaglib
        ${log-lib}
        tag)

target_include_directories(tag PUBLIC
        "../../../../taglib/3rdparty")

target_include_directories(ktaglib PUBLIC
        "../../../../taglib/taglib"
        "../../../../taglib/taglib/toolkit")
