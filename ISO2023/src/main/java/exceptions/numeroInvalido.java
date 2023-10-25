package exceptions;

@SuppressWarnings("serial")
public class numeroInvalido extends Exception{
	
	public numeroInvalido(String error) {
		super(error);
	}
}