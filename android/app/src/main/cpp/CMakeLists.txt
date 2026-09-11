cmake_minimum_required(VERSION 3.22.1)

project("trimpe")

include(FetchContent)

# =========================
# LEVELDB MCPE
# =========================

set(LEVELDB_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(LEVELDB_BUILD_BENCHMARKS OFF CACHE BOOL "" FORCE)

FetchContent_Declare(
    leveldb_mcpe
    GIT_REPOSITORY https://github.com/8Crafter-Studios/leveldb-mcpe.git
    GIT_TAG master
)

FetchContent_MakeAvailable(leveldb_mcpe)

# =========================
# TRIM PE ENGINE
# =========================

add_library(
    trimpe
    SHARED
    trim_engine.cpp
)

find_library(
    log-lib
    log
)

target_include_directories(
    trimpe
    PRIVATE
    ${leveldb_mcpe_SOURCE_DIR}/include
)

target_link_libraries(
    trimpe
    PRIVATE
    leveldb
    ${log-lib}
)
