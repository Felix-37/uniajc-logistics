package logistics.validacion;

/**
 * Lo que devuelve la cadena de validación: si el pedido pasó y, si no,
 * qué validador lo rechazó y por qué.
 */
public record ResultadoValidacion(boolean aprobado, String validador, String motivo) {

    public static ResultadoValidacion aprobado(String validador) {
        return new ResultadoValidacion(true, validador, "");
    }

    public static ResultadoValidacion rechazado(String validador, String motivo) {
        return new ResultadoValidacion(false, validador, motivo);
    }
}
