package socket;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

import principal.Perrera;

public class Cliente {
	private String evento = "chat mensaje";
	private String conectado = "usuario nuevo";
	private String desconectado = "usuario desconectado";
	private String usuariosConectados = "usuarios conectados";
	public static String nombre;
	private String nuevoPerro = "nuevo perro";

	private static io.socket.client.Socket mSocket;

	public Cliente() {

		/////////////////////////////// AQUI DEBAJO CONECTAMOS AL
		/////////////////////////////// SERVIDOR/////////////////////////////

		try {
//	        mSocket = IO.socket("https://servidorclaseperro.herokuapp.com/");
			mSocket = IO.socket("http://127.0.0.1:3000/");
			System.out.println("Conexion establecida con el servidor");
		} catch (URISyntaxException e) {
			System.out.println(e.getMessage());
		}

		mSocket.connect(); // Conectamos y permanecemos a la escucha

		/////////////////////////////// AQUI DEBAJO CONECTAMOS AL
		/////////////////////////////// SERVIDOR/////////////////////////////

		mSocket.on(evento, new Emitter.Listener() { // Esperamos si recibimos un evento
			public void call(Object... args) {
				JOptionPane.showMessageDialog(null, "SERVIDOR: \n" + args[0], "MENSAJE DEL SERVIDOR:",
						JOptionPane.INFORMATION_MESSAGE, Perrera.devuelveIcono());
			}
		});

		mSocket.on(conectado, new Emitter.Listener() { // Esperamos el nombre del nuevo usuario conectado
			public void call(Object... args) {
				JOptionPane.showMessageDialog(null, "SERVIDOR: \n" + args[0] + " se ha conectado !!!",
						"MENSAJE DEL SERVIDOR:", JOptionPane.INFORMATION_MESSAGE, Perrera.devuelveIcono());
			}
		});

		mSocket.on(nuevoPerro, new Emitter.Listener() { // Esperamos el nombre del nuevo perro desde el server
			public void call(Object... args) {
				JOptionPane.showMessageDialog(null,
						"SERVIDOR:\n" + args[1] + " Ha creado un nuevo perro llamado: \n" + args[0],
						"MENSAJE DEL SERVIDOR:", JOptionPane.INFORMATION_MESSAGE, Perrera.devuelveIcono());
			}
		});



		mSocket.on(usuariosConectados, new Emitter.Listener() { // Esperamos los usuarios conectados desde el servidor
			public void call(Object... args) {
				String usuarios = "";
				for (int i = 0; i < args.length; i++) {
					usuarios += args[i] + "\n";
					// System.out.println(args[i]);
				}
				JOptionPane.showMessageDialog(null, "SERVIDOR:\n Usuarios conectados: \n" + (String) usuarios,
						"MENSAJE DEL SERVIDOR:", JOptionPane.INFORMATION_MESSAGE, Perrera.devuelveIcono());
			}
		});

	}
	

	/////////////////////////////// AQUI DEBAJO LO QUE ENVIAMOS AL
	/////////////////////////////// SERVIDOR/////////////////////////////
	public void enviaMordisco(String mensaje) { // Enviamos un mensaje al servidor
		mSocket.emit(evento, mensaje);
	}

	public void usuario(String nick) {
		mSocket.emit(conectado, nick);
		nombre = nick;
	}

	public void devuelveUsuarios() {
		mSocket.emit(usuariosConectados);
	}  

//	public void desconectarUsuario() {
//		mSocket.emit(desconectado, nombre);
//		mSocket.disconnect();
//	}

	public void perroAniadido(String perro) {
		mSocket.emit(nuevoPerro, perro, nombre);
	}




	
	///////////////////////////////////////////// GETTERS Y SETTERS /////////////////////////////////////
	
	public String getEvento() {
		return evento;
	}


	public void setEvento(String evento) {
		this.evento = evento;
	}


	public String getConectado() {
		return conectado;
	}


	public void setConectado(String conectado) {
		this.conectado = conectado;
	}


	public String getDesconectado() {
		return desconectado;
	}


	public void setDesconectado(String desconectado) {
		this.desconectado = desconectado;
	}


	public String getUsuariosConectados() {
		return usuariosConectados;
	}


	public void setUsuariosConectados(String usuariosConectados) {
		this.usuariosConectados = usuariosConectados;
	}


	public static String getNombre() {
		return nombre;
	}


	public static void setNombre(String nombre) {
		Cliente.nombre = nombre;
	}


	public String getNuevoPerro() {
		return nuevoPerro;
	}


	public void setNuevoPerro(String nuevoPerro) {
		this.nuevoPerro = nuevoPerro;
	}

	public static io.socket.client.Socket getmSocket() {
		return mSocket;
	}


	public static void setmSocket(io.socket.client.Socket mSocket) {
		Cliente.mSocket = mSocket;
	}




}
