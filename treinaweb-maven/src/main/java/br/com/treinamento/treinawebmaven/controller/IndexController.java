package br.com.treinamento.treinawebmaven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/treinamento")
public class IndexController {

	@RequestMapping({"/",""})
	public String inicio() {			
		return "index";
	}

}
