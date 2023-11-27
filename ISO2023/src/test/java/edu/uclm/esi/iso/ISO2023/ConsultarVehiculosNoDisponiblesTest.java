package edu.uclm.esi.iso.ISO2023;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.json.JSONObject;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ConsultarVehiculosNoDisponiblesTest {
	@Autowired
	private MockMvc server;
	private static final String CONTRASENA = "Hola123*";
	private static final String RUTA = "application/json";

	@Test
	@Order(1)
	void testConsultarVehiculosNoDisponiblesComoMantenimiento () throws Exception {
		ResultActions result = this.sendMantenimiento("prueba20@gmail.com", CONTRASENA);
	    result
            .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
            .andExpect(content().contentType(RUTA));
            // Puedes agregar más aserciones para verificar el contenido de la respuesta si es necesario
   
	}
	
	@Test
	@Order(2)
	void testConsultarVehiculosNoDisponiblesComoAdmin () throws Exception {
		ResultActions result = this.sendMantenimiento("guillermo.santos2@alu.uclm.es", CONTRASENA);
	    result
            .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
            .andExpect(content().contentType(RUTA));
            // Puedes agregar más aserciones para verificar el contenido de la respuesta si es necesario
   
	}
	
	@Test
	@Order(3)
	void testConsultarVehiculosNoDisponiblesComoCliente () throws Exception {
		ResultActions result = this.sendMantenimiento("guillermo.423@alu.uclm.es", CONTRASENA);
	    result
	        .andExpect(status().is4xxClientError());
	        // Debería devolver un error 4xx ya que un usuario no tiene permiso para consultar vehículos

	}
	
	@Test
	@Order(4)
	void testConsultarVehiculosNoDisponiblesSinAutentificación () throws Exception {
		ResultActions result = this.sendMantenimiento("", "");
	    result
	        .andExpect(status().is4xxClientError());
	        // Debería devolver un error 4xx ya que un usuario no tiene permiso para consultar vehículos

	}
	
	public ResultActions sendMantenimiento(String name,String pwd) throws Exception {
		ResultActions resultActions = null;
		JSONObject jsoUser = new JSONObject()
				.put("emailUser", name)
				.put("passwordUser",pwd);
		RequestBuilder request = MockMvcRequestBuilders.post("/users/vehiculo").contentType(RUTA).content(jsoUser.toString());
		resultActions = this.server.perform(request);
		return resultActions;
	}
}
