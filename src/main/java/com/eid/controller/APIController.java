package com.eid.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eid.service.GoAPI;

@RestController
public class APIController {
	@Autowired
	GoAPI goAPI;

	@GetMapping(value = "/getCert", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCert() {
		String json;
		try {
			json = goAPI.getCert();
		} catch (Exception e) {
			json = e.getMessage();
		}
		return json;
	}

	@PostMapping(value = "/doSign", produces = MediaType.APPLICATION_JSON_VALUE)
	public String doSign(@Valid @RequestParam String dtbsB64) {
		String json;
		try {
			json = goAPI.doSign(dtbsB64);
		} catch (Exception e) {
			json = e.getMessage();
		}
		return json;
	}

}
