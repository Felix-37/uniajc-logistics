package logistics.envio;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Envío internacional: tarifa según la zona geográfica (el país de destino)
 * más un recargo arancelario sobre el valor declarado de la mercancía.
 */
public class EnvioInternacional implements EstrategiaEnvio {

    private final Map<String, Double> tarifaPorPais;
    private final double recargoArancelario;

    /**
     * @param tarifaPorPais      tarifa fija por país de destino
     * @param recargoArancelario fracción del valor declarado (0,10 = 10 %)
     */
    public EnvioInternacional(Map<String, Double> tarifaPorPais, double recargoArancelario) {
        this.tarifaPorPais = tarifaPorPais.entrySet().stream()
                .collect(Collectors.toMap(e -> normalizar(e.getKey()), Map.Entry::getValue));
        this.recargoArancelario = recargoArancelario;
    }

    @Override
    public double calcularCosto(Paquete paquete) {
        double tarifaZona = Optional.ofNullable(tarifaPorPais.get(normalizar(paquete.paisDestino())))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay tarifa internacional hacia " + paquete.paisDestino()));
        return tarifaZona + paquete.valorDeclarado() * recargoArancelario;
    }

    @Override
    public int diasHabilesEstimados() {
        return 10;
    }

    @Override
    public String nombre() {
        return "Internacional";
    }

    private static String normalizar(String pais) {
        return pais.trim().toLowerCase(Locale.ROOT);
    }
}
