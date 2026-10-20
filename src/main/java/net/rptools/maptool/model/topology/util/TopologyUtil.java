package net.rptools.maptool.model.topology.util;

import net.rptools.maptool.model.topology.Constants;
import net.rptools.maptool.server.proto.geometry.GeometryDto;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.AffineTransformation;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

public class TopologyUtil {

    //    public static final PolygonHullSimplifier HULL_SIMPLIFIER = new PolygonHullSimplifier();


    public static MultiPolygon newMultiPolygon() {
        return Constants.FACTORY.createMultiPolygon();
    }
    public static synchronized MultiPolygon createMultiPolygon(Geometry geometry) {
        if(geometry instanceof MultiPolygon mp){
            return (MultiPolygon) Constants.FACTORY.createGeometry(mp);
        } else if(geometry.isEmpty()){
            return Constants.FACTORY.createMultiPolygon();
        } else if(geometry instanceof Polygon polygon){
            return Constants.FACTORY.createMultiPolygon(new Polygon[]{polygon});
        } else if(geometry instanceof LinearRing ring){
            return Constants.FACTORY.createMultiPolygon(new Polygon[]{Constants.FACTORY.createPolygon(ring.getCoordinates())});
        } else if(geometry instanceof GeometryCollection collection){
            List<Geometry> geometries = new ArrayList<>();
            for (int i = 0; i < collection.getNumGeometries(); i++) {
                geometries.add(collection.getGeometryN(i));
            }
            return Constants.FACTORY.createMultiPolygon(
                    geometries.stream()
                            .map(g -> (Polygon) g.buffer(0))
                            .toArray(Polygon[]::new)
            );
        } else {
            return Constants.FACTORY.createMultiPolygon(new Polygon[]{(Polygon)geometry.buffer(0)});
        }
    }
    /** Uses AT matrix to build a AffineTransformation */
    public static Function<AffineTransform, AffineTransformation> transformToTransformation = (AffineTransform at) ->{
        double[] matrix = new double[6];
        at.getMatrix(matrix);
        return new AffineTransformation(matrix[0], matrix[2], matrix[4], matrix[1], matrix[3], matrix[5]);
    };
    /** Uses AT matrices to build a Vector of AffineTransformations */
    public static Function<Iterable<AffineTransform>, Vector<AffineTransformation>> transformsToTransformations = (Iterable<AffineTransform> ats) ->{
        double[] matrix = new double[6];
        Vector<AffineTransformation> out = new Vector<>();
        ats.forEach(at -> {
            at.getMatrix(matrix);
            out.add(new AffineTransformation(matrix[0], matrix[2], matrix[4], matrix[1], matrix[3], matrix[5]));
        });
        return out;
    };

    public Geometry fromDto(GeometryDto dto){
        Geometry geometry = Constants.WKB_READER.read(dto.getGeometry())
    }
}
