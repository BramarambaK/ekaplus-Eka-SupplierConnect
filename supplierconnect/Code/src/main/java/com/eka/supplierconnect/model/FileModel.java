
package com.eka.supplierconnect.model;

import java.io.File;

import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Ranjan.Jha
 * 
 *         Named from File to FileModel in order to avoid confusion with
 *         java.io.File
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileModel extends AbstractFileModel {

	@Transient
	private MultipartFile multiPartFile;
	@Transient
	private File file;
	@Transient
	private byte[] fileByteStream;
	private String internalRefNo;

	public FileModel() {
	}

	public MultipartFile getMultiPartFile() {
		return multiPartFile;
	}

	public void setMultiPartFile(MultipartFile multiPartFile) {
		this.multiPartFile = multiPartFile;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public byte[] getFileByteStream() {
		return fileByteStream;
	}

	public void setFileByteStream(byte[] fileByteStream) {
		this.fileByteStream = fileByteStream;
	}
	

	public String getInternalRefNo() {
		return internalRefNo;
	}

	public void setInternalRefNo(String internalRefNo) {
		this.internalRefNo = internalRefNo;
	}

	@Override
	public String toString() {
		return "fileName=" + getFileName() + ", uploadedDate=" + getUploadedDate() + ", uploadedBy=" + getUploadedBy()
				+ ", refObjectId=" + getRefObjectId() + ", refObject=" + getRefObject() + ", description="
				+ getDescription() + ", otherAttributes=" + getOtherAttributes() + ", size=" + getSize();
	}

}