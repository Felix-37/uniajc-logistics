package logistics.envio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import logistics.estado.TransicionInvalidaException;
import logistics.modelo.Cliente;
import logistics.modelo.Direccion;
import logistics.modelo.LineaPedido;
import logistics.modelo.Pedido;
import logistics.modelo.Producto;
import org.junit.jupiter.api.Test;

class EstrategiaEnvioTest {

    // Subtotal 200.000 y peso 2,5 kg en todos los casos.
    private Pedido nuevoPedido(String pais) {
        Cliente cliente = new Cliente("C1", "Ana", "ana@correo.com", true);
        Producto teclado = new Producto("P1", "Teclado", 80_000, 1.0);
        Producto mouse = new Producto("P2", "Mouse", 40_000, 0.5);
        return new Pedido("PED-1", cliente,
                List.of(new LineaPedido(teclado, 2), new LineaPedido(mouse, 1)),
                new Direccion("Calle 5 # 10-20", "Cali", pais));
    }

    private final EnvioInternacional internacional =
            new EnvioInternacional(Map.of("Ecuador", 35_000.0, "Perú", 45_000.0), 0.10);

    @Test
    void estandarCobraTarifaBaseMasCostoPorPeso() {
        Pedido pedido = nuevoPedido("Colombia");

        // 8.000 + 2,5 kg x 2.000
        assertEquals(13_000, pedido.costoEnvio(), 0.001);
        assertEquals(4, pedido.getEnvio().diasHabilesEstimados());
        assertFalse(pedido.getEnvio().despachoPrioritario());
    }

    @Test
    void expressMultiplicaLaTarifaBaseYTienePrioridad() {
        Pedido pedido = nuevoPedido("Colombia");
        pedido.cambiarEnvio(new EnvioExpress());

        // 8.000 x 1,8 + 2,5 kg x 2.000
        assertEquals(19_400, pedido.costoEnvio(), 0.001);
        assertTrue(pedido.getEnvio().despachoPrioritario());
    }

    @Test
    void internacionalCobraTarifaDeZonaMasRecargoArancelario() {
        Pedido pedido = nuevoPedido("Ecuador");
        pedido.cambiarEnvio(internacional);

        // 35.000 de la zona + 10 % de 200.000
        assertEquals(55_000, pedido.costoEnvio(), 0.001);
    }

    @Test
    void internacionalRechazaUnPaisSinTarifa() {
        Pedido pedido = nuevoPedido("Japón");
        pedido.cambiarEnvio(internacional);

        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, pedido::costoEnvio);
        assertEquals("No hay tarifa internacional hacia Japón", error.getMessage());
    }

    @Test
    void cambiarLaEstrategiaEnEjecucionRecalculaElTotal() {
        Pedido pedido = nuevoPedido("Colombia");
        assertEquals(213_000, pedido.total(), 0.001);

        pedido.cambiarEnvio(new EnvioExpress());
        assertEquals(219_400, pedido.total(), 0.001);

        pedido.cambiarEnvio(new EnvioEstandar());
        assertEquals(213_000, pedido.total(), 0.001);
    }

    @Test
    void noSePuedeCambiarElEnvioDespuesDelPago() {
        Pedido pedido = nuevoPedido("Colombia");
        EstrategiaEnvio elegida = pedido.getEnvio();
        pedido.pagar();

        assertThrows(TransicionInvalidaException.class,
                () -> pedido.cambiarEnvio(new EnvioExpress()));
        assertSame(elegida, pedido.getEnvio());
        assertEquals(213_000, pedido.total(), 0.001);
    }

    @Test
    void unaModalidadNuevaFuncionaSinModificarLasClasesExistentes() {
        Pedido pedido = nuevoPedido("Colombia");
        pedido.cambiarEnvio(new RetiroEnPunto());

        assertEquals(0, pedido.costoEnvio(), 0.001);
        assertEquals(200_000, pedido.total(), 0.001);
        assertEquals("Retiro en punto", pedido.getEnvio().nombre());
    }
}
