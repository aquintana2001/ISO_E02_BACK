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
class ListarReservasTest {
	@Autowired
	private MockMvc server;
	private static final String CONTRASENA = "Hola123*";
	private static final String RUTA = "application/json";

	@Test
	@Order(1)
	void ListarReservasComoCliente() throws Exception {
		ResultActions result = this.sendUser("pruebaba@gmail.com", CONTRASENA);
		result.andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
				.andExpect(content().contentType(RUTA));
	}

	@Test
	@Order(2)
	void ListarReservasComoMantenimiento() throws Exception {
		ResultActions result = this.sendUser("prueba20@gmail.com", CONTRASENA);
		result.andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
				.andExpect(content().contentType(RUTA));
	}

	@Test
	@Order(3)
	void ListarReservasComoAdministrador() throws Exception {
		ResultActions result = this.sendUser("prueba10@gmail.com", "");
		result.andExpect(status().is4xxClientError());
		// Debería devolver un error 4xx ya que un usuario no tiene permiso para
				// consultar vehículos
	}

	@Test
	@Order(4)
	void ListarReservasSinAutentificación() throws Exception {
		ResultActions result = this.sendUser("", "");
		result.andExpect(status().is4xxClientError());
		
		// Debería devolver un error 4xx ya que no se proporcionó autenticación

	}

	public ResultActions sendUser(String name, String pwd) throws Exception {
		ResultActions resultActions = null;
		JSONObject jsoUser = new JSONObject().put("emailUser", name).put("passwordUser", pwd);
		RequestBuilder request = MockMvcRequestBuilders.post("/users/listarReservas").contentType(RUTA)
				.content(jsoUser.toString());
		resultActions = this.server.perform(request);
		return resultActions;
	}
}