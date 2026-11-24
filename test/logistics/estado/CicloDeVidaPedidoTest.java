package logistics.estado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.Consumer;
import logistics.modelo.Cliente;
import logistics.modelo.Direccion;
import logistics.modelo.LineaPedido;
import logistics.modelo.Pedido;
import logistics.modelo.Producto;
import org.junit.jupiter.api.Test;

class CicloDeVidaPedidoTest {

    private Pedido nuevoPedido() {
        Cliente cliente = new Cliente("C1", "Ana", "ana@correo.com", true);
        Direccion direccion = new Direccion("Calle 5 # 10-20", "Cali", "Colombia");
        Producto mouse = new Producto("P2", "Mouse", 40_000, 0.2);
        return new Pedido("PED-1", cliente, List.of(new LineaPedido(mouse, 1)), direccion);
    }

    @Test
    void recorreElCicloCompletoHastaEntregado() {
        Pedido pedido = nuevoPedido();
        assertInstanceOf(Pendiente.class, pedido.getEstado());

        pedido.pagar();
        assertInstanceOf(Pagado.class, pedido.getEstado());
        pedido.iniciarPreparacion();
        assertInstanceOf(EnPreparacion.class, pedido.getEstado());
        pedido.enviar();
        assertInstanceOf(Enviado.class, pedido.getEstado());
        pedido.entregar();
        assertInstanceOf(Entregado.class, pedido.getEstado());
    }

    @Test
    void noPasaAEnPreparacionSiNoEstaPagado() {
        Pedido pedido = nuevoPedido();

        TransicionInvalidaException error =
                assertThrows(TransicionInvalidaException.class, pedido::iniciarPreparacion);

        assertEquals("No se puede preparar un pedido en estado Pendiente", error.getMessage());
        assertInstanceOf(Pendiente.class, pedido.getEstado());
    }

    @Test
    void seCancelaDesdePendientePagadoYEnPreparacion() {
        List<Consumer<Pedido>> llevarAlEstado = List.of(
                p -> { },
                Pedido::pagar,
                p -> { p.pagar(); p.iniciarPreparacion(); });

        for (Consumer<Pedido> pasos : llevarAlEstado) {
            Pedido pedido = nuevoPedido();
            pasos.accept(pedido);
            pedido.cancelar();
            assertInstanceOf(Cancelado.class, pedido.getEstado());
        }
    }

    @Test
    void cancelarUnPedidoEnviadoLanzaExcepcionDeDominio() {
        Pedido pedido = nuevoPedido();
        pedido.pagar();
        pedido.iniciarPreparacion();
        pedido.enviar();

        TransicionInvalidaException error =
                assertThrows(TransicionInvalidaException.class, pedido::cancelar);

        assertEquals("No se puede cancelar un pedido en estado Enviado", error.getMessage());
        assertInstanceOf(Enviado.class, pedido.getEstado());
    }

    @Test
    void entregadoEsTerminalYNoAdmiteNingunaAccion() {
        Pedido pedido = nuevoPedido();
        pedido.pagar();
        pedido.iniciarPreparacion();
        pedido.enviar();
        pedido.entregar();

        assertThrows(TransicionInvalidaException.class, pedido::pagar);
        assertThrows(TransicionInvalidaException.class, pedido::iniciarPreparacion);
        assertThrows(TransicionInvalidaException.class, pedido::enviar);
        assertThrows(TransicionInvalidaException.class, pedido::entregar);
        assertThrows(TransicionInvalidaException.class, pedido::cancelar);
        assertThrows(TransicionInvalidaException.class,
                () -> pedido.getEstado().verificarModificable());
        assertInstanceOf(Entregado.class, pedido.getEstado());
    }

    @Test
    void soloSeCambiaElEnvioAntesDelPago() {
        Pedido pedido = nuevoPedido();
        pedido.getEstado().verificarCambioDeEnvio();

        pedido.pagar();

        assertThrows(TransicionInvalidaException.class,
                () -> pedido.getEstado().verificarCambioDeEnvio());
    }
}
