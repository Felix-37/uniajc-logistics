package logistics.validacion;

import java.util.HashMap;
import java.util.Map;

/** Unidades disponibles por SKU. Es un inventario en memoria para el prototipo. */
public class Inventario {

    private final Map<String, Integer> unidades = new HashMap<>();

    public Inventario agregar(String sku, int cantidad) {
        unidades.merge(sku, cantidad, Integer::sum);
        return this;
    }

    public int disponible(String sku) {
        return unidades.getOrDefault(sku, 0);
    }
}
