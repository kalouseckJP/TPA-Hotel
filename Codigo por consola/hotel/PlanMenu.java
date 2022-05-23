package hotel;

import java.util.Objects;

public class PlanMenu {

    private String tipo;
    private String detalles;
    private int precio;

    public PlanMenu(String tipo, String detalles, int precio){
        this.tipo = tipo;
        this.detalles = detalles;
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }
    public int getPrecio() {
        return precio;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public String getDetalles() {
        return detalles;
    }
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o){
        // Compara con el objeto o con una instancia
        if(this == o)
            return true;
        // Omitir los objetos nulos
        if(o == null)
            return false;

        //validamos la clase del objeto entregado
        if(getClass() != o.getClass())
            return false;

        PlanMenu a = (PlanMenu) o;

        //Comparando entre valores
        return Objects.equals(tipo, a.tipo);
    }
}
