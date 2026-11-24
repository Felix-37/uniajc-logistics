package logistics.estado;

/**
 * El pedido salió de bodega. Solo puede pasar a Entregado.
 * Cancelarlo lanza TransicionInvalidaException (comportamiento heredado).
 */
public class Enviado extends EstadoPedido {

    @Override
    public EstadoPedido entregar() {
        return new Entregado();
    }
}
