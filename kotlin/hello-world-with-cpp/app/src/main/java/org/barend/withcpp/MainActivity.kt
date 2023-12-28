package org.barend.withcpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.barend.withcpp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding

    val geo = Geometry()

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pnt = geo.toPoint("POINT(1 2)")
        val wkt = "MULTIPOLYGON(((20.898052 37.805275,20.992496 37.725273,20.998608 37.713882,20.994442 37.698608,20.943333 37.719719,20.924164 37.728607,20.915276 37.731667,20.895832 37.73111,20.87833 37.728333,20.870277 37.726105,20.866943 37.724159,20.84111 37.687218,20.839722 37.683327,20.836109 37.646385,20.831108 37.646385,20.812222 37.651665,20.808609 37.65361,20.713608 37.722771,20.703331 37.732773,20.629997 37.808884,20.62722 37.817497,20.619999 37.847496,20.621944 37.860832,20.628609 37.875549,20.64333 37.898331,20.678333 37.920555,20.699718 37.930275,20.702774 37.928055,20.794167 37.848885,20.863331 37.829163,20.886387 37.814438,20.898052 37.805275)),((20.612499 38.394165,20.680553 38.277496,20.811665 38.121384,20.813332 38.118332,20.81361 38.114441,20.811943 38.105553,20.796665 38.066666,20.7925 38.060272,20.788055 38.059441,20.734997 38.060829,20.557777 38.090553,20.51722 38.102493,20.512775 38.103882,20.377777 38.156662,20.344444 38.174995,20.341389 38.177216,20.339996 38.184441,20.340832 38.196938,20.34222 38.200829,20.35611 38.231941,20.398609 38.325554,20.44083 38.328049,20.442776 38.324997,20.47361 38.309166,20.478611 38.308052,20.483608 38.308327,20.487221 38.309715,20.500553 38.317215,20.507221 38.321106,20.516388 38.327774,20.536388 38.344719,20.54472 38.357498,20.546665 38.361107,20.547497 38.364716,20.550552 38.387772,20.547775 38.394165,20.539719 38.409439,20.534443 38.432777,20.533886 38.436661,20.536942 38.467773,20.539165 38.470551,20.543053 38.471939,20.548332 38.471939,20.562222 38.471107,20.57111 38.468048,20.612499 38.394165)),((20.735554 38.309998,20.719719 38.305275,20.714722 38.306389,20.707222 38.309998,20.699444 38.317497,20.67083 38.35305,20.644722 38.398048,20.615555 38.462494,20.613331 38.469162,20.614166 38.47361,20.648888 38.500275,20.656666 38.501106,20.671108 38.493332,20.706387 38.444717,20.708054 38.44194,20.743332 38.372498,20.761108 38.325272,20.760277 38.321663,20.735554 38.309998)),((20.903053 38.545555,20.899441 38.544167,20.896111 38.545273,20.899166 38.566383,20.914165 38.583328,20.935833 38.604164,20.938889 38.60527,20.941387 38.602493,20.939999 38.593887,20.903053 38.545555)),((20.643608 38.581108,20.54361 38.564438,20.542774 38.567772,20.542221 38.588333,20.557777 38.683884,20.559166 38.688049,20.601665 38.778885,20.643055 38.826942,20.648331 38.832222,20.655552 38.835548,20.701385 38.834717,20.709721 38.832222,20.715832 38.827774,20.729443 38.807495,20.73 38.803886,20.724163 38.634995,20.723053 38.626389,20.720554 38.623604,20.643608 38.581108)),((20.19833 39.174438,20.178055 39.174164,20.165554 39.177498,20.162777 39.179443,20.136665 39.200554,20.124165 39.221382,20.122776 39.232773,20.123608 39.236382,20.133331 39.239441,20.137497 39.240273,20.164997 39.222496,20.187496 39.203049,20.199444 39.186386,20.202221 39.175827,20.19833 39.174438)),((19.926109 39.794441,19.946941 39.783607,19.950554 39.774437,19.950554 39.762215,19.942776 39.744164,19.929165 39.73111,19.926388 39.728607,19.916111 39.722771,19.91222 39.721382,19.875553 39.71611,19.848888 39.705826,19.842499 39.702774,19.839996 39.699997,19.838608 39.695831,19.839443 39.676384,19.846664 39.649162,19.927776 39.477219,19.931942 39.471664,20.018055 39.434441,20.027775 39.431938,20.032776 39.43222,20.048054 39.436943,20.057499 39.442215,20.063332 39.44722,20.067497 39.453606,20.071663 39.453606,20.074718 39.451385,20.121666 39.377777,20.122219 39.373886,20.120277 39.370552,20.114998 39.364998,20.111664 39.363052,20.07972 39.368332,19.884163 39.444443,19.880276 39.446388,19.875275 39.45166,19.854721 39.483887,19.853054 39.486938,19.85083 39.493889,19.846943 39.519722,19.847221 39.535828,19.84972 39.54277,19.844166 39.551941,19.82333 39.576111,19.818333 39.581383,19.806389 39.590553,19.740276 39.624718,19.675831 39.674721,19.673332 39.677216,19.641388 39.744438,19.639999 39.75666,19.650833 39.772499,19.673054 39.793053,19.692219 39.794998,19.696941 39.794441,19.791664 39.790276,19.796665 39.79055,19.803886 39.793884,19.855 39.818329,19.864998 39.81694,19.926109 39.794441)),((19.404163 39.843605,19.393608 39.838333,19.388054 39.838608,19.384163 39.840271,19.377777 39.844719,19.37611 39.847771,19.377777 39.865555,19.378887 39.868607,19.381664 39.871109,19.422497 39.870277,19.427776 39.869995,19.430553 39.867493,19.428608 39.863884,19.404163 39.843605)))"
        val line = geo.toLineString("LINESTRING(1 2,3 4)")
        val len = geo.length(line)
        binding.sampleText.text = geo.buffer(wkt, 70000.0) + " " + pnt.toString() + " " + line.toString()
    }

}