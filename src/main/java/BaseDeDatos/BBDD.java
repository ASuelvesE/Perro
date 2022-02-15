package BaseDeDatos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import principal.*;

public class BBDD {

	private Connection conexion;
	
	public BBDD() {

		//String URL = "jdbc:mysql://80.34.34.150:33070/perrera";
		String URL = "jdbc:mysql://192.168.8.25:3306/perrera";
		
		try {
			conexion = DriverManager.getConnection(URL,"admin","admin");  //
			System.out.println("Conectado a la BDDD exitosamente");
		} catch (SQLException e) {
			System.out.println("No se ha podido conectar a la BBDD");
			e.printStackTrace();
		}
	}
	
	public List<Perro> getAllPerros() {
		List<Perro> perros = new ArrayList<>();
		try {
			ResultSet rs = conexion.createStatement().executeQuery("select * from perros");
			while(rs.next()) {
				String nombre = "", amo, color;
				int edad = rs.getInt("edad");
				nombre = rs.getString("nombre");
				amo = rs.getString("amo");
				color = rs.getString("color");
				perros.add(new Perro(nombre,amo,edad,color));
			}
			//System.out.println(perros);
			return perros;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public void insertaPerro(Perro perro) {

		String nuevo = "insert into perros values ('"+perro.getNombre()+ "','"+perro.getAmo()+"',"+perro.getEdad()+",'"+perro.getColor()+"')";
		//System.out.println(nuevo);
		try {
		conexion.createStatement().execute(nuevo);
		System.out.println("Perro insertado en la base de datos");
		}
		catch(Exception e) {
			System.out.println("No se ha podido insertar el perro");
		}
	} 
	
	public void eliminaPerro(Perro perro) {
		String elimina = "delete from perros where nombre = '"+perro.getNombre()+"'";
		//System.out.println(elimina);
		try {
			conexion.createStatement().execute(elimina);
			System.out.println("Perro eliminado de la base de datos");
		}catch(Exception e) {
			System.out.println("No se ha podido eliminar el perro de la BBDD");
		}
	}
}
