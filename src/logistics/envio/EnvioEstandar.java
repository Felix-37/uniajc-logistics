package logistics.envio;

/** Envío nacional estándar: tarifa base fija más un costo por kilo. */
public class EnvioEstandar implements EstrategiaEnvio {

    public static final double TARIFA_BASE = 8_000;
    public static final double COSTO_POR_KG = 2_000;

    private final double tarifaBase;
    private final double costoPorKg;

    public EnvioEstandar() {
        this(TARIFA_BASE, COSTO_POR_KG);
    }

    public EnvioEstandar(double tarifaBase, double costoPorKg) {
        this.tarifaBase = tarifaBase;
        this.costoPorKg = costoPorKg;
    }

    @Override
    public double calcularCosto(Paquete paquete) {
        return tarifaBase + paquete.pesoKg() * costoPorKg;
    }

    @Override
    public int diasHabilesEstimados() {
        return 4;
    }

    @Override
    public String nombre() {
        return "Estándar";
    }
}
