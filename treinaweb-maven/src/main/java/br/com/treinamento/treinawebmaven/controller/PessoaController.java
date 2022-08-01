package br.com.treinamento.treinawebmaven.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.treinamento.treinawebmaven.dao.CategoriaDao;
import br.com.treinamento.treinawebmaven.dao.PessoaDao;
import br.com.treinamento.treinawebmaven.modelo.Pessoa;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private CategoriaDao categoriaDao;

	// LISTA TODAS PESSOAS
	@RequestMapping({ "/", "" })
	public String listar(Model model) {
		model.addAttribute("pessoas", pessoaDao.findAll(Sort.by("nome")));
		return "pessoa/listar_pessoa";
	}

	// LIMPAR A TELA
	@RequestMapping("/cadastrar")
	public String limparTela(Model model, Pessoa pessoa) {

		model.addAttribute("categorias", categoriaDao.findAll(Sort.by("nomeCategoria")));
		return "pessoa/cadastrar_pessoa";
	}

	// SALVANDO NOVO NO BANCO
	@PostMapping("/salvar")
	public String inserir(@Valid Pessoa pessoa, BindingResult erros, RedirectAttributes redirecionar, Model model) {


		if (erros.hasErrors()) {
			model.addAttribute("categorias", categoriaDao.findAll(Sort.by("nomeCategoria")));
			model.addAttribute("pessoa", pessoa);
			return "/pessoa/cadastrar_pessoa";
		}

		if (pessoa.getId() == null) {
			pessoaDao.save(pessoa);
			redirecionar.addFlashAttribute("msg", "Cadastrado com Sucesso.");
			return "redirect:/pessoa/cadastrar";
		} else {
			pessoaDao.save(pessoa);
			redirecionar.addFlashAttribute("msg", "Alterado com Sucesso.");
			return "redirect:/pessoa/";
		}

	}

	// BUSCA POR ID
	@GetMapping("/{id}")
	public String buscaPorId(Model model, @PathVariable("id") Integer codigo) {
		model.addAttribute("pessoa", pessoaDao.findById(codigo));
		return "pessoa/cadastrar_pessoa";
	}

	// DELETANDO
	@GetMapping("/deletar/{id}")
	public String deletar(Model model, @PathVariable("id") Integer codigo) {
		pessoaDao.deleteById(codigo);
		return "redirect:/pessoa";
	}

	// RETORNADO PARA ALTERAÇÃO
	// /////////////////////////////////////////////////////////////////////////
	@GetMapping("/retorna")
	public String retorna(Integer id, Model model) {
		model.addAttribute("pessoa", pessoaDao.findById(id));
		model.addAttribute("categorias", categoriaDao.findAll(Sort.by("nomeCategoria")));
		return "pessoa/cadastrar_pessoa";
	}

}
