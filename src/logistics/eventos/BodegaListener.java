package logistics.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Genera la orden de alistamiento para la bodega. Se suscribe solo al
 * estado Pagado (ver Pedido.suscribirA), así que no revisa el estado.
 */
public class BodegaListener implements ObservadorPedido {

    private final List<String> ordenesAlistamiento = new ArrayList<>();

    @Override
    public void alCambiarEstado(EventoPedido evento) {
        ordenesAlistamiento.add("ALI-" + evento.pedidoId());
    }

    public List<String> getOrdenesAlistamiento() {
        return Collections.unmodifiableList(ordenesAlistamiento);
    }
}
