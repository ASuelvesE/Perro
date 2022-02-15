package chat;

import javax.swing.*;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import principal.*;
import principal.Perrera;
import socket.Cliente;

import java.awt.event.*;
import java.net.URISyntaxException;

public class Chat implements ActionListener {
	JLabel titulo, usuariosConectados;
	JTextArea area;
	JTextArea areaEscribir;
	JButton boton;
	 JFrame ventana;
	private static String users = "actualiza usuarios";
	private static String eventoChat = "evento chat";
	private static String mensaje;
	private String chat = "";

	private static io.socket.client.Socket mSocket;

	public Chat() {
		devuelveUsuarios();
		ventana = new JFrame();
		titulo = new JLabel();
		titulo.setBounds(50, 25, 300, 30);
		titulo.setText("BIENVENIDOS AL CHAT:");
		usuariosConectados = new JLabel();
		usuariosConectados.setBounds(650, 25, 300, 30);
		usuariosConectados.setText("Usuarios conectados: \n\n");
		area = new JTextArea();
		area.setBounds(20, 75, 600, 200);
		areaEscribir = new JTextArea();
		areaEscribir = new JTextArea();
		areaEscribir.setBounds(20, 300, 600, 50);
		boton = new JButton("Enviar mensaje");
		boton.setBounds(200, 400, 220, 30);
		boton.addActionListener(this);
		ventana.add(titulo);
		ventana.add(usuariosConectados);
		ventana.add(area);
		ventana.add(boton);
		ventana.add(areaEscribir);
		ventana.setSize(1200, 800);
		ventana.setLayout(null);
		ventana.setVisible(true);
		area.setText("");

		ventana.addWindowListener(new WindowAdapter() { //Cierra la ventana al dar a la X
			public void windowClosing(WindowEvent evt) {
				System.out.println("Has salido del chat");
				System.exit(0);
			}
		});

		try {
			mSocket = IO.socket("https://servidorclaseperro.herokuapp.com/");
//			mSocket = IO.socket("http://127.0.0.1:3000/");
			System.out.println("Chat conectado al servidor");
		} catch (URISyntaxException e) {
			System.out.println(e.getMessage());
		}

		mSocket.connect(); // Conectamos y permanecemos a la escucha

		///////////////////////////////////////////////////// RECIBIMOS DEL SERVIDOR
		///////////////////////////////////////////////////// ////////////////////////////////////////////////////
		
		mSocket.on(eventoChat, new Emitter.Listener() { // Esperamos el mensaje de chat desde el servidor
			public void call(Object... args) {
				chat += args[1] + " : " + (String) args[0] + "\n";
				area.setText(chat);
			}
		});
		
		mSocket.on(users, new Emitter.Listener() { // Esperamos los usuarios servidor
			public void call(Object... args) {
				String nombres = "Usuarios conectados: \n\n"; 
				for (int i = 0; i < args.length; i++) {
					nombres += nombres.valueOf(args[i]) + "\n";
				}
				System.out.println("Usuarios recibidos: "+ nombres);
				usuariosConectados.setText(nombres);

			}
		});
		
		

		
	}

	/////////////////////////////////////////////// PETICIONES AL SERVIDOR
	/////////////////////////////////////////////// ////////////////////////////////////////////

	@Override
	public void actionPerformed(ActionEvent e) {
		if (areaEscribir.getText().equalsIgnoreCase("ADIOS")) {
			System.out.println("Has salido del chat");
			UsoPerro.iniciaMenu();
			System.exit(0);

		} else {
			mSocket.emit(eventoChat, areaEscribir.getText(), Cliente.nombre);
			mSocket.emit(users);
		}
		areaEscribir.setText(null);

	}
	public void devuelveUsuarios() {

	}  
	
	
	////////////////////////////////////// GETTERS AND
	////////////////////////////////////// SETTERS///////////////////////////////////////////////////

	public JLabel getL1() {
		return titulo;
	}

	public void setL1(JLabel l1) {
		this.titulo = l1;
	}

	public JLabel getL2() {
		return usuariosConectados;
	}

	public void setL2(JLabel l2) {
		this.usuariosConectados = l2;
	}

	public JTextArea getArea() {
		return area;
	}

	public void setArea(JTextArea area) {
		this.area = area;
	}

	public JButton getB() {
		return boton;
	}

	public void setB(JButton b) {
		this.boton = b;
	}

	public String getEventoChat() {
		return eventoChat;
	}

	public void setEventoChat(String eventoChat) {
		Chat.eventoChat = eventoChat;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		Chat.mensaje = mensaje;
	}

	public static io.socket.client.Socket getmSocket() {
		return mSocket;
	}

	public static void setmSocket(io.socket.client.Socket mSocket) {
		Chat.mSocket = mSocket;
	}

}