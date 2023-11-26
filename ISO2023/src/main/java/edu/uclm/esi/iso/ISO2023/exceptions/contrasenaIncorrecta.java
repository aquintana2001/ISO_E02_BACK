package edu.uclm.esi.iso.ISO2023.exceptions;
/*Excepción personalizada que representa la invalidez de las contraseñas al no ser correcta*/

public class contrasenaIncorrecta extends Exception{
@SuppressWarnings("serial")
		
	public contrasenaIncorrecta(String error) {
			super(error);
	}
}

