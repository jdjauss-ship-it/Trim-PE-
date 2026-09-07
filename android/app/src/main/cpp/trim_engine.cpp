#include <jni.h>
#include <string>
#include <sstream>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_windx_trimpe_TrimEngine_testNativeEngine(
        JNIEnv* env,
        jobject
) {

    std::string message =
            "Trim PE C++ Engine Connected!";

    return env->NewStringUTF(message.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_windx_trimpe_TrimEngine_prepareTrim(
        JNIEnv* env,
        jobject,
        jstring worldPath,
        jint x1,
        jint z1,
        jint x2,
        jint z2
) {

    const char* pathChars =
            env->GetStringUTFChars(
                    worldPath,
                    nullptr
            );

    std::string path =
            pathChars != nullptr
            ? pathChars
            : "";

    if (pathChars != nullptr) {

        env->ReleaseStringUTFChars(
                worldPath,
                pathChars
        );
    }

    std::stringstream result;

    result
        << "Trim Engine Ready!\n\n"
        << "World: " << path << "\n\n"
        << "Safe Area:\n"
        << "X: " << x1 << " to " << x2 << "\n"
        << "Z: " << z1 << " to " << z2
        << "\n\n"
        << "LevelDB integration pending.";

    std::string message =
            result.str();

    return env->NewStringUTF(
            message.c_str()
    );
}
