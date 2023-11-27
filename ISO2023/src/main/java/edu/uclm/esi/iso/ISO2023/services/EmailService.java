package edu.uclm.esi.iso.ISO2023.services;
import org.json.JSONArray;  
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;

import java.io.IOException;



@Service
public class EmailService {
	 private static final String RUTA = "application/json";
	 

	public void sendCorreoConfirmacion(Cliente possibleCliente, String url) {
		 String htmlContent = "<html><head></head><body><p>Por favor, confirma que ha olvidado la contraseña pulsando en el siguiente enlace,</p><p>Haz click aquí <a href='"+url+"'>restablecimiento de la contraseña MUEVE-TIC</a></p></body></html>";
		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse(RUTA);

		JSONObject jsoSender = new JSONObject().
				put("name", "MUEVETE-TIC").
				put("email", "PracticaISO2324@outlook.es");

		JSONObject jsoTo = new JSONObject().	
				put("email", possibleCliente.getEmail()).
				put("name",possibleCliente.getNombre());

		JSONArray jsaTo = new JSONArray().
				put(jsoTo);

		JSONObject jsoBody = new JSONObject();
		jsoBody.put("sender", jsoSender);
		jsoBody.put("to",jsaTo);
		jsoBody.put("subject", "Bienvenido al servicio de Soporte de MUEVE-Tic");

		jsoBody.put("hmtlContent", htmlContent);
		jsoBody.put("textContent", htmlContent);	
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(mediaType, jsoBody.toString());
		Request request = new Request.Builder()
				.url("https://api.brevo.com/v3/smtp/email")
				.post(body)
				.addHeader("accept", RUTA)
				.addHeader("api-key", "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")
				.addHeader("content-type", RUTA)
				.build();
		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}