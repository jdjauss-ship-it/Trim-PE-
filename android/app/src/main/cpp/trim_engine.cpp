#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_windx_trimpe_TrimEngine_testNativeEngine(
        JNIEnv* env,
        jobject /* this */
) {

    std::string message =
            "Trim PE C++ Engine Connected!";

    return env->NewStringUTF(
            message.c_str()
    );
}
