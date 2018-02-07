package com.beatus.goodbyeq.company.service.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beatus.goodbyeq.company.model.CompanyDTO;
import com.beatus.goodbyeq.company.validation.exception.GoodByeQValidationException;


public interface CompanyService {
	
	public String addCompany(HttpServletRequest request, HttpServletResponse response, CompanyDTO companyDTO) throws GoodByeQValidationException ;

}
