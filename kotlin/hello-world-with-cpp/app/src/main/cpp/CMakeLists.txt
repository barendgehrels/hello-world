# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.18.1)
project("hw-geometry-project")

find_library(log-lib log)

# This should be set somehow before, or adapted here.
set(BOOST_ROOT /home/gehrels/git_public/boost)

add_library(hw-geometry SHARED geometry-lib.cpp)
target_include_directories(hw-geometry PRIVATE ${BOOST_ROOT})
target_link_libraries(hw-geometry ${log-lib})