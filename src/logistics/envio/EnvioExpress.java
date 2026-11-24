package logistics.envio;

/**
 * Envío express: la tarifa base se multiplica por 1,8, se suma el costo por
 * kilo y el pedido sale con prioridad de despacho.
 */
public class EnvioExpress implements EstrategiaEnvio {

    public static final double FACTOR_TARIFA_BASE = 1.8;

    private final double tarifaBase;
    private final double costoPorKg;

    public EnvioExpress() {
        this(EnvioEstandar.TARIFA_BASE, EnvioEstandar.COSTO_POR_KG);
    }

    public EnvioExpress(double tarifaBase, double costoPorKg) {
        this.tarifaBase = tarifaBase;
        this.costoPorKg = costoPorKg;
    }

    @Override
    public double calcularCosto(Paquete paquete) {
        return tarifaBase * FACTOR_TARIFA_BASE + paquete.pesoKg() * costoPorKg;
    }

    @Override
    public int diasHabilesEstimados() {
        return 1;
    }

    @Override
    public boolean despachoPrioritario() {
        return true;
    }

    @Override
    public String nombre() {
        return "Express";
    }
}
