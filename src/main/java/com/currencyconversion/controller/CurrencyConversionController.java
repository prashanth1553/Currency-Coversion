package com.currencyconversion.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.currencyconversion.dto.CurrencyConversionBean;
import com.currencyconversion.exception.CustomResponseException;
import com.currencyconversion.services.CurrencyExchangeService;

@RestController
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CurrencyExchangeService currencyExchangeService;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
				uriVariables);

		CurrencyConversionBean response = responseEntity.getBody();

		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public ResponseEntity<CurrencyConversionBean> convertCurrencyFeign(@PathVariable String from,
			@PathVariable String to, @PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = currencyExchangeService.retrieveExchangeValue(from, to);
		CurrencyConversionBean responseBean = null;
		logger.info("{}", response);
		if (response != null) {
			responseBean = new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(),
					quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
		} else {
			//throw new CustomResponseException("From Hystric fallback");
		}
		ResponseEntity<CurrencyConversionBean> entity = new ResponseEntity<>(responseBean, HttpStatus.OK);
		return entity;
	}
}