package logistics.estado;

/** Pedido pagado. Es el único estado desde el que se puede empezar a preparar. */
public class Pagado extends EstadoPedido {

    @Override
    public EstadoPedido iniciarPreparacion() {
        return new EnPreparacion();
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
