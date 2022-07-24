package com.eka.supplierconnect.model;

import java.util.Arrays;

public class MailInfo {

	private String fromAddress;
	private String[] toAddress;
	private String[] ccAddress;
	private String[] bccAddress;
	private String subject;
	private String message;
	private String[] fileNames;
	private String contentType;
	private AbstractFileModel fileModel;
	private String[] docId;

	public String[] getDocId() {
		return docId;
	}

	public void setDocId(String[] docId) {
		this.docId = docId;
	}

	public AbstractFileModel getFileModel() {
		return fileModel;
	}

	public void setFileModel(AbstractFileModel fileModel) {
		this.fileModel = fileModel;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String[] getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String[] bccAddress) {
		this.bccAddress = bccAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailInfo [fromAddress=").append(fromAddress).append(", toAddress=")
				.append(Arrays.toString(toAddress)).append(", ccAddress=").append(Arrays.toString(ccAddress))
				.append(", bccAddress=").append(Arrays.toString(bccAddress)).append(", subject=").append(subject)
				.append(", message=").append(message).append(", fileNames=").append(Arrays.toString(fileNames))
				.append(", contentType=").append(contentType).append(", fileModel=").append(fileModel)
				.append(", docId=").append(Arrays.toString(docId)).append("]");
		return builder.toString();
	}
	/**
	 * @return
	 * Method used for logging purpose after removal of sensitive information
	 */
	public String stringify() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailInfo [fromAddress=").append(fromAddress).append(", toAddress=")
				.append(Arrays.toString(toAddress)).append(", ccAddress=").append(Arrays.toString(ccAddress))
				.append(", bccAddress=").append(Arrays.toString(bccAddress))				
				.append(", docId=").append(Arrays.toString(docId)).append("]");
		return builder.toString();
	}
	

}
