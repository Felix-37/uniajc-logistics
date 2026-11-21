package logistics.validacion;

import logistics.modelo.Pedido;

/**
 * Rechaza el pedido cuando el monto supera el umbral sospechoso y el cliente
 * no tiene compras anteriores.
 */
public class ValidadorFraude extends Validador {

    private final double umbral;

    public ValidadorFraude(double umbral) {
        this.umbral = umbral;
    }

    @Override
    protected ResultadoValidacion revisar(Pedido pedido) {
        boolean sospechoso = pedido.subtotal() > umbral && !pedido.getCliente().tieneHistorial();
        return sospechoso
                ? ResultadoValidacion.rechazado(nombre(), "Monto de " + pedido.subtotal()
                        + " supera el umbral de " + umbral + " y el cliente no tiene historial")
                : ResultadoValidacion.aprobado(nombre());
    }
}
