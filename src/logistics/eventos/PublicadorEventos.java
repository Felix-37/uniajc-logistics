package logistics.eventos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logistics.estado.EstadoPedido;

/**
 * Sujeto del patrón Observer. Un suscriptor puede escuchar todas las
 * transiciones o solo las que llegan a un estado concreto. La selección se
 * hace con un mapa por clase de estado, así ningún suscriptor necesita
 * preguntar con un if a qué estado llegó el pedido.
 */
public class PublicadorEventos {

    private final List<ObservadorPedido> deTodos = new ArrayList<>();
    private final Map<Class<? extends EstadoPedido>, List<ObservadorPedido>> porEstado = new HashMap<>();

    public void suscribir(ObservadorPedido observador) {
        deTodos.add(observador);
    }

    public void suscribirA(Class<? extends EstadoPedido> estado, ObservadorPedido observador) {
        porEstado.computeIfAbsent(estado, clave -> new ArrayList<>()).add(observador);
    }

    public void publicar(EventoPedido evento) {
        deTodos.forEach(observador -> observador.alCambiarEstado(evento));
        porEstado.getOrDefault(evento.estadoNuevo().getClass(), List.of())
                .forEach(observador -> observador.alCambiarEstado(evento));
    }
}
