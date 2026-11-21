package logistics.validacion;

import logistics.modelo.Pedido;

/** Final de la cadena: si el pedido llegó hasta aquí, queda aprobado. */
final class AprobacionAutomatica extends Validador {

    static final AprobacionAutomatica INSTANCIA = new AprobacionAutomatica();

    private AprobacionAutomatica() {
    }

    @Override
    public ResultadoValidacion validar(Pedido pedido) {
        return revisar(pedido);
    }

    @Override
    protected ResultadoValidacion revisar(Pedido pedido) {
        return ResultadoValidacion.aprobado(nombre());
    }
}
