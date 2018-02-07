package com.beatus.goodbyeq.company.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beatus.goodbyeq.company.model.CompanyDTO;
import com.beatus.goodbyeq.company.model.JSendResponse;
import com.beatus.goodbyeq.company.model.SearchResultsDTO;
import com.beatus.goodbyeq.company.service.api.CompanyService;
import com.beatus.goodbyeq.company.service.api.GoogleAPI;
import com.beatus.goodbyeq.company.service.exception.GoodByeQCompanyServiceException;
import com.beatus.goodbyeq.company.validation.exception.GoodByeQClientValidationException;
import com.beatus.goodbyeq.users.utils.Constants;
import com.beatus.goodbyeq.users.utils.GoodByeQMediaType;
import com.beatus.goodbyeq.users.utils.UsersUtilities;

@Controller
public class CompanyController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	private JSendResponse<CompanyDTO> jsend(CompanyDTO companyDTO) {
		if (companyDTO == null) {
			return new JSendResponse<CompanyDTO>(Constants.FAILURE, companyDTO);
		} else {
			return new JSendResponse<CompanyDTO>(Constants.SUCCESS, companyDTO);
		}
	}
	
	// For add and update bill both
		@RequestMapping(value = "/company/searchStoresByArea", method = RequestMethod.POST, consumes = {
				GoodByeQMediaType.APPLICATION_JSON }, produces = { GoodByeQMediaType.APPLICATION_JSON })
		public @ResponseBody JSendResponse<String> searchStoresByArea(@RequestBody String searchString, HttpServletRequest request,
				HttpServletResponse response) throws GoodByeQClientValidationException, GoodByeQCompanyServiceException {
			String METHOD_NAME = "searchStoresByArea()";
			long startTime = System.nanoTime();
			ArrayList<SearchResultsDTO> searchResultsByArea = null;
			String responseMessage = null;
			logger.debug(METHOD_NAME + "::searchString loaded:- " + searchString);
			try {		
				searchResultsByArea = GoogleAPI.getCoordinates(UsersUtilities.readJSONObject(searchString,"inputArea"));
				responseMessage = UsersUtilities.createStoresJSONObject(searchResultsByArea);
			}catch(Exception exception) {
				logger.error(METHOD_NAME + "::Exception has occurred due to " + exception.getLocalizedMessage()); 
			}
			return jsend(responseMessage);
		
		}

		@RequestMapping(value = "/hello", method = RequestMethod.GET, consumes = {
				GoodByeQMediaType.APPLICATION_JSON }, produces = { GoodByeQMediaType.APPLICATION_JSON })
		public @ResponseBody JSendResponse<String> test(String searchString, HttpServletRequest request,
				HttpServletResponse response) throws GoodByeQClientValidationException, GoodByeQCompanyServiceException {
			String METHOD_NAME = "test()";
			long startTime = System.nanoTime();
			ArrayList<SearchResultsDTO> searchResultsByArea = null;
			String responseMessage = "hi";
			logger.debug(METHOD_NAME + "::searchString loaded:- " + searchString);
			return jsend(responseMessage);
		
		}
}
