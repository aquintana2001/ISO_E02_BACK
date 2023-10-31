package edu.uclm.esi.iso.ISO2023;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.json.JSONObject;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class LoginTest {
	@Autowired
	private MockMvc server;
	@Test @Order(1)
	void testLoginCliente() throws Exception {
	    ResultActions result = this.sendLogin("guillermo.4@alu.uclm.es", "Hola123*");
	    result
	        .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
	        .andExpect(content().string("cliente"));
	}
	@Test @Order(2)
	void testLoginAdmin() throws Exception {
	    ResultActions result = this.sendLogin("guillermo.santos2@alu.uclm.es", "Hola123*");
	    result
	        .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
	        .andExpect(content().string("admin"));
	}
	@Test @Order(3)
	void testEmailNotFound() throws Exception {
	    ResultActions result = this.sendLogin("guillermo.423@alu.uclm.es", "Hola123*");
	    result
	        .andExpect(status().is4xxClientError());
	}
	@Test @Order(4)
	void testLoginPasswordFail() throws Exception {
	    ResultActions result = this.sendLogin("guillermo.3@alu.uclm.es", "Hola123");
	    result
	        .andExpect(status().is4xxClientError());
	}
	public ResultActions sendLogin(String name,String pwd) throws Exception {
		JSONObject jsoUser = new JSONObject()
				.put("email", name)
				.put("password",pwd);
		RequestBuilder request = MockMvcRequestBuilders.put("/users/login").contentType("application/json").content(jsoUser.toString());
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
}
