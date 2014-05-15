package scratch.model;

import java.awt.geom.Rectangle2D;
import java.util.Properties;

public class InteractiveObject implements IInteractiveObject {

    private String name, type;
    private Rectangle2D.Double rect;
    private Properties properties;

    public InteractiveObject(String name, String type, int x, int y, int width, int height, Properties properties) {
        this.name = name;
        this.type = type;
        this.rect = new Rectangle2D.Double(x, y, width, height);
        this.properties = properties;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Rectangle2D.Double getArea() {
        return rect;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}