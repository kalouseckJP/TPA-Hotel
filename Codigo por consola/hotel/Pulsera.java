package hotel;

    public class Pulsera {
    private float total;
    Cliente cliente;

    public Pulsera(float total, Cliente cliente){
        this.total=total;
        this.cliente = cliente;
    }

    public float getTotal() {
        return total;
    }
    public Cliente getCliente(){
        return cliente;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
