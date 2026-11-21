package logistics.validacion;

import logistics.modelo.Pedido;

/**
 * Eslabón de la cadena de validación (Chain of Responsibility).
 * Cada validador revisa una sola cosa. Si aprueba, le pasa el pedido al
 * siguiente; si rechaza, la cadena se detiene ahí con el motivo.
 * El último eslabón por defecto es la aprobación automática, así que no hace
 * falta preguntar si hay un siguiente.
 */
public abstract class Validador {

    private Validador siguiente = AprobacionAutomatica.INSTANCIA;

    /** Enlaza el siguiente eslabón y lo devuelve para poder encadenar llamadas. */
    public Validador enlazar(Validador siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public ResultadoValidacion validar(Pedido pedido) {
        ResultadoValidacion resultado = revisar(pedido);
        return resultado.aprobado() ? siguiente.validar(pedido) : resultado;
    }

    /** La regla propia de cada validador. */
    protected abstract ResultadoValidacion revisar(Pedido pedido);

    public String nombre() {
        return getClass().getSimpleName();
    }
}
