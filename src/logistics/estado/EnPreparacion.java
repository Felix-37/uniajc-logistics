package logistics.estado;

/** La bodega está alistando el pedido. Todavía se puede cancelar. */
public class EnPreparacion extends EstadoPedido {

    @Override
    public EstadoPedido enviar() {
        return new Enviado();
    }

    @Override
    public EstadoPedido cancelar() {
        return new Cancelado();
    }

    @Override
    public void verificarModificable() {
        // Se puede modificar mientras no se haya enviado.
    }
}
