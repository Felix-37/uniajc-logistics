package logistics.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simula el correo o el mensaje push que recibe el cliente con el nuevo
 * estado. En el prototipo los mensajes se guardan en una lista.
 */
public class ClienteNotificador implements ObservadorPedido {

    private final List<String> enviados = new ArrayList<>();

    @Override
    public void alCambiarEstado(EventoPedido evento) {
        enviados.add("Para " + evento.correoCliente() + ": tu pedido " + evento.pedidoId()
                + " pasó a " + evento.estadoNuevo().nombre());
    }

    public List<String> getEnviados() {
        return Collections.unmodifiableList(enviados);
    }
}
