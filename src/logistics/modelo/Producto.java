package logistics.modelo;

/** Producto del catálogo. El peso se usa para calcular el envío. */
public record Producto(String sku, String nombre, double precio, double pesoKg) {

    public Producto {
        if (precio < 0 || pesoKg < 0) {
            throw new IllegalArgumentException("El precio y el peso no pueden ser negativos");
        }
    }
}
