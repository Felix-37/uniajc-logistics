package logistics.validacion;

import logistics.modelo.LineaPedido;
import logistics.modelo.Pedido;

/** Rechaza el pedido si algún producto no tiene unidades suficientes. */
public class ValidadorStock extends Validador {

    private final Inventario inventario;

    public ValidadorStock(Inventario inventario) {
        this.inventario = inventario;
    }

    @Override
    protected ResultadoValidacion revisar(Pedido pedido) {
        return pedido.getLineas().stream()
                .filter(linea -> inventario.disponible(linea.producto().sku()) < linea.cantidad())
                .findFirst()
                .map(this::sinStock)
                .orElse(ResultadoValidacion.aprobado(nombre()));
    }

    private ResultadoValidacion sinStock(LineaPedido linea) {
        String sku = linea.producto().sku();
        return ResultadoValidacion.rechazado(nombre(), "Sin stock suficiente de " + sku
                + ": pide " + linea.cantidad() + ", hay " + inventario.disponible(sku));
    }
}
