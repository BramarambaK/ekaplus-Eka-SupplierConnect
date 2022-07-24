package com.eka.supplierconnect.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryItem {
	private String tabRequestId;
	private Map entityVersionMap;
	private String totalNoOfRecords;
	private String listingCreatedDate;
	private String lisitngCreatedBy;
	private String lisitingUpdatedDate;
	private String listingUpdatedBy;
	private String issueDate;
	private String cpName;
	private String contractRefNo;
	private String contractType;
	private String productGroupType;
	private String assetclass;
	private String contractqty;
	private String contractStatus;
	private String deliveryItemRefNo;
	private String qualityName;
	private String quotaMonth;
	private String tollingServiceType;
	private String tradeType;
	private String itemStatus;
	private String attributes;
	private String location;
	private String traxysOrg;
	private String incotermLocation;
	private String qp;
	private String quotaQty;
	private String quotaQtyUnit;
	private String quotaQtyBasis;
	private String quotaOpenQty;
	private String quotaCalloffQty;
	private String quotaDeliveredReceivedQty;
	private String quotaInvoicedQty;
	private String quotaPriceFixedQty;
	private String allocatedQty;
	private String pcdiId;
	private String priceOptionCallOffStatus;
	private String physicalOptPresent;
	private String strategy;
	private String bookProfitCenter;
	private String trader;
	private String qpPricingBasis;
	private String qpPricing;
	private String pricing;
	private String quotaQuantityMax;
	private String quotaQuantityBasis;
	private String toBeCalledOffQty;
	private String calledOffQty;
	private String executedQty;
	private String pricingStatus;
	private String fixedPriceQty;
	private String titleTransferQty;
	private String provInvoicedQty;
	private String finalInvoicedQty;
	private String payInCurrency;
	private String fullfillmentStatus;
	private String passThrough;
	private String dealType;
	private String fullfillmentQty;
	private String orderLineNo	;
	private String isInterCompanyContract;
	private String interCompanyContractRefNo;
	private String rnum;
	private String internalContractRefNo;
	private String fromDate;
	private String toDate;
	private String openQty;
	private String incoLocationName;
	private String cpAddress;
	private String delLocation;
	private String executableQty;
	private String netOpenQty;
	public String getNetOpenQty() {
		return netOpenQty;
	}
	public void setNetOpenQty(String netOpenQty) {
		this.netOpenQty = netOpenQty;
	}
	public String getCpAddress() {
		return cpAddress;
	}
	public void setCpAddress(String cpAddress) {
		this.cpAddress = cpAddress;
	}
	public String getTabRequestId() {
		return tabRequestId;
	}
	public void setTabRequestId(String tabRequestId) {
		this.tabRequestId = tabRequestId;
	}
	public Map getEntityVersionMap() {
		return entityVersionMap;
	}
	public void setEntityVersionMap(Map entityVersionMap) {
		this.entityVersionMap = entityVersionMap;
	}
	public String getTotalNoOfRecords() {
		return totalNoOfRecords;
	}
	public void setTotalNoOfRecords(String totalNoOfRecords) {
		this.totalNoOfRecords = totalNoOfRecords;
	}
	public String getListingCreatedDate() {
		return listingCreatedDate;
	}
	public void setListingCreatedDate(String listingCreatedDate) {
		this.listingCreatedDate = listingCreatedDate;
	}
	public String getLisitngCreatedBy() {
		return lisitngCreatedBy;
	}
	public void setLisitngCreatedBy(String lisitngCreatedBy) {
		this.lisitngCreatedBy = lisitngCreatedBy;
	}
	public String getLisitingUpdatedDate() {
		return lisitingUpdatedDate;
	}
	public void setLisitingUpdatedDate(String lisitingUpdatedDate) {
		this.lisitingUpdatedDate = lisitingUpdatedDate;
	}
	public String getListingUpdatedBy() {
		return listingUpdatedBy;
	}
	public void setListingUpdatedBy(String listingUpdatedBy) {
		this.listingUpdatedBy = listingUpdatedBy;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getContractRefNo() {
		return contractRefNo;
	}
	public void setContractRefNo(String contractRefNo) {
		this.contractRefNo = contractRefNo;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getProductGroupType() {
		return productGroupType;
	}
	public void setProductGroupType(String productGroupType) {
		this.productGroupType = productGroupType;
	}
	public String getAssetclass() {
		return assetclass;
	}
	public void setAssetclass(String assetclass) {
		this.assetclass = assetclass;
	}
	public String getContractqty() {
		return contractqty;
	}
	public void setContractqty(String contractqty) {
		this.contractqty = contractqty;
	}
	public String getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	public String getDeliveryItemRefNo() {
		return deliveryItemRefNo;
	}
	public void setDeliveryItemRefNo(String deliveryItemRefNo) {
		this.deliveryItemRefNo = deliveryItemRefNo;
	}
	public String getQualityName() {
		return qualityName;
	}
	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
	}
	public String getQuotaMonth() {
		return quotaMonth;
	}
	public void setQuotaMonth(String quotaMonth) {
		this.quotaMonth = quotaMonth;
	}
	public String getTollingServiceType() {
		return tollingServiceType;
	}
	public void setTollingServiceType(String tollingServiceType) {
		this.tollingServiceType = tollingServiceType;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTraxysOrg() {
		return traxysOrg;
	}
	public void setTraxysOrg(String traxysOrg) {
		this.traxysOrg = traxysOrg;
	}
	public String getIncotermLocation() {
		return incotermLocation;
	}
	public void setIncotermLocation(String incotermLocation) {
		this.incotermLocation = incotermLocation;
	}
	public String getQp() {
		return qp;
	}
	public void setQp(String qp) {
		this.qp = qp;
	}
	public String getQuotaQty() {
		return quotaQty;
	}
	public void setQuotaQty(String quotaQty) {
		this.quotaQty = quotaQty;
	}
	public String getQuotaQtyUnit() {
		return quotaQtyUnit;
	}
	public void setQuotaQtyUnit(String quotaQtyUnit) {
		this.quotaQtyUnit = quotaQtyUnit;
	}
	public String getQuotaQtyBasis() {
		return quotaQtyBasis;
	}
	public void setQuotaQtyBasis(String quotaQtyBasis) {
		this.quotaQtyBasis = quotaQtyBasis;
	}
	public String getQuotaOpenQty() {
		return quotaOpenQty;
	}
	public void setQuotaOpenQty(String quotaOpenQty) {
		this.quotaOpenQty = quotaOpenQty;
	}
	public String getQuotaCalloffQty() {
		return quotaCalloffQty;
	}
	public void setQuotaCalloffQty(String quotaCalloffQty) {
		this.quotaCalloffQty = quotaCalloffQty;
	}
	public String getQuotaDeliveredReceivedQty() {
		return quotaDeliveredReceivedQty;
	}
	public void setQuotaDeliveredReceivedQty(String quotaDeliveredReceivedQty) {
		this.quotaDeliveredReceivedQty = quotaDeliveredReceivedQty;
	}
	public String getQuotaInvoicedQty() {
		return quotaInvoicedQty;
	}
	public void setQuotaInvoicedQty(String quotaInvoicedQty) {
		this.quotaInvoicedQty = quotaInvoicedQty;
	}
	public String getQuotaPriceFixedQty() {
		return quotaPriceFixedQty;
	}
	public void setQuotaPriceFixedQty(String quotaPriceFixedQty) {
		this.quotaPriceFixedQty = quotaPriceFixedQty;
	}
	public String getAllocatedQty() {
		return allocatedQty;
	}
	public void setAllocatedQty(String allocatedQty) {
		this.allocatedQty = allocatedQty;
	}
	public String getPcdiId() {
		return pcdiId;
	}
	public void setPcdiId(String pcdiId) {
		this.pcdiId = pcdiId;
	}
	public String getPriceOptionCallOffStatus() {
		return priceOptionCallOffStatus;
	}
	public void setPriceOptionCallOffStatus(String priceOptionCallOffStatus) {
		this.priceOptionCallOffStatus = priceOptionCallOffStatus;
	}
	public String getPhysicalOptPresent() {
		return physicalOptPresent;
	}
	public void setPhysicalOptPresent(String physicalOptPresent) {
		this.physicalOptPresent = physicalOptPresent;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public String getBookProfitCenter() {
		return bookProfitCenter;
	}
	public void setBookProfitCenter(String bookProfitCenter) {
		this.bookProfitCenter = bookProfitCenter;
	}
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	public String getQpPricingBasis() {
		return qpPricingBasis;
	}
	public void setQpPricingBasis(String qpPricingBasis) {
		this.qpPricingBasis = qpPricingBasis;
	}
	public String getQpPricing() {
		return qpPricing;
	}
	public void setQpPricing(String qpPricing) {
		this.qpPricing = qpPricing;
	}
	public String getPricing() {
		return pricing;
	}
	public void setPricing(String pricing) {
		this.pricing = pricing;
	}
	public String getQuotaQuantityMax() {
		return quotaQuantityMax;
	}
	public void setQuotaQuantityMax(String quotaQuantityMax) {
		this.quotaQuantityMax = quotaQuantityMax;
	}
	public String getQuotaQuantityBasis() {
		return quotaQuantityBasis;
	}
	public void setQuotaQuantityBasis(String quotaQuantityBasis) {
		this.quotaQuantityBasis = quotaQuantityBasis;
	}
	public String getToBeCalledOffQty() {
		return toBeCalledOffQty;
	}
	public void setToBeCalledOffQty(String toBeCalledOffQty) {
		this.toBeCalledOffQty = toBeCalledOffQty;
	}
	public String getCalledOffQty() {
		return calledOffQty;
	}
	public void setCalledOffQty(String calledOffQty) {
		this.calledOffQty = calledOffQty;
	}
	public String getExecutedQty() {
		return executedQty;
	}
	public void setExecutedQty(String executedQty) {
		this.executedQty = executedQty;
	}
	public String getPricingStatus() {
		return pricingStatus;
	}
	public void setPricingStatus(String pricingStatus) {
		this.pricingStatus = pricingStatus;
	}
	public String getFixedPriceQty() {
		return fixedPriceQty;
	}
	public void setFixedPriceQty(String fixedPriceQty) {
		this.fixedPriceQty = fixedPriceQty;
	}
	public String getTitleTransferQty() {
		return titleTransferQty;
	}
	public void setTitleTransferQty(String titleTransferQty) {
		this.titleTransferQty = titleTransferQty;
	}
	public String getProvInvoicedQty() {
		return provInvoicedQty;
	}
	public void setProvInvoicedQty(String provInvoicedQty) {
		this.provInvoicedQty = provInvoicedQty;
	}
	public String getFinalInvoicedQty() {
		return finalInvoicedQty;
	}
	public void setFinalInvoicedQty(String finalInvoicedQty) {
		this.finalInvoicedQty = finalInvoicedQty;
	}
	public String getPayInCurrency() {
		return payInCurrency;
	}
	public void setPayInCurrency(String payInCurrency) {
		this.payInCurrency = payInCurrency;
	}
	public String getFullfillmentStatus() {
		return fullfillmentStatus;
	}
	public void setFullfillmentStatus(String fullfillmentStatus) {
		this.fullfillmentStatus = fullfillmentStatus;
	}
	public String getPassThrough() {
		return passThrough;
	}
	public void setPassThrough(String passThrough) {
		this.passThrough = passThrough;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getFullfillmentQty() {
		return fullfillmentQty;
	}
	public void setFullfillmentQty(String fullfillmentQty) {
		this.fullfillmentQty = fullfillmentQty;
	}
	public String getOrderLineNo() {
		return orderLineNo;
	}
	public void setOrderLineNo(String orderLineNo) {
		this.orderLineNo = orderLineNo;
	}
	public String getIsInterCompanyContract() {
		return isInterCompanyContract;
	}
	public void setIsInterCompanyContract(String isInterCompanyContract) {
		this.isInterCompanyContract = isInterCompanyContract;
	}
	public String getInterCompanyContractRefNo() {
		return interCompanyContractRefNo;
	}
	public void setInterCompanyContractRefNo(String interCompanyContractRefNo) {
		this.interCompanyContractRefNo = interCompanyContractRefNo;
	}
	public String getRnum() {
		return rnum;
	}
	public void setRnum(String rnum) {
		this.rnum = rnum;
	}
	public String getInternalContractRefNo() {
		return internalContractRefNo;
	}
	public void setInternalContractRefNo(String internalContractRefNo) {
		this.internalContractRefNo = internalContractRefNo;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {		
			this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {		
			this.toDate = toDate;
	}
	public String getOpenQty() {
		return openQty;
	}
	public void setOpenQty(String openQty) {		
		this.openQty = openQty;
	}
	
	public String getIncoLocationName() {
		return incoLocationName;
	}
	public void setIncoLocationName(String incoLocationName) {
		this.incoLocationName = incoLocationName;
	}
	
	public String getDelLocation() {
		return delLocation;
	}
	public void setDelLocation(String delLocation) {
		this.delLocation = delLocation;
	}
	
	public String getExecutableQty() {
		return executableQty;
	}
	public void setExecutableQty(String executableQty) {
		this.executableQty = executableQty;
	}
	@Override
	public String toString() {
		return "DeliveryItem [tabRequestId=" + tabRequestId + ", entityVersionMap=" + entityVersionMap
				+ ", totalNoOfRecords=" + totalNoOfRecords + ", listingCreatedDate=" + listingCreatedDate
				+ ", lisitngCreatedBy=" + lisitngCreatedBy + ", lisitingUpdatedDate=" + lisitingUpdatedDate
				+ ", listingUpdatedBy=" + listingUpdatedBy + ", issueDate=" + issueDate + ", cpName=" + cpName
				+ ", contractRefNo=" + contractRefNo + ", contractType=" + contractType + ", productGroupType="
				+ productGroupType + ", assetclass=" + assetclass + ", contractqty=" + contractqty + ", contractStatus="
				+ contractStatus + ", deliveryItemRefNo=" + deliveryItemRefNo + ", qualityName=" + qualityName
				+ ", quotaMonth=" + quotaMonth + ", tollingServiceType=" + tollingServiceType + ", tradeType="
				+ tradeType + ", itemStatus=" + itemStatus + ", attributes=" + attributes + ", location=" + location
				+ ", traxysOrg=" + traxysOrg + ", incotermLocation=" + incotermLocation + ", qp=" + qp + ", quotaQty="
				+ quotaQty + ", quotaQtyUnit=" + quotaQtyUnit + ", quotaQtyBasis=" + quotaQtyBasis + ", quotaOpenQty="
				+ quotaOpenQty + ", quotaCalloffQty=" + quotaCalloffQty + ", quotaDeliveredReceivedQty="
				+ quotaDeliveredReceivedQty + ", quotaInvoicedQty=" + quotaInvoicedQty + ", quotaPriceFixedQty="
				+ quotaPriceFixedQty + ", allocatedQty=" + allocatedQty + ", pcdiId=" + pcdiId
				+ ", priceOptionCallOffStatus=" + priceOptionCallOffStatus + ", physicalOptPresent="
				+ physicalOptPresent + ", strategy=" + strategy + ", bookProfitCenter=" + bookProfitCenter + ", trader="
				+ trader + ", qpPricingBasis=" + qpPricingBasis + ", qpPricing=" + qpPricing + ", pricing=" + pricing
				+ ", quotaQuantityMax=" + quotaQuantityMax + ", quotaQuantityBasis=" + quotaQuantityBasis
				+ ", toBeCalledOffQty=" + toBeCalledOffQty + ", calledOffQty=" + calledOffQty + ", executedQty="
				+ executedQty + ", pricingStatus=" + pricingStatus + ", fixedPriceQty=" + fixedPriceQty
				+ ", titleTransferQty=" + titleTransferQty + ", provInvoicedQty=" + provInvoicedQty
				+ ", finalInvoicedQty=" + finalInvoicedQty + ", payInCurrency=" + payInCurrency
				+ ", fullfillmentStatus=" + fullfillmentStatus + ", passThrough=" + passThrough + ", dealType="
				+ dealType + ", fullfillmentQty=" + fullfillmentQty + ", orderLineNo=" + orderLineNo
				+ ", isInterCompanyContract=" + isInterCompanyContract + ", interCompanyContractRefNo="
				+ interCompanyContractRefNo + ", rnum=" + rnum + ", internalContractRefNo=" + internalContractRefNo
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", openQty=" + openQty + "]";
	}
	
	

}
