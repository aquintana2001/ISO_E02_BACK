package edu.uclm.esi.iso.ISO2023;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);    
    	System.out.println("Lanzada app desde APP.java");
    	}
}
