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
class ActualizarDatosTest {
	@Autowired
	private MockMvc server;
	private static final String CONTRASENA = "Hola123*";
	private static final String RUTA = "application/json";

	private static final String EMAIL = "pruebaba@gmail.com";
	@Test
	@Order(1)
	void ActualizarDatosCliente() throws Exception {
		ResultActions result = this.sendUser(EMAIL, CONTRASENA, "prueba", "tests1", "01/09/2000", "B",
				"612345678", "16125580G");
		result.andExpect(status().isOk());// Verificar que el código de estado es 200 OK
	}

	@Test
	@Order(2)
	void ActualizarDatosInvalidDNI() throws Exception {
		ResultActions result = this.sendUser(EMAIL, CONTRASENA, "pruebaaa", "tests2", "01/02/2000", "B",
				"612345678", "16125580");
		result.andExpect(status().is4xxClientError());
	}

	@Test
	@Order(3)
	void ActualizarDatosInvalidPhone() throws Exception {
		ResultActions result = this.sendUser(EMAIL, CONTRASENA, "pruebaccdt", "tests3", "01/01/2000", "B",
				"612345", "16125580G");
		result.andExpect(status().is4xxClientError());
	}

	@Test
	@Order(4)
	void ActualizarDatosMantenimiento() throws Exception {
		ResultActions result = this.sendUser("", "", "", "", "", "", "", "");
		result.andExpect(status().is4xxClientError());
	}

	public ResultActions sendUser(String email, String pwd, String nombre, String apellidos, String fechaNacimiento,
			String carnet, String telefono, String dni) throws Exception {
		ResultActions resultActions = null;
		JSONObject jsoUser = new JSONObject().put("email", email).put("password", pwd).put("nombre", nombre)
				.put("apellidos", apellidos).put("fechaNacimiento", fechaNacimiento).put("carnet", carnet)
				.put("telefono", telefono).put("dni", dni);
		RequestBuilder request = MockMvcRequestBuilders.put("/cliente/actualizarDatos").contentType(RUTA)
				.content(jsoUser.toString());
		resultActions = this.server.perform(request);
		return resultActions;
	}
}