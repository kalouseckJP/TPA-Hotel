package hotel;

import java.util.Objects;

public class Reserva {

    private float precio;
    private float total;
    private int  noches;
    int diaInicio;
    int mesInicio;
    int pasajeros;
    Cliente cliente;
    Habitacion habitacion;
    PlanMenu planMenu;
    Excursion excursion;

    public Reserva(float precio,int noches, int diaInicio, int mesInicio, Cliente cliente, int pasajeros, Habitacion habitacion, PlanMenu planMenu, Excursion excursion){
        this.pasajeros = pasajeros;
        this.noches = noches;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.planMenu = planMenu;
        this.excursion = excursion;
        this.precio = precio;
    }

    public Cliente getCliente(){
        return cliente;
    }
    public Habitacion getHabitacion(){
        return habitacion;
    }
    public PlanMenu getPlanMenu(){
        return planMenu;
    }
    public Excursion getExcursion(){
        return excursion;
    }
    public int getPasajeros(){
        return pasajeros;
    }
    public int getDiaInicio(){
        return diaInicio;
    }
    public int getMesInicio(){
        return mesInicio;
    }
    public float getTotal(float precio, int noches) {
        total = noches * precio;
        return total;
    }
    public float getPrecio() {
        return precio;
    }


    public void setPrecio(float precio) {
        this.precio = precio;
    }


    public int getNoches() {
        return noches;
    }


    public void setNoches(int noches) {
        this.noches = noches;
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

        Reserva a = (Reserva) o;

        //Comparando entre valores
        return Objects.equals(cliente, a.cliente);
    }
}