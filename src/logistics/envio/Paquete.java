package logistics.envio;

/**
 * Lo que una estrategia necesita saber del pedido para cotizar el envío.
 * Así el paquete envio no depende del modelo y no hay dependencia circular
 * entre Pedido y las estrategias.
 */
public record Paquete(double pesoKg, double valorDeclarado, String paisDestino) {
}
