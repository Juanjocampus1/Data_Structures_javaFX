package com.empresa.datastructures_javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelloController {
    @FXML
    private TextField txt_dato;
    @FXML
    private Label lbl_count;
    @FXML
    private TextField Agregar;
    @FXML
    private ListView<String> Mostrar;
    @FXML
    private Label lbl_time; // Nuevo Label para la fecha y hora
    @FXML
    private Button btn_sort;

    private ArrayList<String> lista = new ArrayList<String>();
    private Set<String> conjunto = new TreeSet<String>();
    // TextField para ingresar los goles

    @FXML
    private ComboBox<String> cb_jugadores; // ComboBox para seleccionar los jugadores
    @FXML
    private TextField txt_goles; // TextField para ingresar los goles
    @FXML
    private ListView<String> lv_clasificacion; // ListView para mostrar la clasificación
    @FXML
    private ComboBox<String> cb_jornadas; // Nuevo ComboBox para seleccionar la jornada

    private Map<String, Map<String, Integer>> golesJugadores = new HashMap<>(); // Map para almacenar los goles de los jugadores y la jornada

    @FXML
    private TextField txt_jugador;

    @FXML
    protected void agregarAL() {
        lista.add(txt_dato.getText());
        txt_dato.clear();
    }

    @FXML
    protected void mostrarAL() {
        Mostrar.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    protected void add() {
        conjunto.add(Agregar.getText());
        Agregar.clear();
        updateCount();
    }

    @FXML
    protected void show() {
        Mostrar.setItems(FXCollections.observableArrayList(conjunto));
        showAlertWithIP();
    }

    @FXML
    public void initialize() {
        Mostrar.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación de eliminación");
                alert.setHeaderText("Eliminar elemento");
                alert.setContentText("¿Desea eliminar el elemento seleccionado?");
                updateDateTime();

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    conjunto.remove(newSelection);
                    show();
                    updateCount();
                }
            }
        });

        // Crear un Timeline para actualizar la fecha y hora cada minuto
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), event -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        cb_jugadores.setItems(FXCollections.observableArrayList("AGRAMONTE ESTEVEZ", "RAFAEL ENRIQUE", "AMADOR ARANGO", "ANUAR DAVID", "CEVALLOS JIMENEZ"));
        cb_jornadas.setItems(FXCollections.observableArrayList("Jornada 1", "Jornada 2", "Jornada 3", "Jornada 4", "Jornada 5"));

    }

    private void updateCount() {
        lbl_count.setText("elementos: " + conjunto.size());
    }

    private void showAlertWithIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // check if the IP address starts with "172.16."
                    if (addr.getHostAddress().startsWith("172.16.")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información de la máquina");
                        alert.setHeaderText(null);
                        alert.setContentText("La dirección IP de tu máquina es: " + addr.getHostAddress());
                        alert.showAndWait();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar la fecha y hora
    private void updateDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        lbl_time.setText(LocalDateTime.now().format(formatter));
    }

    @FXML
    protected void sort() {
        FXCollections.sort(Mostrar.getItems());
    }

    @FXML
    protected void agregarGoles() {
        String jugador = cb_jugadores.getValue();
        String jornada = cb_jornadas.getValue();
        int goles = Integer.parseInt(txt_goles.getText());

        // Agregar o actualizar los goles del jugador en la jornada seleccionada
        Map<String, Integer> golesPorJornada = golesJugadores.getOrDefault(jugador, new HashMap<>());
        golesPorJornada.put(jornada, golesPorJornada.getOrDefault(jornada, 0) + goles);
        golesJugadores.put(jugador, golesPorJornada);

        txt_goles.clear();
    }

    @FXML
    protected void mostrarClasificacion() {
        // Ordenar los jugadores por goles en orden descendente
        List<Map.Entry<String, Map<String, Integer>>> clasificacion = new ArrayList<>(golesJugadores.entrySet());
        clasificacion.sort((e1, e2) -> e2.getValue().values().stream().reduce(0, Integer::sum).compareTo(e1.getValue().values().stream().reduce(0, Integer::sum)));

        // Mostrar los tres primeros jugadores en el ListView
        List<String> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, clasificacion.size()); i++) {
            String jugador = clasificacion.get(i).getKey();
            int goles = clasificacion.get(i).getValue().values().stream().reduce(0, Integer::sum);
            String jornadas = String.join(", ", clasificacion.get(i).getValue().keySet());
            top3.add(jugador + ": " + goles + " goles (Jornadas: " + jornadas + ")");
        }
        lv_clasificacion.setItems(FXCollections.observableArrayList(top3));
    }

    @FXML
    protected void agregarJugador() {
        String jugador = txt_jugador.getText();

        // Agregar el jugador al ComboBox
        cb_jugadores.getItems().add(jugador);

        txt_jugador.clear();
    }
}