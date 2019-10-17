package frc.lib5k.utils;

public class Measurement{

    static final double MM = 1;
    static final double CM = 10;
    static final double M = 1000;
    static final double IN = 25.4;
    static final double FT = 304.8;
    static final double YD = 914.4;

    public static double convert(double value, double units_from, double units_to){
        return (value * units_from)/units_to;
    };
}