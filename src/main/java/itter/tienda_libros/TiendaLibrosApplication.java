package itter.tienda_libros;

import itter.tienda_libros.vista.LibroForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class TiendaLibrosApplication {

	public static void main(String[] args) {
		//Definimos un objeto de la fabrica de Spring
		ConfigurableApplicationContext contextSpring =
				new SpringApplicationBuilder(TiendaLibrosApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);
		//Ejecutamos el codigo para cargar el formulario
		EventQueue.invokeLater(()->{
			//Obtenemos el objeto form a traves de Spring
			LibroForm libroForm = contextSpring.getBean(LibroForm.class);
			libroForm.setVisible(true);
		});
	}

}
