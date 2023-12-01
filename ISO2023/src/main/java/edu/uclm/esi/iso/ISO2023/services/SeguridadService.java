package edu.uclm.esi.iso.ISO2023.services;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;

import edu.uclm.esi.iso.ISO2023.entities.User;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

@Service

public class SeguridadService {

	public boolean restriccionesPassword(User usuario) throws contrasenaIncorrecta {

		/* restricciones de seguridad de contraseÃ±a */

		if (usuario.getPassword().length() < 8)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener un minimo de 8 caracteres");

		boolean seguro = false;
		boolean contieneMayus = false;
		boolean contieneMinus = false;
		boolean contieneNumero = false;
		boolean contieneCaracterRaro = false;

		for (int i = 0; i < usuario.getPassword().length()
				|| (!contieneMayus && !contieneNumero && !contieneCaracterRaro && !contieneMinus); i++) {
			if (Character.isUpperCase(usuario.getPassword().charAt(i)))
				contieneMayus = true;

			if (Character.isLowerCase(usuario.getPassword().charAt(i)))
				contieneMinus = true;

			if (Character.isDigit(usuario.getPassword().charAt(i)))
				contieneNumero = true;

			if (esCaracterRaro(usuario.getPassword().charAt(i)))
				contieneCaracterRaro = true;
		}

		lanzamientoErrores(contieneMayus, contieneMinus, contieneNumero, contieneCaracterRaro);

		if (contieneMayus && contieneMinus && contieneNumero && contieneCaracterRaro
				&& usuario.getPassword().length() >= 8)
			seguro = true;
		return seguro;
	}
	
	public boolean passwordSecure(String pwd) throws contrasenaIncorrecta {
		if (pwd.length() < 8)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener un minimo de 8 caracteres");

		boolean seguro = false;
		boolean contieneMayus = false;
		boolean contieneMinus = false;
		boolean contieneNumero = false;
		boolean contieneCaracterRaro = false;

		for (int i = 0; i < pwd.length()
				|| (!contieneMayus && !contieneNumero && !contieneCaracterRaro && !contieneMinus); i++) {
			if (Character.isUpperCase(pwd.charAt(i)))
				contieneMayus = true;

			if (Character.isLowerCase(pwd.charAt(i)))
				contieneMinus = true;

			if (Character.isDigit(pwd.charAt(i)))
				contieneNumero = true;

			if (esCaracterRaro(pwd.charAt(i)))
				contieneCaracterRaro = true;
		}

		lanzamientoErrores(contieneMayus, contieneMinus, contieneNumero, contieneCaracterRaro);

		if (contieneMayus && contieneMinus && contieneNumero && contieneCaracterRaro && pwd.length() >= 8)
			seguro = true;
		return seguro;
	}


	public void lanzamientoErrores(boolean may, boolean min, boolean num, boolean car) throws contrasenaIncorrecta {
		if (!may)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener minimo una mayuscula");

		if (!min)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener minimo una minusccula");

		if (!num)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener minimo un numero");

		if (!car)
			throw new contrasenaIncorrecta("Error. La contrasena tiene que tener minimo un caracter raro");
	}

	public static boolean esCaracterRaro(char c) {
		// Patrón que coincide con cualquier carácter que no sea una letra, un número o
		// un carácter de espacio.
		Pattern patron = Pattern.compile("[^A-Za-z0-9\\s]");
		Matcher matcher = patron.matcher(String.valueOf(c));
		return matcher.matches();
	}

	public Boolean comprobarDni(String dni) throws numeroInvalido {

		// El DNI debe tener 9 caracteres
		if (dni.length() != 9)
			throw new numeroInvalido("El numero de dni introducido no es valido. Introduzca nueve digitos");

		// Comprobar que los primeros 8 caracteres son dígitos
		for (int i = 0; i < 8; i++) {
			if (!Character.isDigit(dni.charAt(i))) {

				throw new numeroInvalido(
						"El numero de dni introducido no es valido. Los 8 primeros digitos deben ser numeros");

			}
		}

		// Comprobar que el último carácter es una letra
		char letra = dni.charAt(8);
		if (!Character.isLetter(letra)) {

			throw new numeroInvalido(
					"El nÃºmero de dni introducido no es vÃ¡lido. El ultimo caracter debe ser una letra.");

		}

		return true;
	}

	public Boolean comprobarNumero(String telefono) throws numeroInvalido {
		if (telefono.length() != 9)
			throw new numeroInvalido("El numero de telefono introducido no es valido. Introduzca nueve digitos");

		// Evitar letras en el numero
		for (int i = 0; i < telefono.length(); i++) {
			if (!Character.isDigit(telefono.charAt(i)))
				throw new numeroInvalido("El numero de telefono introducido no es valido. Introduzca nueve digitos");

		}
		return true;
	}

	public boolean validarEmail(String email) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		return email.toLowerCase().matches(regex.toLowerCase());
	}
	//BCrypt utiliza un hash adaptativo de 60 carácteres, lo que significa que puede ajustar su costo computacional a 
	//lo largo del tiempo, lo que lo hace resistente a los ataques de fuerza bruta y de diccionario. 
	public String cifrarPassword(String password) {
		PasswordEncoder ncoder = codificador();
		return ncoder.encode(password);
	}

	public PasswordEncoder codificador() {
		return new BCryptPasswordEncoder();
	}

	public boolean decodificador(String password, String passwordMongo) {
		PasswordEncoder ncoder = codificador();
		return ncoder.matches(password, passwordMongo);
	}

	//Metodos encargados del doble factor de autentificación
	private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

	public String generateSecretKey() {
		final GoogleAuthenticatorKey key = gAuth.createCredentials();
		return key.getKey();
	}

	//Metodo utilizado para generar QR
	public byte[] generateQRCodeImage(String secretKey, String username) throws WriterException, IOException {
		String otpAuthURL = getOtpAuthURL("YourIssuer", username, secretKey);
		return generateQRCode(otpAuthURL, 200);
	}

	private String getOtpAuthURL(String issuer, String accountName, String secretKey) {
		return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, accountName, secretKey, issuer);
	}

	private byte[] generateQRCode(String text, int height) throws WriterException, IOException {
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hintMap);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		return outputStream.toByteArray();
	}

	public boolean verifyCode(String secretKey, int code) {
		return gAuth.authorize(secretKey, code);
	}

}
