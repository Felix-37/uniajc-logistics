package logistics.eventos;

import java.time.Instant;
import logistics.estado.EstadoPedido;

/**
 * Evento que emite el pedido después de cada transición exitosa.
 * Lleva solo los datos que necesitan los suscriptores, así el paquete
 * eventos no depende del modelo y no hay dependencia circular.
 */
public record EventoPedido(
        String pedidoId,
        String correoCliente,
        EstadoPedido estadoAnterior,
        EstadoPedido estadoNuevo,
        Instant momento) {
}
