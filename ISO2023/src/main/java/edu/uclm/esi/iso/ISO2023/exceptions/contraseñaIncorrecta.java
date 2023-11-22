package edu.uclm.esi.iso.ISO2023.exceptions;
/*Excepción personalizada que representa la invalidez de las contraseñas al no ser correcta*/

public class contraseñaIncorrecta extends Exception{
@SuppressWarnings("serial")
		
	public contraseñaIncorrecta(String error) {
			super(error);
	}
}

