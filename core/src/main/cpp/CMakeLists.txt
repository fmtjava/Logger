cmake_minimum_required(VERSION 3.18.1)

add_library(
        logger-lib

        SHARED

        logger-lib.cpp)

find_library(
        log-lib

        log)

target_link_libraries(
        logger-lib

        ${log-lib})