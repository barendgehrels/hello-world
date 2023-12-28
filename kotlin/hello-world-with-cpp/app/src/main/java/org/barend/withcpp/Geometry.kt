package org.barend.withcpp

data class Point(val lon : Double = 0.0, val lat : Double = 0.0) {}

data class LineString(val points : Array<Point> = arrayOf()) {}

class Geometry
{
    external fun buffer(wkt : String, distance: Double) : String

    external fun toPoint(wkt : String) : Point
    external fun toLineString(wkt : String) : LineString
    external fun length(line : LineString) : Double

    companion object
    {
        init
        {
            System.loadLibrary("hw-geometry")
        }
    }
}