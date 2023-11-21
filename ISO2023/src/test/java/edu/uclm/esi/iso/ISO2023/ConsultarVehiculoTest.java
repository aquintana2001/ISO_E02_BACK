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
public class ConsultarVehiculoTest {
    @Autowired
    private MockMvc server;

    @Test
    @Order(1)
    void testConsultarVehiculosComoAdmin() throws Exception {
        ResultActions result = this.sendAdmin("pepe@pepe.com", "Hola123*");
        result
            .andExpect(status().isOk()) // Verificar que el código de estado es 200 OK
            .andExpect(content().contentType("application/json"));
            // Puedes agregar más aserciones para verificar el contenido de la respuesta si es necesario
    }

    @Test
    @Order(2)
    void testConsultarVehiculosComoUsuario() throws Exception {
        ResultActions result = this.sendAdmin("antonio@gmail.com", "Hola123*");
        result
            .andExpect(status().is4xxClientError());
            // Debería devolver un error 4xx ya que un usuario no tiene permiso para consultar vehículos
    }

    @Test
    @Order(3)
    void testConsultarVehiculosSinAutenticación() throws Exception {
        ResultActions result = this.sendAdmin("", "");
        result
            .andExpect(status().is4xxClientError());
            // Debería devolver un error 4xx ya que no se proporcionó autenticación
    }

    public ResultActions sendAdmin(String name,String pwd) throws Exception {
		JSONObject jsoUser = new JSONObject()
				.put("emailUser", name)
				.put("passwordUser",pwd);
		RequestBuilder request = MockMvcRequestBuilders.post("/admin/vehiculo").contentType("application/json").content(jsoUser.toString());
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
}