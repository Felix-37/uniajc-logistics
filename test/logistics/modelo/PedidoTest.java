package logistics.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class PedidoTest {

    private final Cliente cliente = new Cliente("C1", "Ana", "ana@correo.com", true);
    private final Direccion direccion = new Direccion("Calle 5 # 10-20", "Cali", "Colombia");

    @Test
    void calculaSubtotalYPesoDeTodasLasLineas() {
        Producto teclado = new Producto("P1", "Teclado", 80_000, 1.0);
        Producto mouse = new Producto("P2", "Mouse", 40_000, 0.5);
        Pedido pedido = new Pedido("PED-1", cliente,
                List.of(new LineaPedido(teclado, 2), new LineaPedido(mouse, 1)), direccion);

        assertEquals(200_000, pedido.subtotal(), 0.001);
        assertEquals(2.5, pedido.pesoTotalKg(), 0.001);
    }

    @Test
    void noPermitePedidoSinProductos() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido("PED-2", cliente, List.of(), direccion));
    }
}
