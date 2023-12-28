#include <jni.h>
#include <android/log.h>
#include <string>
#include <sstream>

#include "myjni.h"
#include <boost/geometry.hpp>

namespace {

    std::string do_buffer(const std::string &wkt, double distance_in_meters) {
        namespace bg = boost::geometry;

        using point_t = bg::model::point<double, 2, bg::cs::geographic<bg::degree> >;
        using polygon_t = bg::model::polygon<point_t>;
        using multi_polygon_t = bg::model::multi_polygon<polygon_t>;

        multi_polygon_t geo;
        bg::read_wkt(wkt, geo);

        using formula = bg::strategy::andoyer;

        bg::strategies::buffer::geographic <formula> strategy;
        bg::strategy::buffer::geographic_join_round <formula> join(36);

        // Dummy strategies, not used for polygon
        bg::strategy::buffer::geographic_side_straight <formula> _s;
        bg::strategy::buffer::geographic_point_circle <formula> _c;
        bg::strategy::buffer::end_flat _e;

        bg::strategy::buffer::distance_symmetric<double> distance_strategy(distance_in_meters);

        multi_polygon_t buffered;
        bg::detail::buffer::buffer_inserter<polygon_t>(geo,
                                                       std::back_inserter(buffered),
                                                       distance_strategy,
                                                       _s,
                                                       join,
                                                       _e,
                                                       _c,
                                                       strategy,
                                                       bg::detail::no_rescale_policy());

        polygon_t hull;
        bg::convex_hull(buffered, hull);

        std::ostringstream out;
        out << bg::area(hull) / (1000.0 * 1000.0);
        return out.str();
    }


    template <typename P>
    inline myjni::java_object MakePoint(JNIEnv* env, const P& point)
    {
        auto jpoint = myjni::make_object(env, "org/barend/withcpp/Point");
        myjni::set_java_field(env, jpoint, "lon", boost::geometry::get<0>(point));
        myjni::set_java_field(env, jpoint, "lat", boost::geometry::get<1>(point));
        return jpoint;
    }

}



extern "C" JNIEXPORT jstring JNICALL
Java_org_barend_withcpp_Geometry_buffer(
        JNIEnv* env,
        jobject thiz, jstring wkt, jdouble distance) {

    const auto cppWkt = myjni::make_cpp_string(env, wkt);
    const auto result = do_buffer(cppWkt, distance);
    return env->NewStringUTF(result.c_str());
}

extern "C" JNIEXPORT jobject JNICALL
Java_org_barend_withcpp_Geometry_toPoint(JNIEnv* env, jobject thiz, jstring wkt)
{

    try {
        const std::string cppWkt = myjni::make_cpp_string(env, wkt);
        namespace bg = boost::geometry;
        bg::model::point<double, 2, bg::cs::geographic<bg::degree> > point;
        bg::read_wkt(cppWkt, point);
        const auto jpoint = MakePoint(env, point);
        return jpoint.jobj;
    } catch (const std::exception& e) {
        __android_log_print(ANDROID_LOG_ERROR, "GEO", "EXCEPTION %s", e.what());
    }

    return {};
}

extern "C" JNIEXPORT jobject JNICALL
Java_org_barend_withcpp_Geometry_toLineString(JNIEnv* env, jobject thiz, jstring wkt)
{
    try {
        const std::string cppWkt = myjni::make_cpp_string(env, wkt);

        namespace bg = boost::geometry;
        using point_t = bg::model::point<double, 2, bg::cs::geographic<bg::degree> >;
        bg::model::linestring<point_t> linestring;

        bg::read_wkt(cppWkt, linestring);
        auto result = myjni::make_object(env, "org/barend/withcpp/LineString");
        auto points = myjni::make_array(env, "org/barend/withcpp/Point", linestring.size());
        for (std::size_t i = 0; i < linestring.size(); i++)
        {
            auto jpoint = myjni::make_object(env, "org/barend/withcpp/Point");
            myjni::set_java_field(env, jpoint, "lon", boost::geometry::get<0>(linestring[i]));
            myjni::set_java_field(env, jpoint, "lat", boost::geometry::get<1>(linestring[i]));
            env->SetObjectArrayElement(points.jarr, i, jpoint.jobj);
        }
        // Unclear how to do this...
        jfieldID field = env->GetFieldID(result.jcls, "points", "[Lorg/barend/withcpp/Point;");
        env->SetArrayField(result.jobj, field, points);
        //myjni::set_java_field(env, result, "points", jpoint);
        return result.jobj;
    } catch (const std::exception& e) {
        __android_log_print(ANDROID_LOG_ERROR, "GEO", "EXCEPTION %s", e.what());
    }
    return {};
}

extern "C" JNIEXPORT jdouble JNICALL
Java_org_barend_withcpp_Geometry_length(JNIEnv* env, jobject thiz, jobject linestring) {
    // Translate the linestring
    return 0.0;
}