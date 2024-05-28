package com.giglive;

import com.giglive.model.Cancion;
import com.giglive.model.Cartel;
import com.giglive.model.FragmentoCancion;
import com.giglive.model.Imagen;
import com.giglive.service.CancionService;
import com.giglive.service.ServicioCartel;
import com.giglive.service.ServicioFragmentoCancion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.giglive.service.ImagenService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class GigliveApplication {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(GigliveApplication.class, args);
		Scanner scanner = new Scanner(System.in);



		// Obtenemos los beans de los servicios necesarios
		ImagenService imagenService = ctx.getBean(ImagenService.class);
		ServicioCartel cartelServicios = ctx.getBean(ServicioCartel.class);
		CancionService cancionService = ctx.getBean(CancionService.class);
		ServicioFragmentoCancion fragmentoCancionServicios = ctx.getBean(ServicioFragmentoCancion.class);

		// Menú principal
		while (true) {
			System.out.println("1. Insertar imagen desde el explorador de archivos");
			System.out.println("2. Insertar canción desde el explorador de archivos");
			System.out.println("3. Extraer y mostrar una imagen");
			System.out.println("4. Extraer y reproducir una canción");
			System.out.println("5. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = scanner.nextInt();
			scanner.nextLine(); // Consumir nueva línea

			switch (opcion) {
		/*		case 1:
					System.out.print("Ruta de la imagen: ");
					String rutaImagen = scanner.nextLine();
					try {
						insertarImagen(imagenService, rutaImagen);
					} catch (IOException e) {
						System.err.println("Error al insertar la imagen: " + e.getMessage());
					}
					break;
				case 2:
					System.out.print("Ruta de la canción: ");
					String rutaCancion = scanner.nextLine();
					try {
						insertarCancion(cancionService, rutaCancion);
					} catch (IOException e) {
						System.err.println("Error al insertar la canción: " + e.getMessage());
					}
					break;
			*/	case 3:
					System.out.print("ID de la imagen a extraer: ");
					int idImagen = scanner.nextInt();
					mostrarImagen(cartelServicios, idImagen);
					break;
				case 4:
					System.out.print("ID de la canción a extraer: ");
					int idCancion = scanner.nextInt();
					reproducirCancion(fragmentoCancionServicios, idCancion);
					break;
				case 5:
					System.out.println("Saliendo...");
					return;
				default:
					System.out.println("Opción no válida");
			}
		}
	}

/*
	private static void insertarImagen(ImagenService imagenService, String rutaImagen) throws IOException {
		File imagenFile = new File(rutaImagen);
		byte[] imagenBytes = Files.readAllBytes(imagenFile.toPath());
		imagenService.guardarImagen(new ByteArrayResource(imagenBytes));
		System.out.println("Imagen insertada correctamente.");
	}

	private static void insertarCancion(CancionService cancionService, String rutaCancion) throws IOException {
		File cancionFile = new File(rutaCancion);
		byte[] cancionBytes = Files.readAllBytes(cancionFile.toPath());
		cancionService.guardarCancion(new ByteArrayResource(cancionBytes));
		System.out.println("Canción insertada correctamente.");
	}
*/
	private static void mostrarImagen(ServicioCartel servicioCartel, int idImagen) {
		Optional<Cartel> imagenOptional = servicioCartel.obtenerCartel(idImagen);
		imagenOptional.ifPresentOrElse(
				imagen -> {
					try {
						byte[] imagenBytes = imagen.getImagen();
						Files.write(new File("imagen_recuperada.jpg").toPath(), imagenBytes);
						System.out.println("Imagen extraída y guardada como imagen_recuperada.jpg.");
					} catch (IOException e) {
						System.err.println("Error al guardar la imagen extraída: " + e.getMessage());
					}
				},
				() -> System.err.println("No se encontró la imagen con ID " + idImagen)
		);
	}

	private static void reproducirCancion(ServicioFragmentoCancion servicioFragmentoCancion, int idCancion) {
		Optional<FragmentoCancion> cancionOptional = servicioFragmentoCancion.findById(idCancion);
		cancionOptional.ifPresentOrElse(
				cancion -> {
					try {
						byte[] cancionBytes = cancion.getFragmento();
						Files.write(new File("cancion_recuperada.mp3").toPath(), cancionBytes);
						System.out.println("Canción extraída y guardada como cancion_recuperada.mp3.");
						// Lógica para reproducir la canción en tu aplicación móvil
					} catch (IOException e) {
						System.err.println("Error al guardar la canción extraída: " + e.getMessage());
					}
				},
				() -> System.err.println("No se encontró la canción con ID " + idCancion)
		);
	}

}
