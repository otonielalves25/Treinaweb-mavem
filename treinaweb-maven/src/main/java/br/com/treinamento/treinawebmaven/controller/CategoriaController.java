package br.com.treinamento.treinawebmaven.controller;

import java.io.IOException;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.treinamento.treinawebmaven.dao.CategoriaDao;
import br.com.treinamento.treinawebmaven.modelo.Categoria;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaDao categoriaDao;

	// LISTA TODAS PESSOAS
	// /////////////////////////////////////////////////////////////////////////
	@RequestMapping({ "/", "" })
	public String inicio(Model model) {
		model.addAttribute("categorias", categoriaDao.findAll(Sort.by("nomeCategoria")));
		
		return "categoria/listar_categoria";
	}

	// LIMPAR A TELA
	// /////////////////////////////////////////////////////////////////////////
	@RequestMapping("/cadastrar")
	public String limparTela(Categoria categoria) {
		return "categoria/cadastrar_categoria";
	}

	// SALVANDO NOVO NO BANCO
	// /////////////////////////////////////////////////////////////////////////
	@PostMapping("/salvar")
	public String salvar(@Valid Categoria categoria, BindingResult erros, RedirectAttributes redirecionar, @RequestParam("imagemProduto") MultipartFile file,
			Model model) {

		if (erros.hasErrors()) {			
			return "categoria/cadastrar";
		}
		
		try {
			categoria.setImagem(file.getBytes());
		} catch (IOException e) {
			
		}
		
		if (categoria.getId() == null) {
			categoriaDao.save(categoria);
			
			model.addAttribute("msg", "Cadastrado com Sucesso.");
		} else {
			categoriaDao.save(categoria);
			model.addAttribute("msg", "Alterado com Sucesso.");
		}

		return "redirect:/categoria";
	}

	// BUSCA POR ID
	// /////////////////////////////////////////////////////////////////////////
	@GetMapping("/{id}")
	public String buscaPorId(Model model, @PathVariable("id") Integer codigo) {
		model.addAttribute("categoria", categoriaDao.findById(codigo));
		return "categoria/cadastrar_categoria";
	}

	// DELETANDO
	// /////////////////////////////////////////////////////////////////////////
	@GetMapping("/deletar/{id}")
	public String deletar(Model model, @PathVariable("id") Integer codigo) {
		categoriaDao.deleteById(codigo);
		return "redirect:/categoria";

	}

	// RETORNADO PARA ALTERAÇÃO
	// /////////////////////////////////////////////////////////////////////////
	@GetMapping("/retorna")
	
	public String retorna(Integer id, Model model) {
		model.addAttribute("categoria", categoriaDao.findById(id));	
		model.addAttribute("idCat", id);
		model.addAttribute("caminho", "/categoria/imagem/"+id);
		return "categoria/cadastrar_categoria";
	}
	
	@GetMapping("/imagem/{id}")
	@ResponseBody
	public byte[] exibirImagem( @PathVariable("id") Integer codigo){
	Categoria categoria = null;	
	Optional<Categoria> categoriaOpt = categoriaDao.findById(codigo);
	
	if(categoriaOpt.isPresent()) {
		categoria = categoriaOpt.get();	
		return categoria.getImagem();
	}else {
		return null;
	}
	
	}

}
