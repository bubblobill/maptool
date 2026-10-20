package net.rptools.maptool.model.topology;

import org.locationtech.jts.awt.ShapeReader;
import org.locationtech.jts.awt.ShapeWriter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.util.GeometricShapeFactory;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.rptools.maptool.model.Grid.MAX_GRID_SIZE;

public class Constants {
    public static final PrecisionModel PRECISION_MODEL = new PrecisionModel(MAX_GRID_SIZE);
    public static final GeometryFactory FACTORY = new GeometryFactory(PRECISION_MODEL);
    public static final GeometricShapeFactory SHAPE_FACTORY = new GeometricShapeFactory(FACTORY);
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    public static final ExecutorCompletionService<Object> COMPLETION_SERVICE = new ExecutorCompletionService<>(THREAD_POOL);
    public static final WKBReader WKB_READER = new WKBReader(FACTORY);
    public static final WKBWriter WKB_WRITER = new WKBWriter();
    public static final ShapeReader SHAPE_READER = new ShapeReader(FACTORY);
    public static final ShapeWriter SHAPE_WRITER = new ShapeWriter();

    public enum Facing {
      OCEAN_SIDE_FACES_ORIGIN,
      ISLAND_SIDE_FACES_ORIGIN
    }

    public enum TopologyType {
        WALL,
        HILL,
        PIT,
        COVER,
        MOVEMENT;
    }

    public enum VisibilityType {
      Sight,
      Light,
      Aura
    }

    public enum VisionResult {
      Possible,
      CompletelyObscured
    }
}
