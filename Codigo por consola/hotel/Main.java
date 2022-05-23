package hotel;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Cliente> clts = new ArrayList<Cliente>();
    static ArrayList<Habitacion> rooms = new ArrayList<Habitacion>();
    static ArrayList<PlanMenu> plans = new ArrayList<PlanMenu>();
    static ArrayList<Excursion> excs = new ArrayList<Excursion>();
    static ArrayList<Reserva> resers = new ArrayList<Reserva>();

    public static void main(String[] args) {
        boolean ejecutando = true;
        int eleccion;
        listaHabitaciones();
        listaPlan();
        listaExcursion();

        /////CREACION Y REVISION CLIENTES/////
        try {
            File nuevoArchivo = new File("hotel/Datos Clientes.csv");
            if (nuevoArchivo.createNewFile()) {
                System.out.println("Archivo Creado: " + nuevoArchivo.getName());
                try (FileWriter escritor = new FileWriter("hotel/Datos Clientes.csv", true)) {
                    escritor.write("Nombre,Apellido,Rut");
                    escritor.close();
                }
            }
            Scanner lector = new Scanner(nuevoArchivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String[] valor = linea.split(",");
                if (!valor[0].equals((String) "Nombre")) {
                    String nombre = valor[0], apellido = valor[1], rut = valor[2];
                    Cliente cliente = new Cliente(rut, nombre, apellido);
                    clts.add(cliente);
                }
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("Ocurrió un error en la creación del archivo");
            e.printStackTrace();
        }
        /////CREACION Y REVISION CLIENTES/////

        ///// CREACION Y REVISION DE RESERVAS /////
        try {
            File nuevoArchivo = new File("hotel/reservas.csv");
            if (nuevoArchivo.createNewFile()) {
                System.out.println("Archivo Creado: " + nuevoArchivo.getName());
                try (FileWriter escritor = new FileWriter("hotel/reservas.csv")) {
                    escritor.write(
                            "Nombre,Apellido,Rut,Pasajeros,Dia Inicio,Mes Inicio,Habitacion,Plan Menu,Excursion,Noches");
                    escritor.close();
                }
            } else {
                Scanner lector = new Scanner(nuevoArchivo);
                while (lector.hasNextLine()) {
                    String linea = lector.nextLine();
                    String[] valor = linea.split(",");
                    Habitacion habitacion = rooms.get(0);
                    for (int i = 0; i < rooms.size(); i++) {
                        habitacion = rooms.get(i);
                        if (habitacion.getTipo().equals(valor[6])) {
                            habitacion.setDisponible(habitacion.getDisponible() - 1);
                        }
                        rooms.remove(i);
                        rooms.add(i, habitacion);
                    }
                    if (!valor[0].equals("Nombre")) {
                        int noches = Integer.parseInt(valor[9]), diaInicio = Integer.parseInt(valor[4]),
                                mesInicio = Integer.parseInt(valor[5]), pasajeros = Integer.parseInt(valor[3]);
                        Cliente cliente;
                        cliente = clts.get(0);
                        for (int i = 0; !valor[2].equals(cliente.getRut()); i++) {
                            cliente = clts.get(i);
                        }
                        // Habitacion habitacion;
                        habitacion = rooms.get(0);
                        for (int i = 0; !habitacion.getTipo().equals(valor[6]); i++) {
                            habitacion = rooms.get(i);
                        }
                        PlanMenu planMenu;
                        planMenu = plans.get(0);
                        for (int i = 0; !planMenu.getTipo().equals(valor[7]); i++) {
                            planMenu = plans.get(i);
                        }
                        Excursion excursion;
                        excursion = excs.get(0);
                        for (int i = 0; !excursion.getTipo().equals(valor[8]); i++) {
                            excursion = excs.get(i);
                        }
                        float precio = (habitacion.getPrecio() + planMenu.getPrecio() + excursion.getPrecio())
                                * noches;
                        Reserva reserva = new Reserva(precio, noches, diaInicio, mesInicio, cliente, pasajeros,
                                habitacion, planMenu, excursion);
                        resers.add(reserva);
                    }
                }
                lector.close();
            }
        }catch(IOException e){
            System.out.println("Ocurrió un error en la creación del archivo");
            e.printStackTrace();
        }
        ///// CREACION Y REVISION DE RESERVAS /////


        //// MAIN ////
        while (ejecutando) {
            System.out.println("Opciones:\n1. Crear Cliente.\n2. Crear Reserva.\n3. Cobrar Reserva.\n4. Cerrar.");
            eleccion = sc.nextInt();
            do {
                switch (eleccion) {
                    case 1:
                        crearCliente();
                        break;
                    case 2:
                        cReser();
                        break;
                    case 3:
                        System.out.println("Ingrese rut del reservante:");
                        sc.nextLine();
                        String rut = sc.nextLine();
                        Cliente actual = clts.get(0);
                        for (int i = 0; clts.size() > i; i++) {
                            if (!actual.getRut().equals(rut)) {
                                actual = clts.get(i);
                            }
                        }
                        if (actual.getRut().equals(rut)) {
                            Reserva reserva = resers.get(0);
                            for(int i = 0;i < resers.size();i++){
                                reserva = resers.get(i);
                                if(!reserva.getCliente().getRut().equals(actual.getRut())){
                                    File reservasFile = new File("hotel/tempReservas.csv");
                                    try {
                                        if(reservasFile.createNewFile()){
                                            FileWriter reservasWriter = new FileWriter(reservasFile,true);
                                            reservasWriter.write("Nombre,Apellido,Rut,Pasajeros,Dia Inicio,Mes Inicio,Habitacion,Plan Menu,Excursion,Noches");
                                            reservasWriter.close();
                                        }
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    Scanner lectorReservas = new Scanner("hotel/tempReservas.csv");
                                    while(lectorReservas.hasNextLine()){ 
                                        try {
                                            FileWriter reservasWriter = new FileWriter(reservasFile,true);
                                            reservasWriter.append("\n" + reserva.getCliente().getName() + ","
                                                + reserva.getCliente().getApellido() + ","
                                                + reserva.getCliente().getRut()
                                                + "," + reserva.getPasajeros() + "," + reserva.getDiaInicio() + ","
                                                + reserva.getMesInicio() + "," + reserva.getHabitacion().getTipo()
                                                + ","
                                                + reserva.getPlanMenu().getTipo() + ","
                                                + reserva.getExcursion().getTipo()
                                                + "," + reserva.getNoches());
                                            reservasWriter.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        lectorReservas.nextLine();
                                    }lectorReservas.close();
                                }
                            }
                            File reservaBorrar = new File("hotel/reservas.csv");
                            reservaBorrar.delete();
                            File reservasFile = new File("hotel/tempReservas.csv");
                            reservasFile.renameTo(reservaBorrar);
                            if(reserva.getCliente().getRut().equals(rut)){
                                for(int i = 0; i < resers.size(); i++){
                                    if(actual.getRut().equals(reserva.getCliente().getRut())){
                                        resers.remove(i);
                                    }
                                }
                                Pulsera pulsera = new Pulsera(reserva.getPrecio(),actual);
                                System.out.println("\nPulsera de "+pulsera.getCliente().getName()+" "+pulsera.getCliente().getApellido()+" a sido entregada");
                            }else{
                                System.out.println("Este cliente no tiene una reserva\n");
                            }
                        } else {
                            System.out.println("No tenemos un cliente resgistrado a este rut.");
                        }
                        break;
                    case 4:
                        ejecutando = false;
                        break;
                    default:
                        break;
                }
            } while (eleccion < 0 || eleccion > 5);
        }
        sc.close();
        //// MAIN ////
    }

    private static void crearCliente() {
        sc.nextLine();
        System.out.println("Ingrese su nombre");
        String nombre = sc.nextLine();
        nombre = Cliente.nomComp(nombre);
        System.out.println("Ingrese su apellido");
        String apellido = sc.nextLine();
        apellido = Cliente.apeComp(apellido);
        System.out.println("Ingrese su rut");
        String rut = sc.nextLine();
        rut = Cliente.rutComp(rut);
        

        File fCliente = new File("hotel/Datos Clientes.csv");
        Cliente c1 = new Cliente(rut, nombre, apellido);
        Cliente dupe = clts.get(0);
        boolean cd = false;
        for(int i = 0; i<clts.size();i++){
            dupe = clts.get(i);
            if(dupe.getRut().equals(c1.getRut())){
                cd=true;
                System.out.println("\nEste rut ya esta registrado.");
            }
        }
        clts.add(c1);
        if(!cd){
            try {
                Scanner lector = new Scanner(fCliente);
                try {
                    FileWriter escritor = new FileWriter("hotel/Datos Clientes.csv", true);
                    escritor.append("\n" + c1.getName() + "," + c1.getApellido() + "," + c1.getRut());
                    escritor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lector.close();
                } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void cReser(){
        System.out.println("Ingrese rut del reservante:");
        sc.nextLine();
        String rut = sc.nextLine();
        Cliente actual = clts.get(0);
        for (int i = 0; clts.size() > i; i++) {
            if (!actual.getRut().equals(rut)) {
                actual = clts.get(i);
            }
        }
        if (actual.getRut().equals(rut)) {
            System.out.println("Cliente: " + actual.getName() + " " + actual.getApellido()
                    + "\nQue dia desea iniciar su reserva:");
            int diaInicio;
            do {
                diaInicio = sc.nextInt();
            } while (diaInicio < 0 || diaInicio > 31);
            System.out.println("del mes:");
            int mesInicio;
            do {
                mesInicio = sc.nextInt();
            } while (mesInicio < 1 || mesInicio > 12);
            System.out.println("Cantidad de pasajeros, no puede ser más de 8:");
            int nPasajeros;
            do {
                nPasajeros = sc.nextInt();
            } while (nPasajeros > 8 || nPasajeros < 1);
            selecHab(diaInicio, mesInicio, actual, nPasajeros);
        } else {
            System.out.println("No tenemos un cliente resgistrado a este rut.");
        }
    }

    private static void listaHabitaciones() {
        Habitacion ejecutivaIndividual = new Habitacion("Ejecutiva Individual", 50000, 2, 9);
        rooms.add(ejecutivaIndividual);
        Habitacion ejecutivaDoble = new Habitacion("Ejecutivo Doble", 80000, 4, 10);
        rooms.add(ejecutivaDoble);
        Habitacion familiar = new Habitacion("Familiar", 150000, 8, 10);
        rooms.add(familiar);
        Habitacion pentHouse = new Habitacion("Pent-House", 1080000, 2, 2);
        rooms.add(pentHouse);
    }

    private static void listaPlan() {
        PlanMenu inicial = new PlanMenu("Inicial", "Incluye el plato principal de una comida (almuerzo o cena) del menu diario a gusto del chef.", 10000);
        plans.add(inicial);
        PlanMenu intermedio = new PlanMenu("Intermedio","Incluye una comida (almuerzo o cena) de tres tiempos (entrada, fondo y postre) del menu diario a gusto del chef.",25000);
        plans.add(intermedio);
        PlanMenu completo = new PlanMenu("Completo","Incluye dos comidas, almuerzo y cena, de tres tiempos (entrada, fondo y postre) del menu diario a gusto del chef.",45000);
        plans.add(completo);
        PlanMenu avanzado = new PlanMenu("Avanzado","Incluye dos comidas, almuerzo y cena, de tres tiempos (entrada, fondo y postre) del menu abierto de especialidad del chef.",60000);
        plans.add(avanzado);
        PlanMenu premium = new PlanMenu("Premium","Incluye tiempo de chef dedicado a todo momento para satisfacer los gustos exclusivos y peticiones especificas de los pasajeros para una cantidad no determinada de comidas al dia.",100000);
        plans.add(premium);
        PlanMenu mNinguno = new PlanMenu("Ninguno", "", 0);
        plans.add(mNinguno);
    }

    private static void listaExcursion(){
        Excursion light = new Excursion("Light", "Corresponde a una excursion de tipo caminata de 6 horas en total por senderos de complejidad baja con hermosos lugares de vegetacion nativa y afluentes de agua, ideal para grupos familiares con ninos o personas de 3ra edad (inclusive para personas con dificultades metrices)", 5000);
        excs.add(light);
        Excursion plus = new Excursion("Plus", "Corresponde a una excursion de tipo hiking de 3 dias en total por una cadena montanosa, experiencia de campamento y contemplacion de glaciares y cascadas, ideal para grupos de personas con capacidades fisicas compatibles con la exigencia de la caminata", 25000);
        excs.add(plus);
        Excursion heavy = new Excursion("Heavy", "Corresponde a una excursion de tipo hiking de 5 dias en total por una cadena montanosa y con navegacion en afluentes locales. Se incluyen actividades extremas de Rapel, Canopi, Rafting y Escalada. Las actividades requieren de capacidades fisicas compatibles con la complejidad de la excursion.", 50000);
        excs.add(heavy);
        Excursion eNinguno = new Excursion("Ninguno", "", 0);
        excs.add(eNinguno);
    }
    
    private static void selecHab(int diaInicio, int mesInicio, Cliente cliente,int pasajeros) {
        System.out.println("Selecciones su habitacion\n1. Ejecutiva Individual, Capacidad: 2\n2. Ejecutiva Doble, Capacidad: 4\n3. Familiar, Capacidad: 8\n4. Pent-House, Capacidad: 2");
        int hab = sc.nextInt();
        Habitacion habitacion;
        do {
            switch (hab) {
                case 1:
                    habitacion = rooms.get(0);
                    compHab(diaInicio, mesInicio, cliente, pasajeros, habitacion);
                    break;
                case 2:
                    habitacion = rooms.get(1);
                    compHab(diaInicio, mesInicio, cliente, pasajeros, habitacion);
                    break;
                case 3:
                    habitacion = rooms.get(2);
                    compHab(diaInicio, mesInicio, cliente, pasajeros, habitacion);
                    break;
                case 4:
                    habitacion = rooms.get(3);
                    compHab(diaInicio, mesInicio, cliente, pasajeros, habitacion);
                    break;
                default:
                    System.out.print("Seleccione una opcion valida");
                    break;
            }
        } while (hab < 1 || hab > 4);
    }

    private static void compHab(int diaInicio, int mesInicio, Cliente cliente, int pasajeros, Habitacion habitacion){
        if(pasajeros>habitacion.getCapacidad()){
            System.out.println("Usted no cumple con los requisitos de capacidad");
        }else if(habitacion.getDisponible()<=0){
                System.out.println("Lo sentimos no hay habitaciones disponibles");
        }else{
            selecPlan(diaInicio,mesInicio,cliente,pasajeros,habitacion);
        }
    }

    private static void selecPlan(int diaInicio, int mesInicio, Cliente cliente,int pasajeros, Habitacion habitacion) {
        System.out.println("El precio de momento es: "+ habitacion.getPrecio()+ "\n");
        System.out.println("\nSeleccione un plan de menus.\n1. Inicial.\n2. Intermedio. \n3. Completo.\n4. Avanzado.\n5. Premium.\n6.No gracias\n");
        int plan = sc.nextInt();
        PlanMenu planMenu;
        do {
            switch (plan) {
                case 1:
                    planMenu = plans.get(0);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                case 2:
                    planMenu = plans.get(1);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                case 3:
                    planMenu = plans.get(2);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                case 4:
                    planMenu = plans.get(3);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                case 5:
                    planMenu = plans.get(4);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                case 6:
                    planMenu = plans.get(5);
                    selecEx(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu);
                    break;
                default:
                    break;
            }
        } while (plan < 1 || plan > 6);
    }

    private static void selecEx(int diaInicio, int mesInicio, Cliente cliente, int pasajeros, Habitacion habitacion, PlanMenu planMenu){
        System.out.println("El precio de momento es: "+ (habitacion.getPrecio()+planMenu.getPrecio()) + "\n");
        System.out.println("Seleccione un plan de excursion.\n1. Light.\n2. Plus.\n3. Heavy.\n4. No gracias\n");
        int exc = sc.nextInt();
        Excursion excursion;
        do {
            switch (exc) {
                case 1:
                    excursion = excs.get(0);
                    crearReser(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu, excursion);
                    break;
                case 2:
                    excursion = excs.get(1);
                    crearReser(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu, excursion);
                    break;
                case 3:
                    excursion = excs.get(2);
                    crearReser(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu, excursion);
                    break;
                case 4:
                    excursion = excs.get(3);
                    crearReser(diaInicio,mesInicio,cliente,pasajeros,habitacion, planMenu, excursion);
                    break;
                default:
                    break;
            }
        } while (exc < 1 || exc > 4);
    }

    private static void crearReser(int diaInicio, int mesInicio, Cliente cliente, int pasajeros, Habitacion habitacion,PlanMenu planMenu, Excursion excursion) {
        int a = (habitacion.getPrecio() + planMenu.getPrecio() + excursion.getPrecio());
        System.out.println("El precio de momento es: " + a + "\n");
        System.out.println("Ingrese la cantidad de noches:");
        int noches = sc.nextInt();
        System.out.println("El precio final es: " + a * noches);
        Reserva reserva = new Reserva(a * noches, noches, diaInicio, mesInicio, cliente, pasajeros, habitacion,
                planMenu, excursion);
        resers.add(reserva);

        try {
            File bd = new File("hotel/reservas.csv");
            Scanner lector = new Scanner(bd);
            if (lector.hasNextLine()) {
                try {
                    FileWriter escritor = new FileWriter("hotel/reservas.csv", true);
                    System.out.print(
                            "El valor Total es: $" + reserva.getPrecio() + "\n Desea continuar?\n1.si\n2.no\n");
                    int opcion = sc.nextInt();
                    switch (opcion) {
                        case 1:
                            escritor.append("\n" + reserva.getCliente().getName() + ","
                                    + reserva.getCliente().getApellido() + "," + reserva.getCliente().getRut()
                                    + "," + reserva.getPasajeros() + "," + reserva.getDiaInicio() + ","
                                    + reserva.getMesInicio() + "," + reserva.getHabitacion().getTipo() + ","
                                    + reserva.getPlanMenu().getTipo() + "," + reserva.getExcursion().getTipo()
                                    + "," + reserva.getNoches());
                            escritor.close();
                            System.out.println("La escritura del archivo fue realizada con exito!!!");
                            break;
                        case 2:
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("Ocurrió un error en la escritura del archivo");
                    e.printStackTrace();
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Archivo no encontrado!!!");
            e.printStackTrace();
        }
    }
}
