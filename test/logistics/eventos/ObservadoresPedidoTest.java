package logistics.eventos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import logistics.estado.Pagado;
import logistics.estado.TransicionInvalidaException;
import logistics.modelo.Cliente;
import logistics.modelo.Direccion;
import logistics.modelo.LineaPedido;
import logistics.modelo.Pedido;
import logistics.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObservadoresPedidoTest {

    private Pedido pedido;
    private ClienteNotificador notificador;
    private BodegaListener bodega;
    private AuditoriaLogger auditoria;

    @BeforeEach
    void preparar() {
        Cliente cliente = new Cliente("C1", "Ana", "ana@correo.com", true);
        Producto mouse = new Producto("P2", "Mouse", 40_000, 0.2);
        pedido = new Pedido("PED-1", cliente, List.of(new LineaPedido(mouse, 1)),
                new Direccion("Calle 5 # 10-20", "Cali", "Colombia"));

        notificador = new ClienteNotificador();
        bodega = new BodegaListener();
        auditoria = new AuditoriaLogger();
        pedido.suscribir(notificador);
        pedido.suscribir(auditoria);
        pedido.suscribirA(Pagado.class, bodega);
    }

    private void llevarHastaEntregado() {
        pedido.pagar();
        pedido.iniciarPreparacion();
        pedido.enviar();
        pedido.entregar();
    }

    @Test
    void cadaTransicionExitosaQuedaEnLaAuditoria() {
        llevarHastaEntregado();

        List<AuditoriaLogger.Registro> historial = auditoria.getHistorial();
        assertEquals(4, historial.size());
        assertEquals("Pendiente", historial.get(0).estadoAnterior());
        assertEquals("Pagado", historial.get(0).estadoNuevo());
        assertEquals("Enviado", historial.get(3).estadoAnterior());
        assertEquals("Entregado", historial.get(3).estadoNuevo());
        historial.forEach(registro -> assertEquals("PED-1", registro.pedidoId()));
        // Las marcas de tiempo quedan en orden.
        for (int i = 1; i < historial.size(); i++) {
            assertFalse(historial.get(i).momento().isBefore(historial.get(i - 1).momento()));
        }
    }

    @Test
    void elClienteRecibeUnMensajePorCadaCambioConElEstadoNuevo() {
        pedido.pagar();
        pedido.cancelar();

        assertEquals(List.of(
                "Para ana@correo.com: tu pedido PED-1 pasó a Pagado",
                "Para ana@correo.com: tu pedido PED-1 pasó a Cancelado"),
                notificador.getEnviados());
    }

    @Test
    void laBodegaSoloSeActivaCuandoElPedidoPasaAPagado() {
        llevarHastaEntregado();

        assertEquals(List.of("ALI-PED-1"), bodega.getOrdenesAlistamiento());
    }

    @Test
    void laBodegaNoSeActivaSiElPedidoSeCancelaSinPagar() {
        pedido.cancelar();

        assertTrue(bodega.getOrdenesAlistamiento().isEmpty());
        assertEquals(1, auditoria.getHistorial().size());
    }

    @Test
    void unaTransicionInvalidaNoEmiteEventos() {
        assertThrows(TransicionInvalidaException.class, pedido::enviar);

        assertTrue(notificador.getEnviados().isEmpty());
        assertTrue(auditoria.getHistorial().isEmpty());
        assertTrue(bodega.getOrdenesAlistamiento().isEmpty());
    }

    @Test
    void seAgregaUnSuscriptorNuevoSinModificarElPedido() {
        List<String> analitica = new ArrayList<>();
        pedido.suscribir(evento -> analitica.add(evento.estadoNuevo().nombre()));

        pedido.pagar();
        pedido.iniciarPreparacion();

        assertEquals(List.of("Pagado", "EnPreparacion"), analitica);
    }
}
