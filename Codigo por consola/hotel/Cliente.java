package hotel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Objects;

public class Cliente {
    String rut;
    String nombre;
    String apellido;
    static Scanner sc = new Scanner(System.in);
    
    public Cliente(String rut, String nombre, String apellido){
        setRut(rut);
        setNombre(nombre);
        setApellido(apellido);
    }


    public String getRut(){
        return this.rut;
    }
    public String getName(){
        return this.nombre;
    }
    public String getApellido(){
        return this.apellido;
    }

    public void setRut(String rut){
        this.rut = rut;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setApellido(String apellido){
        this.apellido = apellido;
    }

    static public String rutComp(String rut) {
        boolean valido;
        do{
            String regex = "^[1-9]\\d{6,7}$";
            Pattern p = Pattern.compile(regex);
            if(rut == null){
                valido = false;
            }
            Matcher m = p.matcher(rut);
            valido = m.matches();
            if(!valido){
                System.out.println("\nIngrese un rut valido:");
                rut = sc.nextLine();
            }
        }while(!valido);
        return rut;
    }

    static public String nomComp(String nombre){
        boolean valido;
        do{
            String regex = "^[A-Za-z]\\w{2,29}$";
            Pattern p = Pattern.compile(regex);
            if (nombre == null) {
                valido = false;
            }
            Matcher m = p.matcher(nombre);
            String regex2 = ".*[0-9].*";
            Pattern p2 = Pattern.compile(regex2);
            Matcher m2 = p2.matcher(nombre);
            valido = m.matches();
            if(m.matches()){
                valido = !m2.matches();
            }
            if(!valido){
                System.out.println("\nIngrese un nombre valido:");
                nombre = sc.nextLine();
            }
        }while(!valido);
        return nombre;
    }

    static public String apeComp(String apellido){
        boolean valido;
        do{
            String regex = "^[A-Za-z]\\w{2,29}$";
            Pattern p = Pattern.compile(regex);
            if (apellido == null) {
                valido = false;
            }
            Matcher m = p.matcher(apellido);
            String regex2 = ".*[0-9].*";
            Pattern p2 = Pattern.compile(regex2);
            Matcher m2 = p2.matcher(apellido);
            valido = m.matches();
            if(m.matches()){
                valido = !m2.matches();
            }
            if(!valido){
                System.out.println("\nIngrese un apellido valido:");
                apellido = sc.nextLine();
            }
        }while(!valido);
        return apellido;
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

        Cliente a = (Cliente) o;

        //Comparando entre valores
        return Objects.equals(rut, a.rut);
    }
}
