package principal;

import socket.*;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import BaseDeDatos.BBDD;
import Paneles.TiposPerro;

import java.awt.Image;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import java.io.Serializable;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;

public class Perrera {
	private static io.socket.client.Socket mSocket;
	private List<Perro> perreras;
	static BBDD miconexion;
	Cliente nuevoCliente;

	public Perrera() {
		miconexion = new BBDD();
		perreras = miconexion.getAllPerros();
		nuevoCliente = new Cliente(); // Creo un nuevo cliente
		String usuario = JOptionPane.showInputDialog("Introduce tu nick");
		nuevoCliente.usuario(usuario);
		
	}

	public void anadePerro(Perro nuevo) {
		perreras.add(nuevo);
		miconexion.insertaPerro(nuevo);
	}

	public void eliminaPerro(Perro p) {
		perreras.remove(p);
		miconexion.eliminaPerro(p);
	}

	public void buscaDueno() {
		String[] duenos = new String[perreras.size()]; // Creo un array de los duenios (en ejecucion)

		for (int i = 0; i < duenos.length; i++) // Introduzco los duenios en tiempo de ejecucinin
			duenos[i] = (String) perreras.get(i).getAmo();

		String dueno = (String) JOptionPane.showInputDialog(null, "Elige un dueno primero", "Dueno:", 3, null, duenos,
				null);
		muestraPerroDueno(dueno);
	}

	public void muestraPerroDueno(String dueno) { // Busca y muestra los perros del duenio recibido
		String perros = "";
		for (Perro perro : perreras) {
			if (perro.getAmo().equals(dueno))
				perros += "-" + perro.getNombre() + "\n";
		}
		JOptionPane.showMessageDialog(null, perros, "Los perros de: " + dueno, JOptionPane.INFORMATION_MESSAGE);

	}

	public void muestraTodos() { // FIXME Muestra todos los perros de la perrera
		String perros = "Nombre:          Amo:            Edad:            Color:              Herido:\n\n";

		for (Perro perro : perreras)
			perros += perro;

		JOptionPane.showMessageDialog(null, perros, "Tenemos estos perros:", JOptionPane.INFORMATION_MESSAGE);
	}

	public void ordenaNombres() {
		Collections.sort(perreras);
	}

	public boolean estaVacia() {
		if (perreras.size() == 0)
			return true;
		return false;
	}

	public String[] rellenaArray() { // Devuelve un array con los perros
		String[] array = new String[perreras.size()];
		for (int i = 0; i < perreras.size(); i++) // Relleno el array para poder elegir en el desplegable
			array[i] = perreras.get(i).getNombre();
		return array;
	}

	public static ImageIcon devuelveIcono() {
		ImageIcon iconoPerro = new ImageIcon(Perrera.class.getResource("/images/dog.png"));
		Image iconoPerroaCambiar = iconoPerro.getImage();
		Image iconoPerroFinal = iconoPerroaCambiar.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		return iconoPerro = new ImageIcon(iconoPerroFinal);
	}

	public void atacaPerro() { // Un perro ataca a otro perro
		Perro atacante = buscaPerro((String) JOptionPane.showInputDialog(null, "Elige el perro que muerde", "Perro:", 3,
				null, rellenaArray(), null));
		Perro mordido = buscaPerro((String) JOptionPane.showInputDialog(null, "Elige el perro que es mordido", "Perro:",
				3, null, rellenaArray(), null));

		atacante.muerde(mordido); // Ataque

		if (!mordido.getVivo()) { // Si ha muerto o si esta herido
			String alerta = mordido.getNombre() + " HA MUERTO !!!";
			JOptionPane.showMessageDialog(null, alerta, "!!!!!", JOptionPane.INFORMATION_MESSAGE, devuelveIcono());
			perreras.remove(mordido);
			miconexion.eliminaPerro(mordido);
			nuevoCliente.enviaMordisco(alerta);
		} else {
			String alerta = atacante.getNombre() + " ha mordido a " + mordido.getNombre() + " y est� herido !!!";
			JOptionPane.showMessageDialog(null, alerta, "!!!!!", JOptionPane.INFORMATION_MESSAGE, devuelveIcono());
			nuevoCliente.enviaMordisco(alerta);
		}

	}

	public Perro buscaPerro(String nombre) { // Encuentra y devuelve el perro por su nombre
		for (Perro p : perreras)
			if (p.getNombre().equals(nombre))
				return p;
		return null;
	}

	public Perro anadePerroConsola() {

		String dueno = JOptionPane.showInputDialog(null, "Tu nombre:");
		JTextField nombrePeText = new JTextField(5);
		JTextField edadText = new JTextField(5);
		JTextField colorText = new JTextField(5);

		JPanel creaPerro = new JPanel();
		creaPerro.add(new JLabel("nombre: "));
		creaPerro.add(nombrePeText);
		creaPerro.add(Box.createHorizontalStrut(15));
		creaPerro.add(new JLabel("edad: "));
		creaPerro.add(edadText);
		creaPerro.add(Box.createHorizontalStrut(15));

		JOptionPane.showConfirmDialog(null, creaPerro, "Datos del perro: ", JOptionPane.OK_CANCEL_OPTION);
		TiposPerro colorTipo = new TiposPerro();

		String color = colorTipo.getPanel();

		Perro perroNuevo = new Perro(nombrePeText.getText(), dueno, Integer.parseInt(edadText.getText()), color);
		nuevoCliente.perroAniadido(perroNuevo.getNombre());
		return perroNuevo;

	}

	public Perro procrearPerro() {
		String nombreMacho = (String) JOptionPane.showInputDialog(null, "Elige un perro", "Perro:", 3, null,
				rellenaArray(), null);
		String nombreHembra = (String) JOptionPane.showInputDialog(null, "Elige otro perro", "Perro:", 3, null,
				rellenaArray(), null);

		String nuevoNombre = creaNombre(nombreMacho, nombreHembra);
		Perro padre = buscaPerro(nombreMacho);

		Perro nuevo = new Perro(nuevoNombre, padre.getAmo(), 0, padre.getColor());
		JOptionPane.showMessageDialog(null, "Ha nacido un nuevo Perro !!! \n se llama: " + nuevo.getNombre(), "!!!!!",
				JOptionPane.INFORMATION_MESSAGE, devuelveIcono());
		return nuevo;
	}

	public String creaNombre(String nombremacho, String nombrehembra) { // FIXME Devuelve el nombre del nuevo perro
																		// (HIJO)
		String hom = "";
		String hem = "";

		for (int i = 0; i < 3; i++)
			hom += String.valueOf(nombremacho.charAt(i));

		for (int i = 2; i < nombrehembra.length(); i++)
			hem += String.valueOf(nombrehembra.charAt(i));

		return hom + hem; // Este sera el nombre del nuevo perro (hijo) (concatenamos silabas)
	}
	public void usuariosConectados() {
		nuevoCliente.devuelveUsuarios();
	}
	public void desconectaUsuario() {
		nuevoCliente.desconectarUsuario();
	}
}
