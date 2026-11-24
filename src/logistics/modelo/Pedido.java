package logistics.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logistics.estado.EstadoPedido;
import logistics.estado.Pendiente;

/**
 * Pedido de la plataforma. Es el contexto del patrón State: delega cada
 * transición en su estado actual y se queda con el estado que este devuelve.
 * En los siguientes commits se le agregan la estrategia de envío (Strategy)
 * y los observadores (Observer).
 */
public class Pedido {

    private final String id;
    private final Cliente cliente;
    private final List<LineaPedido> lineas;
    private Direccion direccion;
    private double descuento;
    private EstadoPedido estado = new Pendiente();

    public Pedido(String id, Cliente cliente, List<LineaPedido> lineas, Direccion direccion) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto");
        }
        this.id = id;
        this.cliente = cliente;
        this.lineas = new ArrayList<>(lineas);
        this.direccion = direccion;
    }

    public void pagar() {
        transicionar(estado.pagar());
    }

    public void iniciarPreparacion() {
        transicionar(estado.iniciarPreparacion());
    }

    public void enviar() {
        transicionar(estado.enviar());
    }

    public void entregar() {
        transicionar(estado.entregar());
    }

    public void cancelar() {
        transicionar(estado.cancelar());
    }

    private void transicionar(EstadoPedido nuevo) {
        estado = nuevo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public double subtotal() {
        return lineas.stream().mapToDouble(LineaPedido::subtotal).sum();
    }

    public double pesoTotalKg() {
        return lineas.stream().mapToDouble(LineaPedido::pesoKg).sum();
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<LineaPedido> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
