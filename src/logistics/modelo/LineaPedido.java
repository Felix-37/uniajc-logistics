package logistics.modelo;

/** Un producto con la cantidad que pide el cliente. */
public record LineaPedido(Producto producto, int cantidad) {

    public LineaPedido {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    public double subtotal() {
        return producto.precio() * cantidad;
    }

    public double pesoKg() {
        return producto.pesoKg() * cantidad;
    }
}
