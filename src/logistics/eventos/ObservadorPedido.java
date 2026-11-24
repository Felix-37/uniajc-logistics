package logistics.eventos;

/** Suscriptor de los cambios de estado del pedido (patrón Observer). */
@FunctionalInterface
public interface ObservadorPedido {

    void alCambiarEstado(EventoPedido evento);
}
