cmake_minimum_required(VERSION 3.22.1)

project("trimpe")

add_library(
    trimpe
    SHARED
    trim_engine.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    trimpe
    ${log-lib}
)
