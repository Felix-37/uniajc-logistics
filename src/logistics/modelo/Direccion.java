package logistics.modelo;

/** Dirección de entrega. El país y la ciudad sirven para validar la cobertura. */
public record Direccion(String detalle, String ciudad, String pais) {

    @Override
    public String toString() {
        return detalle + ", " + ciudad + ", " + pais;
    }
}
