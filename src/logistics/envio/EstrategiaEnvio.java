package logistics.envio;

/**
 * Modalidad de envío (patrón Strategy). Cada modalidad calcula su propia
 * tarifa; el pedido solo guarda la estrategia elegida y le pide el costo,
 * sin preguntar de qué tipo es. Para agregar una modalidad nueva basta con
 * crear otra clase que implemente esta interfaz.
 */
public interface EstrategiaEnvio {

    double calcularCosto(Paquete paquete);

    int diasHabilesEstimados();

    String nombre();

    /** Por defecto el pedido sale en el turno normal de despacho. */
    default boolean despachoPrioritario() {
        return false;
    }
}
