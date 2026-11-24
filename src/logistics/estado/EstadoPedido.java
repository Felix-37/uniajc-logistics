package logistics.estado;

/**
 * Estado del ciclo de vida del pedido (patrón State).
 * Por defecto ninguna transición está permitida: cada estado concreto
 * sobrescribe solo las que acepta y devuelve el estado siguiente.
 * Así el pedido nunca pregunta en qué estado está.
 */
public abstract class EstadoPedido {

    public EstadoPedido pagar() {
        throw new TransicionInvalidaException("pagar", this);
    }

    public EstadoPedido iniciarPreparacion() {
        throw new TransicionInvalidaException("preparar", this);
    }

    public EstadoPedido enviar() {
        throw new TransicionInvalidaException("enviar", this);
    }

    public EstadoPedido entregar() {
        throw new TransicionInvalidaException("entregar", this);
    }

    public EstadoPedido cancelar() {
        throw new TransicionInvalidaException("cancelar", this);
    }

    /** Cambiar la modalidad de envío solo se permite antes del pago. */
    public void verificarCambioDeEnvio() {
        throw new TransicionInvalidaException("cambiar el envío de", this);
    }

    /** Descuentos y cambios de dirección solo se permiten antes de enviar. */
    public void verificarModificable() {
        throw new TransicionInvalidaException("modificar", this);
    }

    public String nombre() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return nombre();
    }
}
