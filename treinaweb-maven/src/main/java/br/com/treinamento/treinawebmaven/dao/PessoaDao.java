package br.com.treinamento.treinawebmaven.dao;




import org.springframework.data.jpa.repository.JpaRepository;



import br.com.treinamento.treinawebmaven.modelo.Pessoa;

public interface PessoaDao extends JpaRepository<Pessoa, Integer>{

	Pessoa findByNome(String nome);

	//Pessoa findByNome(String nome);
	
	
}
