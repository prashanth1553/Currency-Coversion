package com.currencyconversion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.currencyconversion.dto.CurrencyConversionBean;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class CurrencyExchangeService {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@HystrixCommand(fallbackMethod = "retrieveExchangeValueFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000") })

	public CurrencyConversionBean retrieveExchangeValue(String from, String to) {
		System.out.println("Executing original method");
		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
		System.out.println("completed original method");
		return response;
	}

	public CurrencyConversionBean retrieveExchangeValueFallback(String from, String to) {
		System.out.println("Failure*****");
		return null;
	}

}
