package logistics.estado;

/** Excepción de dominio: se pidió una transición que el estado actual no permite. */
public class TransicionInvalidaException extends RuntimeException {

    public TransicionInvalidaException(String accion, EstadoPedido estado) {
        super("No se puede " + accion + " un pedido en estado " + estado.nombre());
    }
}
