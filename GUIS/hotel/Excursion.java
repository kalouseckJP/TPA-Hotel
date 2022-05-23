package hotel;

import java.util.Objects;

public class Excursion {
    private String tipo;
    private String descripcion;
    private int precio;


    public Excursion(String tipo, String descripcion, int precio){

        this.tipo=tipo;
        this.descripcion=descripcion;
        this.precio=precio;
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
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

        Excursion a = (Excursion) o;

        //Comparando entre valores
        return Objects.equals(tipo, a.tipo);
    }
    
}
