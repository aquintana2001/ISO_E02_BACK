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

class CancelarReservaTest {
	@Autowired
	private MockMvc server;
	private static final String CONTRASENA = "Hola123*";
	private static final String RUTA = "application/json";

	@Test
	@Order(1)
	void testCancelarReservaComoUsuario() throws Exception {
		ResultActions result = this.sendCliente("pruebaba@gmail.com", CONTRASENA,
				"9ac4e263-6042-47ae-aa4f-99425b037af6");
		result.andExpect(status().isOk()); // Verificar que el código de estado es 200 OK

		// Puedes agregar más aserciones para verificar el contenido de la respuesta si
		// es necesario
	}

	@Test
	@Order(2)
	void testCancelarReservaComoMantenimiento() throws Exception {
		ResultActions result = this.sendCliente("pepe4@pepe.com", CONTRASENA,
				"7e4ec6be-89e0-4970-91b0-fc2d5f14fb02");
		result.andExpect(status().isOk()); // Verificar que el código de estado es 200 OK

		// Puedes agregar más aserciones para verificar el contenido de la respuesta si
		// es necesario
	}

	@Test
	@Order(3)
	void testCancelarResevaComoAdmin() throws Exception {
		ResultActions result = this.sendCliente("guillermo.santos2@alu.uclm.es", CONTRASENA,
				"d38fbf4f-fafa-4a57-86a5-1f927438150c");
		result.andExpect(status().is4xxClientError());
		// Debería devolver un error 4xx ya que un admin no tiene permiso
	}

	@Test
	@Order(4)
	void testCancelrReservaSinAutenticación() throws Exception {
		ResultActions result = this.sendCliente("", "", "d38fbf4f-fafa-4a57-86a5-1f927438150c");
		result.andExpect(status().is4xxClientError());
		// Debería devolver un error 4xx ya que no se proporcionó autenticación
	}

	public ResultActions sendCliente(String name, String pwd, String idReserva) throws Exception {
		ResultActions resultActions = null;
		JSONObject jsoUser = new JSONObject().put("emailUser", name).put("passwordUser", pwd).put("idReserva",
				idReserva);
		RequestBuilder request = MockMvcRequestBuilders.put("/users/cancelarReserva").contentType(RUTA)
				.content(jsoUser.toString());
		resultActions = this.server.perform(request);
		return resultActions;
	}
}