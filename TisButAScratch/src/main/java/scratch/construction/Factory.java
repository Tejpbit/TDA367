package scratch.construction;

import scratch.construction.plugin.Pluggable;
import scratch.construction.plugin.PluginLoader;
import scratch.model.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alma on 4/26/14.
 * A generic class for all Factories which use plugins. A means to avoid duplicating
 * code. It contains A list with the type of objects it produces, a map for all plugins,
 * an abstract method for creating this object at a position
 */
public abstract class Factory<T> {
    private final List<T> givenTypeList;
    private final Map<Integer, Pluggable<?>> pluginMap = PluginLoader.loadPlugins();

    public Factory(){
        givenTypeList = new ArrayList<>();
    }

    public abstract T createType(int id, double x, double y);

    public Map<Integer, Pluggable<?>> getPluginMap() {
        return pluginMap;
    }

    public List<T> getGivenTypeList() {
        return givenTypeList;
    }
}
