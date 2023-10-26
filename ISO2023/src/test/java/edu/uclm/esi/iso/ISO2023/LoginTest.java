package edu.uclm.esi.iso.ISO2023;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uclm.esi.iso.ISO2023.controllers.UserController;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;



public class LoginTest {
	@Autowired
	private UserController userController ;
	
	
	@Test
	void loginTest() throws Exception {
		Cliente cliente = new Cliente("Alejandro","Victoria","cliente@alu.uclm.es","Pepito1234", true, 3, "12/10/2000", "A", "652171717", "928282E");
		Administrador admin = new Administrador("Alejandro","Victoria","admin@alu.uclm.es","pepito1234", true, 3); 

		Administrador admin2 = new Administrador("Alejandro","Victoria","admin2@alu.uclm.es","Pepito12345*", true, 3);
		Cliente cliente2 = new Cliente("Alejandro","Victoria","cliente2@alu.uclm.es","Pepito12345*", true, 3, "12/10/2000", "A", "652171717", "92835282E");
		
		
//		try {
//			String jadmin = userController.loginUser("nombre":"admin@alu.uclm.es","password":"pepito1234");
//		} catch (Exception e) {
//			System.out.println("Error");
//		}
//		try {
//			String jcliente = userController.loginUser("cliente@alu.uclm.es","Pepito1234");
//		} catch (Exception e) {
//			System.out.println("Error");
//		}
//		
//		
//		try {
//			String jadmin2 = userController.loginUser("admin2@alu.uclm.es","Pepito12345*");
//		} catch (Exception e) {
//			System.out.println("Error");
//		}
//		try {
//			String jcliente2 = userController.loginUser("cliente2@alu.uclm.es","Pepito12345*");
//		} catch (Exception e) {
//			System.out.println("Error");
//		}
//		
//
//	}
}
