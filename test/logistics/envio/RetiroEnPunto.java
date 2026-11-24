package logistics.envio;

/**
 * Modalidad nueva creada solo para la prueba del principio abierto/cerrado:
 * el cliente recoge el pedido en un punto físico y no paga envío.
 * Se agrega sin tocar Pedido ni las otras estrategias.
 */
class RetiroEnPunto implements EstrategiaEnvio {

    @Override
    public double calcularCosto(Paquete paquete) {
        return 0;
    }

    @Override
    public int diasHabilesEstimados() {
        return 2;
    }

    @Override
    public String nombre() {
        return "Retiro en punto";
    }
}
