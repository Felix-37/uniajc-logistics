package logistics.validacion;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import logistics.modelo.Pedido;

/** Rechaza el pedido si no hay ruta logística hacia el país de destino. */
public class ValidadorCobertura extends Validador {

    private final Set<String> paisesConRuta;

    public ValidadorCobertura(Set<String> paisesConRuta) {
        this.paisesConRuta = paisesConRuta.stream()
                .map(ValidadorCobertura::normalizar)
                .collect(Collectors.toSet());
    }

    @Override
    protected ResultadoValidacion revisar(Pedido pedido) {
        String pais = pedido.getDireccion().pais();
        return paisesConRuta.contains(normalizar(pais))
                ? ResultadoValidacion.aprobado(nombre())
                : ResultadoValidacion.rechazado(nombre(), "No hay ruta logística hacia " + pais);
    }

    private static String normalizar(String pais) {
        return pais.trim().toLowerCase(Locale.ROOT);
    }
}
