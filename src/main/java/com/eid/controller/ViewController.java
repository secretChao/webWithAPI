package com.eid.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eid.service.GoAPI;

@Controller
public class ViewController {
	@Autowired
	GoAPI goAPI;

	@RequestMapping(value = { "/newPage" })
	public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {

		return "newPage";
	}

}
