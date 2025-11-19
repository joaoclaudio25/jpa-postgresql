package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;

//SerieRepository → interface que acessa o banco de dados (Spring Data JPA).
import br.com.alura.screenmatch.repository.SerieRepository;

//@Autowired → injeta dependências automaticamente.
import org.springframework.beans.factory.annotation.Autowired;

//CommandLineRunner → permite executar código ao iniciar o programa.
import org.springframework.boot.CommandLineRunner;

//SpringApplication → classe usada para iniciar a aplicação.
import org.springframework.boot.SpringApplication;
//@SpringBootApplication → habilita toda a configuração padrão do Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
➡️ @SpringBootApplication
Marca esta classe como o ponto central da aplicação Spring Boot.
Ela ativa:
- configuração automática
- varredura de componentes
- suporte ao Spring Boot

➡️ A classe implementa CommandLineRunner,
o que significa que ela executará o metodo run() após a inicialização.
 */

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	//@Autowired injeta automaticamente uma instância de SerieRepository.
	@Autowired
	private SerieRepository repositorio; //repositorio será usado pela classe Principal para acessar o banco de dados.


	public static void main(String[] args) {
		//SpringApplication.run(...) inicia o Spring Boot e o seu ecossistema.
		//Aqui o servidor embutido (Tomcat) também é iniciado, caso exista API.
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	//Este metodo é executado automaticamente quando a aplicação termina de iniciar.
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();
	}
}
