package logistics.validacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logistics.modelo.Pedido;

/**
 * Arma la cadena a partir de una lista de validadores, en el orden dado.
 * Para agregar, quitar o reordenar filtros solo se cambia la lista.
 * Guarda cada rechazo con su motivo exacto.
 */
public class CadenaValidacion {

    private final Validador primero;
    private final List<ResultadoValidacion> rechazos = new ArrayList<>();

    public CadenaValidacion(List<Validador> validadores) {
        if (validadores.isEmpty()) {
            throw new IllegalArgumentException("La cadena necesita al menos un validador");
        }
        primero = validadores.get(0);
        for (int i = 1; i < validadores.size(); i++) {
            validadores.get(i - 1).enlazar(validadores.get(i));
        }
    }

    public ResultadoValidacion validar(Pedido pedido) {
        ResultadoValidacion resultado = primero.validar(pedido);
        if (!resultado.aprobado()) {
            rechazos.add(resultado);
        }
        return resultado;
    }

    public List<ResultadoValidacion> getRechazos() {
        return Collections.unmodifiableList(rechazos);
    }
}
