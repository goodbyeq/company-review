package com.beatus.goodbyeq.company.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.beatus.goodbyeq.company.model.CompanyDTO;
import com.beatus.goodbyeq.company.service.api.CompanyService;
import com.beatus.goodbyeq.company.validation.CompanyValidator;
import com.beatus.goodbyeq.company.validation.exception.GoodByeQValidationException;

@Component("companyService")
public class CompanyServiceImpl implements CompanyService{
	
	@Resource(name = "companyValidator")
	private CompanyValidator companyValidator;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Override
	public String addCompany(HttpServletRequest request, HttpServletResponse response, CompanyDTO companyDTO) throws GoodByeQValidationException {
		/*if (companyDTO == null) {
			throw new GoodByeQValidationException("Bill data cant be null");
		}
		try {
			// Revisit validator
			boolean isValidated = companyValidator.validateCompanyData(companyDTO);
			if (isValidated) {
				String companyID = companyDTO.getCompanyID();
				CompanyDTO existingCompany = null;
				if (StringUtils.isNotBlank(companyID)) {
					existingCompany = getCompanyByID(companyID);
					return updateCompany(request, response, companyDTO);
				}
				BillData billData = populateBillData(billDTO, existingCompany, companyID);
				return billRepository.addBill(billData);
			}
		} catch (GoodByeQCompanyServiceException goodByeQCompanyServiceException) {
			LOGGER.error("Billlive Service Exception in the updateBillService() {} ", goodByeQCompanyServiceException.getMessage());
			throw billException;
		}*/
		return "N";
	}
	/*
	public String updateCompany(HttpServletRequest request, HttpServletResponse response, CompanyDTO companyDTO)
			throws GoodByeQValidationException {

		if (companyDTO == null) {
			throw new GoodByeQValidationException("Bill data cant be null");
		}
		try {
			// Validate company DTO
			boolean isValidated = companyValidator.validateCompanyDTO(companyDTO);
			if (isValidated) {
				String companyID = companyDTO.getCompanyID();
				CompanyDTO existingCompany = null;
				if (StringUtils.isNotBlank(companyDTO.getCompanyID()))
					existingCompany = getCompanyByID(companyID);
				return companyRepository.updateBill(billData);
			}
		} catch (GoodByeQCompanyServiceException companyServiceException) {
			LOGGER.error("GoodByeQCompany Service Exception in the updateCompanyService() {} ", companyServiceException.getMessage());
			throw companyServiceException;
		}

		return "N";
	}
	
	public CompanyDTO getCompanyByID(String companyId) {
		LOGGER.info("In getBillByBillNumber method of Bill Service");
		if (StringUtils.isNotBlank(companyId)) {

			billRepository.getBillByBillNumber(companyId, billNumber, new OnGetDataListener() {

				@Override
		        public void onStart() {
		        }

		        @Override
		        public void onSuccess(DataSnapshot billSnapshot) {
		            BillData billData = billSnapshot.getValue(BillData.class);
		            billsList.add(billData);
		        	LOGGER.info(" The bill Snapshot Key is " + billSnapshot.getKey());
		        }

		        @Override
		        public void onFailed(DatabaseError databaseError) {
		           LOGGER.info("Error retrieving data");
		           throw new BillliveServiceException(databaseError.getMessage());
		        }
		    });
			if (billData != null && !Constants.YES.equalsIgnoreCase(billData.getIsRemoved())) {
				return populateBillDTO(billData);
			} else {
				return null;
			}
		} else {
			LOGGER.error(
					"Billlive Service Exception in the getBillByBillNumber() {},  CompanyId or Billnumber passed cant be null or empty string");
			throw new BillliveServiceException("Company Id or Bill Number passed cant be null or empty string");
		}
	}
	
	private BillData populateBillData(BillDTO billDTO, BillDTO existingBill, String companyId) {
		BillData billData = new BillData();
		billData.setBillFromContactId(billDTO.getBillFromContactId());
		billData.setBillToContactId(billDTO.getBillToContactId());
		if (existingBill == null && StringUtils.isBlank(billDTO.getBillNumber())) {
			billData.setBillNumber(Utils.generateRandomKey(20));
			billDTO.setBillNumber(Utils.generateRandomKey(20));
		}
		if (existingBill != null) {
			billData.setBillNumber(billDTO.getBillNumber());
			billData.setYear(existingBill.getYear());
			billData.setMonth(existingBill.getMonth());
			billData.setDay(existingBill.getDay());
		} else {
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			billData.setYear(String.valueOf(cal.get(Calendar.YEAR)));
			billData.setMonth(String.valueOf(cal.get(Calendar.MONTH)));
			billData.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		}
		billData.setIsTaxeble(billDTO.getIsTaxeble());
		billData.setDateOfBill(billDTO.getDateOfBill());
		billData.setDueDate(billDTO.getDueDate());
		billData.setCompanyId(billDTO.getCompanyId());
		billData.setReferenceAadharCardNumber(billDTO.getReferenceAadharCardNumber());
		billData.setReferenceMobileNumber(billDTO.getReferenceMobileNumber());
		billData.setTotalTax(billDTO.getTotalTax());
		billData.setTotalCGST(billDTO.getTotalCGST());
		billData.setTotalSGST(billDTO.getTotalSGST());
		billData.setTotalIGST(billDTO.getTotalIGST());
		List<BillItemData> billItems = new ArrayList<BillItemData>();
		for (ItemDTO itemDTO : billDTO.getItems()) {
			BillItemData billItem = new BillItemData();
			if (Constants.YES.equalsIgnoreCase(itemDTO.getIsAdded())
					|| Constants.YES.equalsIgnoreCase(itemDTO.getIsUpdated())) {
				if(ItemType.PRODUCT.equals(itemDTO.getItemType())){
					// Get Item Details
					ItemData existingItem = itemService.getItemById(companyId, itemDTO.getItemId());
					billItem.setItemId(itemDTO.getItemId());
					billItem.setInventoryId(itemDTO.getInventoryId());
					billItem.setIsTaxeble(itemDTO.getIsTaxeble());
					billItem.setQuantity(itemDTO.getQuantity());
					billItem.setItemValue(itemDTO.getItemValue());
					billItem.setQuantityType(itemDTO.getQuantityType());
					billItem.setTotalCGST(itemDTO.getTotalCGST());
					billItem.setTotalSGST(itemDTO.getTotalSGST());
					billItem.setTotalIGST(itemDTO.getTotalIGST());
					Double amountBeforeTax = itemDTO.getItemValue() * itemDTO.getQuantity();
					if (itemDTO.getAmountBeforeTax() == null) {
						billItem.setAmountBeforeTax(amountBeforeTax);
					} else {
						billItem.setAmountBeforeTax(itemDTO.getAmountBeforeTax());
					}
					Double taxAmountForItem = 0.0;
					Double taxPercentage = 0.0;
					if(itemDTO.getTaxPercentage() == null){
						Tax tax = taxService.getTaxById(companyId, itemDTO.getTaxId());
						if(tax != null){
							taxPercentage = tax.getTotalTaxPercentage();
							billItem.setTaxId(itemDTO.getTaxId());
							billItem.setTaxPercentage(taxPercentage);
						}
					}else {
						taxPercentage = itemDTO.getTaxPercentage();
						billItem.setTaxId(itemDTO.getTaxId());
						billItem.setTaxPercentage(taxPercentage);
					}
					if (Constants.YES.equalsIgnoreCase(itemDTO.getIsTaxeble())) {
						if (itemDTO.getTaxAmountForItem() == null) {
							taxAmountForItem = Utils.calculateTaxAmount(amountBeforeTax, taxPercentage);
							billItem.setTaxAmountForItem(taxAmountForItem);
						} else {
							billItem.setTaxAmountForItem(itemDTO.getTaxAmountForItem());
						}
					} else {
						taxAmountForItem = 0.00;
					}
					if (itemDTO.getAmountAfterTax() == null) {
						Double amountAfterTax = amountBeforeTax + taxAmountForItem;
						billItem.setAmountAfterTax(amountAfterTax);
					} else {
						billItem.setAmountAfterTax(itemDTO.getAmountAfterTax());
					}
					billItem.setDiscount(itemDTO.getDiscount());
					Double marginAmount = itemDTO.getMarginAmount();
					if (marginAmount == null) {
						marginAmount = Utils.calculateMarginAmount(existingItem, itemDTO);
						billItem.setMarginAmount(marginAmount);
					}
					if (itemDTO.getTaxOnMargin() == null) {
						billItem.setTaxOnMargin(Utils.calculateTaxOnMargin(marginAmount, taxPercentage));
					}
				}else if(ItemType.EXPENSE.equals(itemDTO.getItemType()) || ItemType.SERVICE.equals(itemDTO.getItemType())){
					billItem.setItemId(itemDTO.getItemId());
					billItem.setItemValue(itemDTO.getItemValue());
					billItem.setTotalCGST(itemDTO.getTotalCGST());
					billItem.setTotalSGST(itemDTO.getTotalSGST());
					billItem.setTotalIGST(itemDTO.getTotalIGST());
					Double taxAmountForItem = 0.0;
					Double taxPercentage = 0.0;
					if(itemDTO.getTaxPercentage() == null){
						Tax tax = taxService.getTaxById(companyId, itemDTO.getTaxId());
						if(tax != null){
							taxPercentage = tax.getTotalTaxPercentage();
							billItem.setTaxId(itemDTO.getTaxId());
							billItem.setTaxPercentage(taxPercentage);
						}
					}else {
						taxPercentage = itemDTO.getTaxPercentage();
						billItem.setTaxId(itemDTO.getTaxId());
						billItem.setTaxPercentage(taxPercentage);
					}
					if (Constants.YES.equalsIgnoreCase(itemDTO.getIsTaxeble())) {
						if (itemDTO.getTaxAmountForItem() == null) {
							taxAmountForItem = Utils.calculateTaxAmount(itemDTO.getItemValue(), taxPercentage);
							billItem.setTaxAmountForItem(taxAmountForItem);
						} else {
							billItem.setTaxAmountForItem(itemDTO.getTaxAmountForItem());
						}
					} else {
						taxAmountForItem = 0.00;
					}
					if (itemDTO.getAmountAfterTax() == null) {
						Double amountAfterTax = itemDTO.getItemValue() + taxAmountForItem;
						billItem.setAmountAfterTax(amountAfterTax);
					} else {
						billItem.setAmountAfterTax(itemDTO.getAmountAfterTax());
					}
				}
			} else if (billData != null && Constants.YES.equalsIgnoreCase(itemDTO.getIsDeleted())) {
				for (BillItemData existingBillItem : billData.getBillItems()) {
					if (existingBillItem.getItemId().equalsIgnoreCase(itemDTO.getItemId())) {
						billItem = existingBillItem;
						billItem.setIsRemoved(Constants.YES);
					}
				}
			}
			billItems.add(billItem);
		}
		billData.setBillItems(billItems);
		return billData;
	}*/

}
