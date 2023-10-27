package edu.uclm.esi.iso.ISO2023;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.controllers.UserController;

@SpringBootTest
public class LoginTest {
//  @Autowired
//  private UserController userController ;
//  
//  
//  @Test
//  void loginTest() throws Exception {
//    Cliente cliente = new Cliente("Alejandro","Victoria","cliente@alu.uclm.es","Pepito1234", true, 3, "12/10/2000", "A", "652171717", "928282E");
//    Administrador admin = new Administrador("Alejandro","Victoria","admin@alu.uclm.es","pepito1234", true, 3); 
//
//    Administrador admin2 = new Administrador("Alejandro","Victoria","admin2@alu.uclm.es","Pepito12345*", true, 3);
//    Cliente cliente2 = new Cliente("Alejandro","Victoria","cliente2@alu.uclm.es","Pepito12345*", true, 3, "12/10/2000", "A", "652171717", "92835282E");
//    
//    
//    try {
//      String jadmin = userController.loginUser("nombre":"admin@alu.uclm.es","password":"pepito1234");
//    } catch (Exception e) {
//      System.out.println("Error");
//    }
//    try {
//      String jcliente = userController.loginUser("cliente@alu.uclm.es","Pepito1234");
//    } catch (Exception e) {
//      System.out.println("Error");
//    }
//    
//    
//    try {
//      String jadmin2 = userController.loginUser("admin2@alu.uclm.es","Pepito12345*");
//    } catch (Exception e) {
//      System.out.println("Error");
//    }
//    try {
//      String jcliente2 = userController.loginUser("cliente2@alu.uclm.es","Pepito12345*");
//    } catch (Exception e) {
//      System.out.println("Error");
//    }
//    
//
//  }
  
  
  
	@Autowired
    private UserController userController;
    
    @BeforeEach
    public void setUp() {
        // Configura tus objetos y controladores necesarios antes de cada prueba
        userController = new UserController();
    }

    @Test
    public void testLoginUserAdmin() {
        // Prueba un inicio de sesión exitoso como administrador
        String result = userController.loginUser(Map.of("admin@alu.uclm.es", "Pepito12345*"));
        assertEquals("admin", result);
    }

    @Test
    public void testLoginUserCliente() {
        // Prueba un inicio de sesión exitoso como cliente
        String result = userController.loginUser(Map.of("cliente2@alu.uclm.es", "Pepito12345*"));
        assertEquals("cliente", result);
    }

    @Test
    public void testLoginClienteIncorrectPassword() {
        // Prueba un inicio de sesión fallido de cliente con contraseña incorrecta
        assertThrows(ResponseStatusException.class, () -> userController.loginUser(Map.of("cliente@alu.uclm.es", "pepitoooo")));
    }

    @Test
    public void testLoginAdminInvalidEmail() {
        // Prueba un inicio de sesión fallido de administrador con correo electrónico incorrecto
        assertThrows(ResponseStatusException.class, () -> userController.loginUser(Map.of("admin@alu.uclm.es", "Pepito12345*")));
    }
  
  
}