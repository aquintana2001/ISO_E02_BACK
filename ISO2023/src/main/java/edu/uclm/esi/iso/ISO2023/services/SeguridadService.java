package edu.uclm.esi.iso.ISO2023.services;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uclm.esi.iso.ISO2023.entities.User;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SeguridadService {
	public boolean restriccionesPassword(User usuario) throws contraseñaIncorrecta {
		
		/*restricciones de seguridad de contraseÃ±a*/
		
		if(usuario.getPassword().length() < 8)
			throw new contraseñaIncorrecta("Error. La contrasena tiene que tener un minimo de 8 caracteres");
		
		boolean seguro = false;
		boolean contieneMayus = false;
		boolean contieneMinus = false;
		boolean contieneNumero = false;
		boolean contieneCaracterRaro = false;  
		
		for(int i = 0; i < usuario.getPassword().length() || (!contieneMayus && !contieneNumero && !contieneCaracterRaro && !contieneMinus); i++)
		{
			if(Character.isUpperCase(usuario.getPassword().charAt(i)))
				contieneMayus = true;
			
			if(Character.isLowerCase(usuario.getPassword().charAt(i)))
				contieneMinus = true;
			
			if(Character.isDigit(usuario.getPassword().charAt(i)))
				contieneNumero = true;	
			
			if(esCaracterRaro(usuario.getPassword().charAt(i)))
				contieneCaracterRaro = true;
		}
		
		if(!contieneMayus)
			throw new contraseñaIncorrecta("Error. La contrasena tiene que tener minimo una mayuscula");
		
		if(!contieneMinus)
			throw new contraseñaIncorrecta("Error. La contrasena tiene que tener minimo una minusccula");
		
		if(!contieneNumero)
			throw new contraseñaIncorrecta("Error. La contrasena tiene que tener minimo un numero");
		
		if(!contieneCaracterRaro)
			throw new contraseñaIncorrecta("Error. La contrasena tiene que tener minimo un caracter raro");
		
		if(contieneMayus == true && contieneMinus == true  && contieneNumero == true  && contieneCaracterRaro == true  && usuario.getPassword().length() >=8) 
			return seguro = true;
		return seguro;
	}

	public static boolean esCaracterRaro(char c) {
	    // Patrón que coincide con cualquier carácter que no sea una letra, un número o un carácter de espacio.
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
                throw new numeroInvalido("El numero de dni introducido no es valido. Los 8 primeros digitos deben ser numeros");
            }
        }

        // Comprobar que el último carácter es una letra
        char letra = dni.charAt(8);
        if (!Character.isLetter(letra)) {
            throw new numeroInvalido("El nÃºmero de dni introducido no es vÃ¡lido. El ultimo caracter debe ser una letra.");

        }

        return true;
    }
	
	public Boolean comprobarNumero(String telefono) throws numeroInvalido {
        if(telefono.length() != 9)
            throw new numeroInvalido("El numero de telefono introducido no es valido. Introduzca nueve digitos");

        //Evitar numeros extranjeros
        //if(telefono.charAt(0) != '6' && telefono.charAt(0) != '7' && telefono.charAt(0)!= '8' && telefono.charAt(0)!='9')
        //    return false;

        //Evitar letras en el numero
        for(int i=0;i< telefono.length();i++) {
            if(!Character.isDigit(telefono.charAt(i)))
                throw new numeroInvalido("El numero de telefono introducido no es valido. Introduzca nueve digitos");

        }
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
	
	public boolean decodificador(String password, String passwordMongo) {
		PasswordEncoder ncoder = codificador();
		return ncoder.matches(password, passwordMongo);
	}
	
	public Boolean emailValido(String email) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		return email.toLowerCase().matches(regex.toLowerCase());
	}
}
