package edu.uclm.esi.iso.ISO2023;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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

public class CancelarReservaTest {
	@Autowired
    private MockMvc server;

    @Test
    @Order(1)
    void testCancelarReservaComoUsuario() throws Exception {
        ResultActions result = this.sendCliente("prueba@gmail.com", "Hola123*", "d38fbf4f-fafa-4a57-86a5-1f927438150c");
        result
        .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
        .andExpect(content().contentType("application/json"));
        // Puedes agregar más aserciones para verificar el contenido de la respuesta si es necesario
    }

    @Test
	@Order(2)
	void testCancelarReservaComoMantenimiento () throws Exception {
		ResultActions result = this.sendCliente("prueba20@gmail.com", "Hola123*", "63c9b79a-0ce0-4a7c-8926-a60f6a8ca999");
	    result
		    .andExpect(status().is4xxClientError());
	        // Debería devolver un error 4xx ya que no se proporcionó autenticación
	}
    
    @Test
    @Order(3)
    void testCancelarResevaComoAdmin() throws Exception {
        ResultActions result = this.sendCliente("guillermo.santos2@alu.uclm.es", "Hola123*", "d38fbf4f-fafa-4a57-86a5-1f927438150c");
        result
	        .andExpect(status().is4xxClientError());
	        // Debería devolver un error 4xx ya que no se proporcionó autenticación
    }
    
    @Test
    @Order(4)
    void testCancelrReservaSinAutenticación() throws Exception {
        ResultActions result = this.sendCliente("", "","");
        result
            .andExpect(status().is4xxClientError());
            // Debería devolver un error 4xx ya que no se proporcionó autenticación
    }

    public ResultActions sendCliente(String name,String pwd,String idReserva ) throws Exception {
		JSONObject jsoUser = new JSONObject()
				.put("emailUser", name)
				.put("passwordUser",pwd)
				.put("idReserva", idReserva);
		RequestBuilder request = MockMvcRequestBuilders.put("/users/cancelarReserva").contentType("application/json").content(jsoUser.toString());
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
}