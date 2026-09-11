cmake_minimum_required(VERSION 3.22.1)

project(trimpe)

include(FetchContent)

# =========================
# Download LevelDB MCPE
# =========================

FetchContent_Declare(
    leveldb_mcpe
    GIT_REPOSITORY https://github.com/Amulet-Team/leveldb-mcpe.git
    GIT_TAG main
)

FetchContent_MakeAvailable(leveldb_mcpe)

# =========================
# Trim PE Native Engine
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

# =========================
# Include LevelDB headers
# =========================

target_include_directories(
    trimpe
    PRIVATE
    ${leveldb_mcpe_SOURCE_DIR}/include
)

# =========================
# Link libraries
# =========================

target_link_libraries(
    trimpe
    PRIVATE
    leveldb
    ${log-lib}
)
