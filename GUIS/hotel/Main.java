package hotel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class Main extends Application{
    public static void main(String[]args){
        
        listaHabitaciones();
        listaPlan();
        listaExcursion(); 

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
            }else {
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
        
        launch(args);
    }

    Stage ventana;
    boolean veEi=false;
    boolean veEd=false;
    boolean veFa=false;
    boolean vePe=false;

    boolean veM1=false;
    boolean veM2=false;
    boolean veM3=false;
    boolean veM4=false;
    boolean veM5=false;

    boolean veE1=false;
    boolean veE2=false;
    boolean veE3=false;


    static ArrayList<Cliente> clts = new ArrayList<Cliente>();
    static ArrayList<Habitacion> rooms = new ArrayList<Habitacion>();
    static ArrayList<PlanMenu> plans = new ArrayList<PlanMenu>();
    static ArrayList<Excursion> excs = new ArrayList<Excursion>();
    static ArrayList<Reserva> resers = new ArrayList<Reserva>();

    private static void compPanel(TextField nombre, TextField apellido, GridPane panelCliente, TextField RUT,
            Stage ventana, Scene temInicio, GridPane inicio) {
        boolean nomValido;
        Label nomInval = new Label();
        nomInval.setText("Ingrese un nombre valido");
        GridPane.setColumnSpan(nomInval, 10);
        GridPane.setConstraints(nomInval, 2, 1);
        panelCliente.getChildren().removeAll(nomInval);
        if (Cliente.nomComp(nombre.getText())) {
            nomValido = true;
        } else {
            panelCliente.getChildren().add(nomInval);
            nomValido = false;
        }

        boolean apeValido;
        Label apeInval = new Label("Ingrese un apellido valido");
        GridPane.setColumnSpan(apeInval, 10);
        GridPane.setConstraints(apeInval, 2, 4);
        panelCliente.getChildren().removeAll(apeInval);
        if (Cliente.apeComp(apellido.getText())) {
            apeValido = true;
        } else {
            panelCliente.getChildren().add(apeInval);
            apeValido = false;
        }

        boolean rutValido;
        Label rutInval = new Label();
        rutInval.setText("Ingrese un rut valido");
        GridPane.setColumnSpan(rutInval, 10);
        GridPane.setConstraints(rutInval, 2, 7);
        if (Cliente.rutComp(RUT.getText())) {
            rutValido = true;
        } else {
            panelCliente.getChildren().add(rutInval);
            rutValido = false;
        }

        File fCliente = new File("hotel/Datos Clientes.csv");
        Cliente c1 = new Cliente(RUT.getText(), nombre.getText(), apellido.getText());
        Cliente dupe = clts.get(0);
        boolean cd = false;
        for (int i = 0; i < clts.size(); i++) {
            dupe = clts.get(i);
            if (dupe.getRut().equals(c1.getRut())) {
                cd = true;
                Label cDupe = new Label("Este rut ya esta registrado.");
                GridPane.setColumnSpan(cDupe, 8);
                GridPane.setConstraints(cDupe, 1, 9);
                panelCliente.getChildren().add(cDupe);
            }
        }

        if (nomValido && rutValido && apeValido && !cd) {

            Label cSuccess = new Label("Cliente registrado exitosamente.");
            GridPane.setColumnSpan(cSuccess, 8);
            GridPane.setConstraints(cSuccess, 1, 2);
            inicio.getChildren().add(cSuccess);

            clts.add(c1);
            if (!cd) {
                try {
                    Scanner lector = new Scanner(fCliente);
                    try {
                        FileWriter escritor = new FileWriter("hotel/Datos Clientes.csv", true);
                        escritor.append("\n" + c1.getName() + "," + c1.getApellido() + "," + c1.getRut());
                        escritor.close();
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                    lector.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            panelCliente.getChildren().clear();

            nombre.setText("");
            apellido.setText("");
            RUT.setText("");

            ventana.setScene(temInicio);
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
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        ventana = primaryStage;
        ventana.setTitle("Hotel");        
        
        GridPane panelInicio = new GridPane();
        GridPane panelCliente = new GridPane();
        GridPane panelReserva = new GridPane();
        GridPane panelDB = new GridPane();
        GridPane panelHabitaciones = new GridPane();
        GridPane panelMenu = new GridPane();
        GridPane panelExcursiones = new GridPane();
        GridPane panelFinalReserva = new GridPane();
        GridPane panelCobrar = new GridPane();
        
        Scene temInicio = new Scene(panelInicio, 400 , 300);
        Scene cionCliente = new Scene(panelCliente, 400, 300);
        Scene cionReserva = new Scene(panelReserva, 400,300);
        Scene datosBasicos = new Scene(panelDB, 400, 300);
        Scene temHabitaciones = new Scene(panelHabitaciones,400,300);
        Scene temMenu = new Scene(panelMenu,400,300);
        Scene temExcursiones = new Scene(panelExcursiones,400,300);
        Scene temFinalReserva = new Scene(panelFinalReserva,400,300);
        Scene temCobrar = new Scene(panelCobrar,400,300);
        ventana.setScene(temInicio);
        
        ///// MENU INICIO /////

        panelInicio.setHgap(20);
        panelInicio.setVgap(10);
        panelInicio.setPadding(new Insets(10, 10, 10, 10));
        
        Label bienvenidaHotel = new Label();
        bienvenidaHotel.setText("Bienvenidos al Hotel");
        bienvenidaHotel.setPadding(new Insets(0, 0, 0, 0));
        GridPane.setColumnSpan(bienvenidaHotel, 10);
        GridPane.setConstraints(bienvenidaHotel, 1, 0);

        Button crearCliente = new Button("Registrar Cliente");
        GridPane.setConstraints(crearCliente, 0, 2);

        Button crearReserva = new Button("Registrar reserva");
        GridPane.setConstraints(crearReserva, 0, 4);

        Button cobrarReserva = new Button("Cobrar Reserva");
        GridPane.setConstraints(cobrarReserva, 0, 6);

        panelInicio.getChildren().addAll(bienvenidaHotel,crearCliente,crearReserva,cobrarReserva);

        ///// MENU INICIO /////

        ////// CREACION CLIENTE //////

        panelCliente.setHgap(20);
        panelCliente.setVgap(10);
        panelCliente.setPadding(new Insets(10, 10, 10, 10));

        Label ingNombre = new Label();
        ingNombre.setText("Ingrese su nombre:");
        GridPane.setColumnSpan(ingNombre, 2);
        GridPane.setConstraints(ingNombre, 0,1);
        TextField nombre = new TextField();
        nombre.setPromptText("Nombre");
        GridPane.setColumnSpan(nombre, 15);
        GridPane.setConstraints(nombre, 0, 2);

        Label ingApellido = new Label();
        ingApellido.setText("Ingrese su apellido:");
        GridPane.setColumnSpan(ingApellido, 2);
        GridPane.setConstraints(ingApellido, 0,4);
        TextField apellido = new TextField();
        apellido.setPromptText("Apellido");
        GridPane.setColumnSpan(apellido, 15);
        GridPane.setConstraints(apellido, 0, 5);

        Label ingRut = new Label();
        ingRut.setText("Ingrese su RUT:");
        GridPane.setConstraints(ingRut, 0,7);
        TextField RUT = new TextField();
        RUT.setPromptText("Sin Puntos ni Guion");
        GridPane.setColumnSpan(RUT, 15);
        GridPane.setConstraints(RUT, 0, 8);

        Button finalizar = new Button("Finalizar");
        GridPane.setColumnSpan(finalizar, 8);
        GridPane.setConstraints(finalizar, 7, 10);

        Button atras1 = new Button("Atras");
        GridPane.setColumnSpan(atras1, 8);
        GridPane.setConstraints(atras1, 1, 10);
        
        finalizar.setOnAction(e ->{
            compPanel(nombre, apellido, panelCliente, RUT, ventana, temInicio, panelInicio);
        });

        atras1.setOnAction(e ->{
            panelCliente.getChildren().clear();
            ventana.setScene(temInicio);
            nombre.setText("");
            apellido.setText("");
            RUT.setText("");
        });
        
        ////// CREACION CLIENTE //////
        
        panelCobrar.setHgap(20);
        panelCobrar.setVgap(10);
        panelCobrar.setPadding(new Insets(10, 10, 10, 10));

        Label ingRut3 = new Label("Ingrese su rut");
        GridPane.setColumnSpan(ingRut3, 2);
        GridPane.setConstraints(ingRut3, 0, 1);
        TextField rut3 = new TextField();
        rut3.setPromptText("Rut sin punto ni guion");
        GridPane.setColumnSpan(rut3, 15);
        GridPane.setConstraints(rut3, 0, 2);

        Button continuar = new Button("Siguiente");
        GridPane.setColumnSpan(continuar, 2);
        GridPane.setConstraints(continuar, 5, 9);

        continuar.setOnAction(e -> {
            Cliente actual = clts.get(0);
            for (int i = 0; clts.size() > i; i++) {
                if (!actual.getRut().equals(rut3.getText())) {
                    actual = clts.get(i);
                }
            }
            if (actual.getRut().equals(rut3.getText())) {
                Reserva reserva = resers.get(0);
                for (int i = 0; i < resers.size(); i++) {
                    reserva = resers.get(i);
                    if (!reserva.getCliente().getRut().equals(actual.getRut())) {
                        File reservasFile = new File("hotel/tempReservas.csv");
                        try {
                            if (reservasFile.createNewFile()) {
                                FileWriter reservasWriter = new FileWriter(reservasFile, true);
                                reservasWriter.write(
                                        "Nombre,Apellido,Rut,Pasajeros,Dia Inicio,Mes Inicio,Habitacion,Plan Menu,Excursion,Noches");
                                reservasWriter.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Scanner lectorReservas = new Scanner("hotel/tempReservas.csv");
                        while (lectorReservas.hasNextLine()) {
                            try {
                                FileWriter reservasWriter = new FileWriter(reservasFile, true);
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
                            } catch (IOException error) {
                                error.printStackTrace();
                            }
                            lectorReservas.nextLine();
                        }
                        lectorReservas.close();
                    }
                }
                File reservaBorrar = new File("hotel/reservas.csv");
                reservaBorrar.delete();
                File reservasFile = new File("hotel/tempReservas.csv");
                reservasFile.renameTo(reservaBorrar);
                if (reserva.getCliente().getRut().equals(rut3.getText())) {
                    for (int i = 0; i < resers.size(); i++) {
                        if (actual.getRut().equals(reserva.getCliente().getRut())) {
                            resers.remove(i);
                        }
                    }
                    Pulsera pulsera = new Pulsera(reserva.getPrecio(), actual);
                    System.out.println("\nPulsera de " + pulsera.getCliente().getName() + " "
                            + pulsera.getCliente().getApellido() + " a sido entregada");
                    panelCobrar.getChildren().clear();
                    ventana.setScene(temInicio);
                } else {
                    Label noReserva = new Label("No tenemos una reserva a este rut");
                    GridPane.setColumnSpan(noReserva, 5);
                    GridPane.setConstraints(noReserva, 0, 3);

                    panelCobrar.getChildren().add(noReserva);
                }
            } else {
                Label noReserva = new Label("No tenemos este rut registrado");
                GridPane.setColumnSpan(noReserva, 5);
                GridPane.setConstraints(noReserva, 0, 3);

                panelCobrar.getChildren().add(noReserva);
            }
        });

        ////// CREACION RESERVA //////
        
        

        ////// CREACION RESERVA //////

        ////// COBRAR RESERVA //////

        panelReserva.setHgap(20);
        panelReserva.setVgap(10);
        panelReserva.setPadding(new Insets(10, 10, 10, 10));

        Label ingRut2 = new Label("Ingrese su rut.");
        GridPane.setConstraints(ingRut2, 0, 1);
        TextField rut2 = new TextField();
        rut2.setPromptText("Rut");
        GridPane.setColumnSpan(rut2, 15);
        GridPane.setConstraints(rut2, 0, 2);
        
        Button siguiente = new Button("Siguiente");
        GridPane.setColumnSpan(siguiente, 4);
        GridPane.setConstraints(siguiente, 9, 20);
 
        siguiente.setOnAction(e->{
            boolean rutValido;
            Label rutInval = new Label();
            rutInval.setText("Ingrese un rut valido");
            GridPane.setColumnSpan(rutInval, 10);
            GridPane.setConstraints(rutInval, 2, 7);

            if (Cliente.rutComp(rut2.getText())) {
                rutValido = true;
            } else {
                panelReserva.getChildren().add(rutInval);
                rutValido = false;
            }
            
            if(rutValido){
                Cliente actual = clts.get(0);
                for (int i = 0; clts.size() > i; i++) {
                    if (!actual.getRut().equals(rut2.getText())) {
                        actual = clts.get(i);
                    }
                }
                if (actual.getRut().equals(rut2.getText())) {
                    panelReserva.getChildren().clear();
                    ventana.setScene(datosBasicos);
                    datosBasicos(panelDB,actual,temFinalReserva,panelHabitaciones,temHabitaciones, panelMenu,temMenu,temExcursiones,panelExcursiones,panelFinalReserva,temInicio,panelInicio);
                    // selecHab(diaInicio, mesInicio, actual, nPasajeros);
                } else {
                    Label noExiste = new Label("No tenemos un cliente registrado a este rut");
                    GridPane.setColumnSpan(noExiste, 10);
                    GridPane.setConstraints(noExiste, 0, 4);
                    panelReserva.getChildren().add(noExiste);
                }
            }
        });

        ////// COBRAR RESERVA //////

        /////////////// BOTONES ///////////////

        crearCliente.setOnAction(e -> {
            ventana.setScene(cionCliente);
            panelCliente.getChildren().addAll(ingNombre,nombre,ingApellido,apellido,ingRut,RUT,finalizar,atras1);
        });

        crearReserva.setOnAction(e ->{
            ventana.setScene(cionReserva);
            panelReserva.getChildren().addAll(ingRut2,rut2,siguiente);
        });

        cobrarReserva.setOnAction(e->{
            ventana.setScene(temCobrar);
            panelCobrar.getChildren().addAll(ingRut3,rut3,continuar);
        });
        /////////////// BOTONES ///////////////

        ventana.show();
    }

   
        
    private void datosBasicos(GridPane panelDB, Cliente actual, Scene temFinalReserva, GridPane panelHabitaciones,Scene temHabitaciones, GridPane panelMenu, Scene temMenu, Scene temExcursiones, GridPane panelExcursiones, GridPane panelFinalReserva, Scene temInicio, GridPane panelInicio) {
        ////// DATOS BASICOS //////
        
        panelDB.setHgap(20);
        panelDB.setVgap(10);
        panelDB.setPadding(new Insets(10, 10, 10, 10));

        Label cliente = new Label("Cliente: "+actual.getName()+" "+actual.getApellido());
        GridPane.setColumnSpan(cliente, 8);
        GridPane.setConstraints(cliente, 0, 1);

        Label ingDia = new Label("Dia Reserva");
        GridPane.setColumnSpan(ingDia, 2);
        GridPane.setConstraints(ingDia, 0, 3);
        TextField dia = new TextField();
        dia.setPromptText("Dia");    
        dia.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        GridPane.setColumnSpan(dia, 15);
        GridPane.setConstraints(dia, 0, 4);

        Label ingMes = new Label("Mes Reserva");
        GridPane.setColumnSpan(ingMes, 2);
        GridPane.setConstraints(ingMes, 0, 6);
        TextField mes = new TextField();
        GridPane.setColumnSpan(mes, 15);
        GridPane.setConstraints(mes, 0, 7);
        mes.setPromptText("Mes");
        mes.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        Label nPasajeros = new Label("Numero de pasajeros");
        GridPane.setColumnSpan(nPasajeros, 2);
        GridPane.setConstraints(nPasajeros, 0, 9);
        TextField pasajeros = new TextField();
        GridPane.setColumnSpan(pasajeros, 15);
        GridPane.setConstraints(pasajeros, 0, 10);
        pasajeros.setPromptText("Debe ser menor que 8");
        pasajeros.setTextFormatter((new TextFormatter<>(new IntegerStringConverter())));

        Button siguiente2 = new Button("Siguiente");
        GridPane.setColumnSpan(siguiente2, 4);
        GridPane.setConstraints(siguiente2, 6, 12);
        
        siguiente2.setOnAction(e->{
            boolean diaValido;
            

            if(Integer.parseInt(dia.getText())>31 || Integer.parseInt(dia.getText())<1){
                diaValido = false;
                Label diaInval = new Label("Ingrese un valor valido");
                GridPane.setColumnSpan(diaInval, 5);
                GridPane.setConstraints(diaInval, 2, 3);
                panelDB.getChildren().add(diaInval);
            }else{
                diaValido = true;

            }

            boolean mesValido;
            if(Integer.parseInt(mes.getText())>12 || Integer.parseInt(mes.getText())<1){
                mesValido=false;
                Label mesInval = new Label("Ingrese un valor valido");
                GridPane.setColumnSpan(mesInval, 5);
                GridPane.setConstraints(mesInval, 2, 6);
                panelDB.getChildren().add(mesInval);
            }else{
                mesValido=true;

            }

            boolean pasValido;
            if(Integer.parseInt(pasajeros.getText())<1 || Integer.parseInt(pasajeros.getText())>8){
                pasValido=false;
                Label pasInval = new Label("Ingrese un valor valido");
                GridPane.setColumnSpan(pasInval, 5);
                GridPane.setConstraints(pasInval, 2, 9);
                panelDB.getChildren().add(pasInval);
            }else{
                pasValido=true;
            }
            if(diaValido && mesValido && pasValido){
                panelDB.getChildren().clear();
                ventana.setScene(temHabitaciones);
                int diaElegido = Integer.parseInt(dia.getText());
                int mesElegido = Integer.parseInt(mes.getText());
                int cantidadPasajeros = Integer.parseInt(pasajeros.getText());
                selecHabitacion(panelHabitaciones, temHabitaciones,diaElegido,mesElegido,cantidadPasajeros,actual,panelMenu,temMenu,temExcursiones,panelExcursiones,temFinalReserva,panelFinalReserva,temInicio,panelInicio);
            }
        });

        panelDB.getChildren().addAll(cliente,ingDia,dia,ingMes,mes,nPasajeros,pasajeros,siguiente2);

        ////// DATOS BASICOS //////
    }

    private void selecHabitacion(GridPane panelHabitaciones, Scene temHabitaciones, int diaElegido, int mesElegido, int cantidadPasajeros,Cliente actual, GridPane panelMenu, Scene temMenu, Scene temExcursiones, GridPane panelExcursiones, Scene temFinalReserva, GridPane panelFinalReserva, Scene temInicio, GridPane panelInicio) {
        panelHabitaciones.setHgap(5);
        panelHabitaciones.setVgap(10);
        panelHabitaciones.setPadding(new Insets(5, 5, 5, 5));

        Label H = new Label();
        H.setText("Escoger Habitacion");
        GridPane.setConstraints(H,3,2);

        Button ei = new Button("Ejecutiva Individual");
        GridPane.setConstraints(ei, 3, 4);
        
        Button ed = new Button("Ejecutiva Doble");
        GridPane.setConstraints(ed, 3, 6);
        
        Button fa = new Button("Familiar");
        GridPane.setConstraints(fa, 3, 8);
        
        Button pe = new Button("Penthouse");
        GridPane.setConstraints(pe, 3, 10);

        Label selecEi = new Label("Seleccionado");
        GridPane.setColumnSpan(selecEi, 10);
        Label selecEd = new Label("Seleccionado");
        GridPane.setColumnSpan(selecEd, 10);
        Label selecFa = new Label("Seleccionado");
        GridPane.setColumnSpan(selecFa, 10);
        Label selecPe = new Label("Seleccionado");
        GridPane.setColumnSpan(selecPe, 10);

        Label toEi = new Label("Demasiados pasajeros");
        GridPane.setColumnSpan(toEi, 5);
        GridPane.setConstraints(toEi, 4, 4);
        Label toEd = new Label("Demasiados pasajeros");
        GridPane.setColumnSpan(toEd, 5);
        GridPane.setConstraints(toEd, 4, 6);
        Label toFa = new Label("Demasiados pasajeros");
        GridPane.setColumnSpan(toFa, 5);
        GridPane.setConstraints(toFa, 4, 8);
        Label toPe = new Label("Demasiados pasajeros");
        GridPane.setColumnSpan(toPe, 5);
        GridPane.setConstraints(toPe, 4, 10);

        ei.setOnAction(e->{
            Habitacion habitacion = rooms.get(0);
            veEi=true;
            if(veEd){
                veEd=false;
                panelHabitaciones.getChildren().remove(selecEd);
                panelHabitaciones.getChildren().remove(toEd);
            }else if(veFa){
                veFa=false;
                panelHabitaciones.getChildren().remove(selecFa);
                panelHabitaciones.getChildren().remove(toFa);
            }else if(vePe){
                vePe=false;
                panelHabitaciones.getChildren().remove(selecPe);
                panelHabitaciones.getChildren().remove(toPe);
            }
            if(cantidadPasajeros>2){
                veEi = false;
                panelHabitaciones.getChildren().add(toEi);
            }else if (habitacion.getDisponible() < 1) {
                vePe = false;
            } else {
                GridPane.setConstraints(selecEi, 4, 4);
                panelHabitaciones.getChildren().add(selecEi);     
            }
        });

        ed.setOnAction(e->{
            Habitacion habitacion = rooms.get(1);
            veEd=true;
            if(veEi){
                veEi=false;
                panelHabitaciones.getChildren().remove(selecEi);
                panelHabitaciones.getChildren().remove(toEi);
            }else if(veFa){
                veFa=false;
                panelHabitaciones.getChildren().remove(selecFa);
                panelHabitaciones.getChildren().remove(toFa);
            }else if(vePe){
                vePe=false;
                panelHabitaciones.getChildren().remove(selecPe);
                panelHabitaciones.getChildren().remove(toPe);
                
            }
            if(cantidadPasajeros>4){
                panelHabitaciones.getChildren().add(toEd);
                veEd = false;
            }
            else if (habitacion.getDisponible() < 1) {
                vePe = false;
            } else {
                GridPane.setConstraints(selecEd, 4, 6);          
                panelHabitaciones.getChildren().add(selecEd);
            }
        });
        
        fa.setOnAction(e->{
            Habitacion habitacion = rooms.get(2);
            veFa=true;
            if(veEi){
                veEi=false;
                panelHabitaciones.getChildren().remove(selecEi);
                panelHabitaciones.getChildren().remove(toEi);
            }else if(veEd){
                veEd=false;
                panelHabitaciones.getChildren().remove(selecEd);
                panelHabitaciones.getChildren().remove(toEd);
            }else if(vePe){
                vePe=false;
                panelHabitaciones.getChildren().remove(selecPe);
                panelHabitaciones.getChildren().remove(toPe);
            }
            if(cantidadPasajeros>8){
                panelHabitaciones.getChildren().add(toFa);
                veFa = false;
            } else if (habitacion.getDisponible() < 1) {
                vePe = false;
            } else {
                GridPane.setConstraints(selecFa, 4, 8);          
                panelHabitaciones.getChildren().add(selecFa);     
            }
        });

        pe.setOnAction(e->{
            Habitacion habitacion = rooms.get(3);
            vePe=true;
            if(veEi){
                veEi=false;
                panelHabitaciones.getChildren().remove(selecEi);
                panelHabitaciones.getChildren().remove(toEi);
            }else if(veFa){
                veFa=false;
                panelHabitaciones.getChildren().remove(selecFa);
                panelHabitaciones.getChildren().remove(toFa);
            }else if(veEd){
                veEd=false;
                panelHabitaciones.getChildren().remove(selecEd);
                panelHabitaciones.getChildren().remove(toEd);
            }
            if(cantidadPasajeros>2){
                panelHabitaciones.getChildren().add(toPe);
                vePe=false;       
            }else if(habitacion.getDisponible()<1){
                vePe=false;
            }else{
                GridPane.setConstraints(selecPe, 4, 10);          
                panelHabitaciones.getChildren().add(selecPe);
            }
        });
        

        Button enviar2 = new Button("Enviar");
        enviar2.setOnAction(e->{
            if(veEd || veEi || veFa || vePe){
                if(veEi){
                    Habitacion habitacion = rooms.get(0);
                    seleccionMenu(panelMenu, temExcursiones,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion, panelExcursiones,temFinalReserva,panelFinalReserva,temInicio,panelInicio);
                    System.out.println(habitacion.getTipo());
                }else if(veEd){
                    Habitacion habitacion = rooms.get(1);
                    seleccionMenu(panelMenu, temExcursiones,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion, panelExcursiones,temFinalReserva,panelFinalReserva,temInicio,panelInicio);
                    System.out.println(habitacion.getTipo());
                }else if(veFa){
                    Habitacion habitacion = rooms.get(2);
                    seleccionMenu(panelMenu, temExcursiones,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,panelExcursiones,temFinalReserva,panelFinalReserva,temInicio,panelInicio);
                    System.out.println(habitacion.getTipo());
                }else if(vePe){
                    Habitacion habitacion = rooms.get(3);
                    seleccionMenu(panelMenu, temExcursiones,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,panelExcursiones,temFinalReserva,panelFinalReserva,temInicio,panelInicio);
                    System.out.println(habitacion.getTipo());
                }
                panelHabitaciones.getChildren().clear();
                ventana.setScene(temMenu);
                
            };
        });

        GridPane.setConstraints(enviar2, 6, 12);

        panelHabitaciones.getChildren().addAll(H,ei,ed,fa,pe,enviar2);
    }

    private void seleccionMenu(GridPane panelMenu, Scene temExcursiones,int diaElegido,int mesElegido,int cantidadPasajeros,Cliente actual, Habitacion habitacion, GridPane panelExcursiones, Scene temFinalReserva, GridPane panelFinalReserva, Scene temInicio, GridPane panelInicio) {
        panelMenu.setHgap(5);
        panelMenu.setVgap(10);
        panelMenu.setPadding(new Insets(5, 5, 5, 5));


        Label M = new Label();
        M.setText("Escoger Menu");
        GridPane.setConstraints(M,3,2);

        Button m1 = new Button("Menu Inicial ");
        GridPane.setConstraints(m1, 3, 4);

        Button m2 = new Button("Menu Intermedio");
        GridPane.setConstraints(m2, 3, 6);

        Button m3 = new Button("Menu Completo");
        GridPane.setConstraints(m3, 3, 8);

        Button m4 = new Button("Menu Avanzado");
        GridPane.setConstraints(m4, 3, 10);

        Button m5 = new Button("Menu Premium");
        GridPane.setConstraints(m5, 3, 12);

        Label selecM1 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecM1, 10);
        Label selecM2 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecM2, 10);
        Label selecM3 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecM3, 10);
        Label selecM4 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecM4, 10);
        Label selecM5 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecM5, 10);
        
        m1.setOnAction(e->{
            veM1=true;
            if(veM2){
                veM2=false;
                panelMenu.getChildren().remove(selecM2);
            }else if(veM3){
                veM3=false;
                panelMenu.getChildren().remove(selecM3);
            }else if(veM4){
                veM4=false;
                panelMenu.getChildren().remove(selecM4);
            }else if(veM5){
                veM5=false;
                panelMenu.getChildren().remove(selecM5);
            }
            if(veM1){
                GridPane.setConstraints(selecM1, 4, 4);          
                panelMenu.getChildren().add(selecM1);
            }
        });

        m2.setOnAction(e->{
            veM2=true;
            if(veM1){
                veM1=false;
                panelMenu.getChildren().remove(selecM1);
            }else if(veM3){
                veM3=false;
                panelMenu.getChildren().remove(selecM3);
            }else if(veM4){
                veM4=false;
                panelMenu.getChildren().remove(selecM4);
            }else if(veM5){
                veM5=false;
                panelMenu.getChildren().remove(selecM5);
            }
            if(veM2){
                GridPane.setConstraints(selecM2, 4, 6);          
                panelMenu.getChildren().add(selecM2);
            }
        });

        m3.setOnAction(e->{
            veM3=true;
            if(veM2){
                veM2=false;
                panelMenu.getChildren().remove(selecM2);
            }else if(veM1){
                veM1=false;
                panelMenu.getChildren().remove(selecM1);
            }else if(veM4){
                veM4=false;
                panelMenu.getChildren().remove(selecM4);
            }else if(veM5){
                veM5=false;
                panelMenu.getChildren().remove(selecM5);
            }
            if(veM3){
                GridPane.setConstraints(selecM3, 4, 8);          
                panelMenu.getChildren().add(selecM3);
            }
        });

        m4.setOnAction(e->{
            veM4=true;
            if(veM2){
                veM2=false;
                panelMenu.getChildren().remove(selecM2);
            }else if(veM3){
                veM3=false;
                panelMenu.getChildren().remove(selecM3);
            }else if(veM1){
                veM1=false;
                panelMenu.getChildren().remove(selecM1);
            }else if(veM5){
                veM5=false;
                panelMenu.getChildren().remove(selecM5);
            }
            if(veM4){
                GridPane.setConstraints(selecM4, 4, 10);          
                panelMenu.getChildren().add(selecM4);
            }
        });

        m5.setOnAction(e->{
            veM5=true;
            if(veM2){
                veM2=false;
                panelMenu.getChildren().remove(selecM2);
            }else if(veM3){
                veM3=false;
                panelMenu.getChildren().remove(selecM3);
            }else if(veM1){
                veM1=false;
                panelMenu.getChildren().remove(selecM1);
            }else if(veM4){
                veM4=false;
                panelMenu.getChildren().remove(selecM4);
            }
            if(veM4){
                GridPane.setConstraints(selecM5, 4, 12);          
                panelMenu.getChildren().add(selecM5);
            }
        });


        Button no = new Button("Omitir");
        no.setOnAction(e->{
            ventana.setScene(temExcursiones);
            PlanMenu planMenu = plans.get(5);
            seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
        });
        GridPane.setConstraints(no, 4, 13);

        Button enviar3 = new Button("Enviar");
        enviar3.setOnAction(e->{
            if(veM1 || veM2 || veM3 || veM4){
                if(veM1){
                    PlanMenu planMenu = plans.get(0);
                    seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
                }else if(veM2){
                    PlanMenu planMenu = plans.get(1);
                    seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
                }else if(veM3){
                    PlanMenu planMenu = plans.get(2);
                    seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
                }else if(veM4){
                    PlanMenu planMenu = plans.get(3);
                    seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
                }else if(veM5){
                    PlanMenu planMenu = plans.get(4);
                    seleccionExcursiones(panelExcursiones, temFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,panelFinalReserva,temInicio,panelInicio);
                }
                panelMenu.getChildren().clear();
                ventana.setScene(temExcursiones);
            }
        });
        GridPane.setConstraints(enviar3, 5, 13);
        panelMenu.getChildren().addAll(M,m1,m2,m3,m4,no,enviar3);
    }

    private void seleccionExcursiones(GridPane panelExcursiones, Scene temFinalReserva,int diaElegido,int mesElegido,int cantidadPasajeros,Cliente actual, Habitacion habitacion,PlanMenu planMenu, GridPane panelFinalReserva, Scene temInicio, GridPane panelInicio) {
        panelExcursiones.setHgap(5);
        panelExcursiones.setVgap(10);
        panelExcursiones.setPadding(new Insets(5, 5, 5, 5));

        Label E = new Label();
        E.setText("Escoger Excursion");
        GridPane.setConstraints(E,3,2);

        Button e1 = new Button("Light");
        GridPane.setConstraints(e1, 3, 4);

        Button e2 = new Button("Plus");
        GridPane.setConstraints(e2, 3, 6);

        Button e3 = new Button("Heavy");
        GridPane.setConstraints(e3, 3, 8);

        Label selecE1 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecE1, 10);
        GridPane.setConstraints(selecE1, 4, 4);
        Label selecE2 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecE2, 10);
        GridPane.setConstraints(selecE2, 4, 6);
        Label selecE3 = new Label("Seleccionado");
        GridPane.setColumnSpan(selecE3, 10);
        GridPane.setConstraints(selecE3, 4, 8);

        e1.setOnAction(e->{
            veE1=true;
            if(veE2){
                veE2=false;
                panelExcursiones.getChildren().remove(selecE2);
            }else if(veE3){
                veE3=false;
                panelExcursiones.getChildren().remove(selecE3);
            }
            panelExcursiones.getChildren().add(selecE1);
        });

        e2.setOnAction(e->{
            veE2=true;
            if(veE1){
                veE1=false;
                panelExcursiones.getChildren().remove(selecE1);
            }else if(veE3){
                veE3=false;
                panelExcursiones.getChildren().remove(selecE3);
            }
            panelExcursiones.getChildren().add(selecE2);
        });

        e3.setOnAction(e->{
            veE3=true;
            if(veE1){
                veE1=false;
                panelExcursiones.getChildren().remove(selecE1);
            }else if(veE2){
                veE2=false;
                panelExcursiones.getChildren().remove(selecE2);
            }
            panelExcursiones.getChildren().add(selecE3);
        });

        Button no2 = new Button("Omitir");
        GridPane.setConstraints(no2, 5, 10);

        no2.setOnAction(e->{
            ventana.setScene(temFinalReserva);
            Excursion excursion = excs.get(3);
            finalReserva(panelFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,excursion,temInicio,panelInicio);
        });
        
        Button enviar4 = new Button("Enviar");
        enviar4.setOnAction(e->{
            if(veE1 || veE2 || veE3){
                if(veE1){
                    Excursion excursion = excs.get(0);
                    finalReserva(panelFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,excursion,temInicio,panelInicio);
                }else if(veE2){
                    Excursion excursion = excs.get(1);
                    finalReserva(panelFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,excursion,temInicio,panelInicio);
                }else if(veE3){
                    Excursion excursion = excs.get(2);
                    finalReserva(panelFinalReserva,diaElegido,mesElegido,cantidadPasajeros,actual,habitacion,planMenu,excursion,temInicio,panelInicio);
                }
                panelExcursiones.getChildren().clear();
                ventana.setScene(temFinalReserva);
            };
        });
        
        GridPane.setConstraints(enviar4, 6, 10);
        
        panelExcursiones.getChildren().addAll(E,e1,e2,e3,no2,enviar4);
    }

    private void finalReserva(GridPane panelFinalReserva,int diaElegido,int mesElegido,int cantidadPasajeros,Cliente actual, Habitacion habitacion,PlanMenu planMenu,Excursion excursion, Scene temInicio, GridPane panelInicio) {
        
        
        panelFinalReserva.setHgap(20);
        panelFinalReserva.setVgap(10);
        panelFinalReserva.setPadding(new Insets(5, 5, 5, 5));

        Label ingNoches = new Label("Ingrese cantidad de noches");
        GridPane.setColumnSpan(ingNoches,10);
        GridPane.setConstraints(ingNoches, 0, 1);
        TextField noches = new TextField();
        noches.setPromptText("Cantidad de noches.");
        noches.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        GridPane.setColumnSpan(noches, 15);
        GridPane.setConstraints(noches, 0, 2);

        Button aceptar = new Button("Aceptar");
        GridPane.setColumnSpan(aceptar, 2);
        GridPane.setConstraints(aceptar, 6, 5);

        Label precio = new Label();
        Button finalizar = new Button("Finalizar");

        aceptar.setOnAction(e->{
            precio.setText("$"+String.valueOf((habitacion.getPrecio()+planMenu.getPrecio()+excursion.getPrecio())*Integer.parseInt(noches.getText())));
            GridPane.setConstraints(precio, 2, 5);
            panelFinalReserva.getChildren().add(precio);
            
            GridPane.setColumnSpan(finalizar, 2);
            GridPane.setConstraints(finalizar, 6, 6);
            panelFinalReserva.getChildren().add(finalizar);
        });
        System.out.println(precio.getText());
        finalizar.setOnAction(e -> {
            Reserva reserva = new Reserva(
                    ((habitacion.getPrecio() + planMenu.getPrecio() + excursion.getPrecio())
                            * Integer.parseInt(noches.getText())),
                    Integer.parseInt(noches.getText()), diaElegido, mesElegido, actual, cantidadPasajeros,
                    habitacion,
                    planMenu, excursion);
            resers.add(reserva);

            try {
                File bd = new File("hotel/reservas.csv");
                Scanner lector = new Scanner(bd);
                if (lector.hasNextLine()) {
                    try {
                        FileWriter escritor = new FileWriter("hotel/reservas.csv", true);
                        escritor.append("\n" + reserva.getCliente().getName() + ","
                                + reserva.getCliente().getApellido() + "," + reserva.getCliente().getRut()
                                + "," + reserva.getPasajeros() + "," + reserva.getDiaInicio() + ","
                                + reserva.getMesInicio() + "," + reserva.getHabitacion().getTipo() + ","
                                + reserva.getPlanMenu().getTipo() + "," + reserva.getExcursion().getTipo()
                                + "," + reserva.getNoches());
                        escritor.close();
                        System.out.println("La escritura del archivo fue realizada con exito!!!");
                    } catch (IOException error) {
                        System.out.println("Ocurrió un error en la escritura del archivo");
                        error.printStackTrace();
                    }
                }
                lector.close();
            } catch (FileNotFoundException error) {
                System.out.println("ERROR: Archivo no encontrado!!!");
                error.printStackTrace();
            }
            Label reservaCreada = new Label("Reserva Creada");
            GridPane.setColumnSpan(reservaCreada, 3);
            GridPane.setConstraints(reservaCreada, 1, 4);
            panelInicio.getChildren().add(reservaCreada);
            panelFinalReserva.getChildren().clear();
            ventana.setScene(temInicio);
        });
        panelFinalReserva.getChildren().addAll(ingNoches, noches, aceptar);
    }
}