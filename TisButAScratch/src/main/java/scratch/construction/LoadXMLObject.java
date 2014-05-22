package scratch.construction;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import scratch.model.GameCharacter;
import scratch.model.NpcType;
import scratch.model.weapons.Weapon;

import java.io.File;

/**
 * Created by Anna on 2014-05-18.
 */
public class LoadXMLObject {
	final static Serializer serializer = new Persister(new MyMatcher());

	public Object loadObject (String loadObject, String file){
		final StringBuilder fileBuild = new StringBuilder();
		if (file.trim().isEmpty()){
                    throw new RuntimeException("Tried to load file: " + file + ". It is not a proper filename");
                }
                fileBuild.append("res/");
		fileBuild.append(file.trim());
		fileBuild.append(".xml");
                System.out.println("Trying to load: " + fileBuild.toString() + "the obj: "+loadObject);
		final File source = new File(fileBuild.toString());
		Object object = new Object();
		try {
			if("NpcType".equals(loadObject)) {
				object = serializer.read(NpcType.class, source);
			}else if("GameCharacter".equals(loadObject)) {
				object = serializer.read(GameCharacter.class, source);
			}else if("Weapon".equals(loadObject)){
				object = serializer.read(Weapon.class, source);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}

}
