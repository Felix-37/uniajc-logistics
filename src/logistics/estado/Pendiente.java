package logistics.estado;

/** Pedido aceptado que todavía no se ha pagado. */
public class Pendiente extends EstadoPedido {

    @Override
    public EstadoPedido pagar() {
        return new Pagado();
    }

    @Override
    public EstadoPedido cancelar() {
        return new Cancelado();
    }

    @Override
    public void verificarCambioDeEnvio() {
        // Antes del pago se puede cambiar la modalidad de envío.
    }

    @Override
    public void verificarModificable() {
        // Se puede modificar.
    }
}
