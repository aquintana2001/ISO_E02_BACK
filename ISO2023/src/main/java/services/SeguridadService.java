package services;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.User;
import exceptions.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class SeguridadService {
	public void restriccionesPassword(User usuario) throws contraseñaIncorrecta {
		
		/*restricciones de seguridad de contraseÃ±a*/
		
		if(usuario.getPassword().length() < 8)
			throw new contraseñaIncorrecta("Error. La contraseÃ±a tiene que tener un minimo de 8 caracteres");
		
		boolean contieneMayus = false;
		boolean contieneMinus = false;
		boolean contieneNumero = false;
		boolean contieneCaracterRaro = false;  
		
		for(int i = 0; i < usuario.getPassword().length() || (!contieneMayus && !contieneNumero && !contieneCaracterRaro && !contieneMinus); i++)
		{
			if(Character.isUpperCase(usuario.getPassword().charAt(i)))
				contieneMayus = true;
			
			if(Character.isLowerCase(usuario.getPassword().charAt(i)))
				contieneMayus = true;
			
			if(Character.isDigit(usuario.getPassword().charAt(i)))
				contieneNumero = true;	
			
			if(esCaracterRaro(usuario.getPassword().charAt(i)))
				contieneCaracterRaro = true;
		}
		
		if(!contieneMayus)
			throw new contraseñaIncorrecta("Error. La contraseÃ±a tiene que tener minimo una mayuscula");
		
		if(!contieneMinus)
			throw new contraseñaIncorrecta("Error. La contraseÃ±a tiene que tener minimo una minusccula");
		
		if(!contieneNumero)
			throw new contraseñaIncorrecta("Error. La contraseÃ±a tiene que tener minimo un numero");
		
		if(!contieneMayus)
			throw new contraseñaIncorrecta("Error. La contraseÃ±a tiene que tener minimo un caracter raro");
	}

	public static boolean esCaracterRaro(char c) {
	    // Patrón que coincide con cualquier carácter que no sea una letra, un número o un carácter de espacio.
	    Pattern patron = Pattern.compile("[^A-Za-z0-9\\s]");
	    Matcher matcher = patron.matcher(String.valueOf(c));
	    return matcher.matches();
	}
	
	public Boolean comprobarDni(String dni) {
		 
		// El DNI debe tener 9 caracteres
		if (dni.length() != 9) {
			return false;
		}

		// Comprobar que los primeros 8 caracteres son dígitos
		for (int i = 0; i < 8; i++) {
			if (!Character.isDigit(dni.charAt(i))) {
				return false;
			}
		}

		// Comprobar que el último carácter es una letra
		char letra = dni.charAt(8);
		if (!Character.isLetter(letra)) {
			return false;
		}

		return true;
	}
	
	public Boolean comprobarNumero(String telefono) {
		if(telefono.length() != 9)
			return false;
		
		//Evitar numeros extranjeros
		//if(telefono.charAt(0) != '6' && telefono.charAt(0) != '7' && telefono.charAt(0)!= '8' && telefono.charAt(0)!='9')
		//	return false;
		
		//Evitar letras en el numero
		for(int i=0;i< telefono.length();i++)
			if(!Character.isDigit(telefono.charAt(i)))
				return false;
		
		return true;
	}
	
	public boolean validarEmail(String email) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		return email.toLowerCase().matches(regex.toLowerCase());
	}
	
	public String cifrarPassword(String password) {
		PasswordEncoder ncoder = codificador();
		return ncoder.encode(password);
	}
	
	public PasswordEncoder codificador() {
		return new BCryptPasswordEncoder();
	}
}
