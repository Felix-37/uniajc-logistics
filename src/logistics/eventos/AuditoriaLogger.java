package logistics.eventos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Guarda en un historial la hora, el estado anterior y el estado nuevo de cada transición. */
public class AuditoriaLogger implements ObservadorPedido {

    /** Una línea del historial de auditoría. */
    public record Registro(Instant momento, String pedidoId, String estadoAnterior, String estadoNuevo) {
    }

    private final List<Registro> historial = new ArrayList<>();

    @Override
    public void alCambiarEstado(EventoPedido evento) {
        historial.add(new Registro(evento.momento(), evento.pedidoId(),
                evento.estadoAnterior().nombre(), evento.estadoNuevo().nombre()));
    }

    public List<Registro> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}
