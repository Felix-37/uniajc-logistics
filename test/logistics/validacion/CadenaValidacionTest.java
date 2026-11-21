package logistics.validacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import logistics.modelo.Cliente;
import logistics.modelo.Direccion;
import logistics.modelo.LineaPedido;
import logistics.modelo.Pedido;
import logistics.modelo.Producto;
import org.junit.jupiter.api.Test;

class CadenaValidacionTest {

    private static final double UMBRAL_FRAUDE = 1_000_000;

    private final Producto portatil = new Producto("P1", "Portátil", 2_500_000, 2.0);
    private final Producto mouse = new Producto("P2", "Mouse", 40_000, 0.2);
    private final Cliente clienteConHistorial = new Cliente("C1", "Ana", "ana@correo.com", true);
    private final Cliente clienteNuevo = new Cliente("C2", "Luis", "luis@correo.com", false);
    private final Direccion cali = new Direccion("Calle 5 # 10-20", "Cali", "Colombia");
    private final Direccion pekin = new Direccion("Wangfujing 1", "Pekín", "China");

    private final Inventario inventario = new Inventario().agregar("P1", 5).agregar("P2", 10);

    private CadenaValidacion cadenaCompleta() {
        return new CadenaValidacion(List.of(
                new ValidadorStock(inventario),
                new ValidadorFraude(UMBRAL_FRAUDE),
                new ValidadorCobertura(Set.of("Colombia", "Ecuador"))));
    }

    private Pedido pedido(Cliente cliente, Direccion destino, LineaPedido... lineas) {
        return new Pedido("PED-1", cliente, List.of(lineas), destino);
    }

    @Test
    void pedidoValidoPasaTodaLaCadenaYQuedaAprobado() {
        ResultadoValidacion resultado = cadenaCompleta()
                .validar(pedido(clienteConHistorial, cali, new LineaPedido(portatil, 1)));

        assertTrue(resultado.aprobado());
        assertEquals("AprobacionAutomatica", resultado.validador());
    }

    @Test
    void rechazaPorStockYDiceElMotivo() {
        CadenaValidacion cadena = cadenaCompleta();
        ResultadoValidacion resultado = cadena
                .validar(pedido(clienteConHistorial, cali, new LineaPedido(mouse, 11)));

        assertFalse(resultado.aprobado());
        assertEquals("ValidadorStock", resultado.validador());
        assertEquals("Sin stock suficiente de P2: pide 11, hay 10", resultado.motivo());
        assertEquals(List.of(resultado), cadena.getRechazos());
    }

    @Test
    void rechazaPorFraudeAClienteSinHistorialConMontoAlto() {
        ResultadoValidacion resultado = cadenaCompleta()
                .validar(pedido(clienteNuevo, cali, new LineaPedido(portatil, 1)));

        assertFalse(resultado.aprobado());
        assertEquals("ValidadorFraude", resultado.validador());
    }

    @Test
    void elMismoMontoPasaSiElClienteTieneHistorial() {
        ResultadoValidacion resultado = cadenaCompleta()
                .validar(pedido(clienteConHistorial, cali, new LineaPedido(portatil, 1)));

        assertTrue(resultado.aprobado());
    }

    @Test
    void rechazaPorCoberturaSiNoHayRutaAlDestino() {
        ResultadoValidacion resultado = cadenaCompleta()
                .validar(pedido(clienteConHistorial, pekin, new LineaPedido(mouse, 1)));

        assertFalse(resultado.aprobado());
        assertEquals("ValidadorCobertura", resultado.validador());
        assertEquals("No hay ruta logística hacia China", resultado.motivo());
    }

    @Test
    void laCadenaSeDetieneEnElPrimerRechazo() {
        ValidadorEspia espia = new ValidadorEspia();
        CadenaValidacion cadena = new CadenaValidacion(List.of(
                new ValidadorStock(inventario), espia));

        cadena.validar(pedido(clienteConHistorial, cali, new LineaPedido(mouse, 99)));

        assertEquals(0, espia.llamadas);
    }

    @Test
    void seAgregaUnFiltroNuevoSinTocarLosDemas() {
        ValidadorEspia espia = new ValidadorEspia();
        CadenaValidacion cadena = new CadenaValidacion(List.of(
                new ValidadorStock(inventario), new ValidadorFraude(UMBRAL_FRAUDE), espia));

        ResultadoValidacion resultado = cadena
                .validar(pedido(clienteConHistorial, cali, new LineaPedido(mouse, 1)));

        assertTrue(resultado.aprobado());
        assertEquals(1, espia.llamadas);
    }

    /** Validador de prueba que solo cuenta cuántas veces lo llaman. */
    private static class ValidadorEspia extends Validador {
        int llamadas;

        @Override
        protected ResultadoValidacion revisar(Pedido pedido) {
            llamadas++;
            return ResultadoValidacion.aprobado(nombre());
        }
    }
}
