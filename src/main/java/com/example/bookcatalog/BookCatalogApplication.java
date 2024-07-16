package com.example.bookcatalog;

import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.service.AuthorService;
import com.example.bookcatalog.service.BookService;
import com.example.bookcatalog.service.GutendexService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class BookCatalogApplication implements CommandLineRunner {

	@Autowired
	private BookService bookService;

	@Autowired
	private GutendexService gutendexService;

	@Autowired
	private AuthorService authorService;

	public static void main(String[] args) {
		SpringApplication.run(BookCatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;

		while (!exit) {
			System.out.println("1. Buscar libro por título");
			System.out.println("2. Listar libros registrados");
			System.out.println("3. Listar autores registrados");
			System.out.println("4. Listar autores vivos en un año determinado");
			System.out.println("5. Listar libros por idioma");
			System.out.println("6. Salir");
			int option = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			switch (option) {
				case 1:
					System.out.println("Ingrese el título del libro:");
					String title = scanner.nextLine();
					JsonObject bookJson = gutendexService.searchBook(title);
					if (bookJson != null) {
						String authorName = bookJson.getAsJsonArray("authors").get(0).getAsJsonObject().get("name").getAsString();
						String[] authorParts = authorName.split(", ");
						String authorLastName = authorParts[0];
						String authorFirstName = authorParts[1];
						String language = "EN"; // Asumiendo inglés como idioma
						int downloads = 6493; // Número de descargas fijo

						Book book = new Book();
						book.setTitle(title);
						book.setAuthorLastName(authorLastName);
						book.setAuthorFirstName(authorFirstName);
						book.setLanguage(language);
						book.setDownloads(downloads);

						if (bookService.findBookByTitle(title) == null) {
							bookService.addBook(book);
							System.out.println("Libro registrado exitosamente.");
						} else {
							System.out.println("El libro ya está registrado en la base de datos.");
						}
					} else {
						System.out.println("Libro no encontrado.");
					}
					break;

				case 2:
					System.out.println("Lista de libros registrados:");
					bookService.listBooks().forEach(b -> System.out.println(b.getTitle()));
					break;

				case 3:
					System.out.println("Lista de autores registrados:");
					authorService.listAuthors().forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
					break;


				case 4:
					System.out.println("Ingrese el año:");
					int year = scanner.nextInt();
					scanner.nextLine(); // Limpiar el buffer
					System.out.println("Lista de autores vivos en el año " + year + ":");
					authorService.listLivingAuthors(year).forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
					break;

				case 5:
					System.out.println("Ingrese el idioma:");
					String language = scanner.nextLine();
					System.out.println("Lista de libros en " + language + ":");
					bookService.listBooksByLanguage(language).forEach(b -> System.out.println(b.getTitle()));
					break;

				case 6:
					exit = true;
					break;

				default:
					System.out.println("Opción no válida. Inténtelo de nuevo.");
					break;
			}
		}
		scanner.close();
	}
}