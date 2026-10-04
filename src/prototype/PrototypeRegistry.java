package prototype;

import java.util.HashMap;
import java.util.Map;

public class PrototypeRegistry {
    private final Map<String, Prototype> codes = new HashMap<>();

    public void addCode(String name, Prototype prototype) {
        codes.put(name, prototype);
    }

    public void setCode(String name, Prototype prototype) {
        codes.put(name, prototype);
    }

    public Prototype getCode(String name) {
        return codes.get(name);
    }

    public Prototype createClone(String sourceName, String newName) {
        Prototype prototype = codes.get(sourceName);
        if (prototype == null) {
            throw new IllegalArgumentException("Prototype not found: " + sourceName);
        }
        Prototype clone = prototype.clone();
        codes.put(newName, clone);
        return clone;
    }
}
