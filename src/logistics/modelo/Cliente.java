package logistics.modelo;

/**
 * Cliente que hace el pedido.
 * tieneHistorial indica si ya compró antes; lo usa el validador de fraude.
 */
public record Cliente(String id, String nombre, String correo, boolean tieneHistorial) {
}
