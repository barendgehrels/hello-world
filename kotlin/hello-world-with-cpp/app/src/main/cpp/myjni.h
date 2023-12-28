#pragma once

#include <jni.h>
#include <string>

namespace myjni
{
    struct java_object
    {
        jclass jcls = 0;
        jobject jobj = 0;
    };
    struct java_array
    {
        jclass jcls = 0;
        jobjectArray jarr = 0;
    };
    namespace internal
    {
        template <typename T> struct signature {};
        template <> struct signature<double> { static const char* value() { return "D"; } };

        template <typename T>
        struct access {};

        template <>
        struct access<double> {
            static inline double get(JNIEnv* env, const jobject input, const jfieldID& field) {
                return env->GetDoubleField(input, field);
            }
            static inline void set(JNIEnv* env, const jobject result, const jfieldID& field, double value) {
                env->SetDoubleField(result, field, value);
            }
        };
    }

    template <typename T>
    inline bool set_java_field(JNIEnv* env, const java_object& obj,
                               const char* name, const T& value) {
        jfieldID field = env->GetFieldID(obj.jcls, name, internal::signature<T>::value());
        if (field) {
            internal::access<T>::set(env, obj.jobj, field, value);
            return true;
        }
        return false;
    }

    inline std::string make_cpp_string(JNIEnv* env, jstring s)
    {
        const char* chars = env->GetStringUTFChars(s, 0);
        const std::string result(chars);
        env->ReleaseStringUTFChars(s, chars);
        return result;
    }


    inline java_object make_object(JNIEnv* env, const char* name)
    {
        java_object result;

        result.jcls = env->FindClass(name);
        if (result.jcls == nullptr) {
            return result;
        }
        jmethodID ctor = env->GetMethodID(result.jcls, "<init>", "()V");
        if (ctor == nullptr) {
            return result;
        }

        result.jobj = env->NewObject(result.jcls, ctor);
        return result;
    }

    inline java_array make_array(JNIEnv* env, const char* name, std::size_t size) {
        java_array result;
        result.jcls = env->FindClass(name);
        if (result.jcls == nullptr) {
            return result;
        }
        jmethodID ctor = env->GetMethodID(result.jcls, "<init>", "()V");
        if (ctor == nullptr) {
            return result;
        }
        result.jarr = env->NewObjectArray(size, result.jcls, 0);
        return result;
    }


}
