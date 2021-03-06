package scratch.construction;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;
import scratch.model.INPCMove;

import java.awt.geom.Rectangle2D;

/**
 * Created by Anna on 2014-05-08.
 */
public class MyMatcher implements Matcher {

    @Override
    public Transform match(Class type) {
        if (type.equals(INPCMove.class)) {
            return new INPCMoveTransformer();
        }
        if (type.equals(Rectangle2D.Double.class)) {
            return new RectangleTransformer();
        }
        return null;
    }
}
