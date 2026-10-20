package net.rptools.maptool.model.topology;

import net.rptools.maptool.model.topology.util.TopologyUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;

import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class MutableGeometry {
    private static final Logger log = LogManager.getLogger(MutableGeometry.class);

    private final Geometry source;
    private final AtomicReference<Geometry> geometry = new AtomicReference<>(null);
    private final AtomicReference<AffineTransformation> transformation = new AtomicReference<>(null);

    public MutableGeometry(Geometry source) {
        this.source = source;
        this.geometry.set(this.source);
    }

    public Geometry cumulativeTransform(AffineTransform xform) {
        return cumulativeTransform(List.of(xform), false);
    }

    public Geometry relativeCumulativeTransform(Iterable<AffineTransform> xforms) {
        return cumulativeTransform(xforms, true);
    }

    public Geometry transform(AffineTransform xform) {
        return transform(xform, false);
    }

    public Geometry cumulativeTransform(Iterable<AffineTransform> xforms, boolean relative) {
        try {
            return (Geometry) transformFuture(xforms, relative).get();
        } catch (InterruptedException|ExecutionException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Geometry transform(AffineTransform xform, boolean relative) {
        try {
           return (Geometry) transformFuture(xform, relative).get();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    protected Future<Object> transformFuture(Iterable<Object> xforms, boolean relative) {
        Iterator<Object> iterator = xforms.iterator();
        Future<Object> future = transformFuture(iterator.next(), relative);
        while(iterator.hasNext()){
            try {
                future.get();
                future = transformFuture(iterator.next(), true);
            } catch (InterruptedException|ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }
        return future;
    }
    protected Future<Object> transformFuture(AffineTransform xform, boolean relative) {
        return transformFuture(TopologyUtil.transformToTransformation.apply(xform), relative);
    }

    protected Future<Object> transformFuture(Object transform, boolean relative) throws IllegalArgumentException {
        Callable<Object> callable = transformCallable(transform, relative);
        if (relative) {
            return Constants.COMPLETION_SERVICE.submit(callable);
        } else {
            return Constants.THREAD_POOL.submit(callable);
        }
    }

    protected Callable<Object> transformCallable(Object transform, boolean relative) throws IllegalArgumentException {
        return () -> {
            AffineTransformation affineTransformation;
            if(transform instanceof AffineTransform at){
                affineTransformation = TopologyUtil.transformToTransformation.apply(at);
            } else if(transform instanceof AffineTransformation at){
                affineTransformation = at;
            } else {
                throw new IllegalArgumentException("Not a transform");
            }
            while (this.transformation.compareAndExchangeAcquire(null, affineTransformation) != null) {
                try {
                    wait(5);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }
            Geometry g;
            if (relative) {
                g = this.geometry.get();
            } else {
                g = this.source;
            }
            this.geometry.set(this.transformation.get().transform(g));
            this.transformation.setRelease(null);
            return this.geometry.get();
        };
    }
}
