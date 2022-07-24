package com.eka.supplierconnect.model;

/**
 * @author Ranjan.Jha 
 * 
 * <Code>SupplierDocument</Code> POJO is a placeholder for segregating the requests sent from client(UI) 
 * and need to be posted on "{host:port}api/logistic/uploadDocument" where the uploaded file will be stored in BLOB.
 * 
 */

import java.io.File;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDocument {

	private String documentId;
	private byte[] uploadedFile;
	private String contentType;
	private String fileName;
	private String entityId;
	private String entityType;
	private String actionId;
	private String description;
	private String dateForDisplay;
	private File doc;
	private String cpID;
	private String internalRefNo;
	private String uploadedByName;

	public byte[] getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(byte[] uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getDoc() {
		return doc;
	}

	public void setDoc(File doc) {
		this.doc = doc;
	}

	public String getDateForDisplay() {
		return dateForDisplay;
	}

	public void setDateForDisplay(String dateForDisplay) {
		this.dateForDisplay = dateForDisplay;
	}

	public String getCpID() {
		return cpID;
	}

	public void setCpID(String cpID) {
		this.cpID = cpID;
	}

	public String getInternalRefNo() {
		return internalRefNo;
	}

	public void setInternalRefNo(String internalRefNo) {
		this.internalRefNo = internalRefNo;
	}

	public String getUploadedByName() {
		return uploadedByName;
	}

	public void setUploadedByName(String uploadedByName) {
		this.uploadedByName = uploadedByName;
	}

	@Override
	public String toString() {
		return "SupplierDocument [documentId=" + documentId + ", uploadedFile=" + Arrays.toString(uploadedFile)
				+ ", contentType=" + contentType + ", fileName=" + fileName + ", entityId=" + entityId + ", entityType="
				+ entityType + ", actionId=" + actionId + ", description=" + description + ", dateForDisplay="
				+ dateForDisplay + ", doc=" + doc + ", cpID=" + cpID + ", internalRefNo=" + internalRefNo
				+ ", uploadedByName=" + uploadedByName + ", getUploadedFile()=" + Arrays.toString(getUploadedFile())
				+ ", getContentType()=" + getContentType() + ", getFileName()=" + getFileName() + ", getEntityId()="
				+ getEntityId() + ", getEntityType()=" + getEntityType() + ", getDocumentId()=" + getDocumentId()
				+ ", getActionId()=" + getActionId() + ", getDescription()=" + getDescription() + ", getDoc()="
				+ getDoc() + ", getDateForDisplay()=" + getDateForDisplay() + ", getCpID()=" + getCpID()
				+ ", getInternalRefNo()=" + getInternalRefNo() + ", getUploadedByName()=" + getUploadedByName()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	

}
