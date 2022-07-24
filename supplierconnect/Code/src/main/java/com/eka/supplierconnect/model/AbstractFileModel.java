
package com.eka.supplierconnect.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.model.Tag;
import com.eka.supplierconnect.validator.CommonValidator;

/**
 * @author Ranjan.Jha
 * 
 *         Keep file details except it's content.
 * 
 */

public class AbstractFileModel extends EkaModel {

	private String fileName;
	private String uploadedDate;
	private String uploadedBy;
	private String refObjectId;
	private String refObject;
	private String description;
	private Object otherAttributes;
	private long size;
	private String fileContentType;
	private String fileId;
	private String bucketDir;
	private Object tags;
	private List<Tag> s3Tags;

	 
	 CommonValidator validator=new CommonValidator();

	public String getBucketDir() {
		return bucketDir;
	}
	public void setBucketDir(String bucketDir) {
		this.bucketDir = bucketDir;
	}
	public AbstractFileModel() {
	}
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return validator.cleanData(fileName);
	}

	public void setFileName(String fileName) {
		this.fileName = validator.cleanData(fileName);
	}

	public String getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getRefObjectId() {
		return refObjectId;
	}

	public void setRefObjectId(String refObjectId) {
		this.refObjectId = refObjectId;
	}

	public String getRefObject() {
		return refObject;
	}

	public void setRefObject(String refObject) {
		this.refObject = refObject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getOtherAttributes() {
		return otherAttributes;
	}

	public void setOtherAttributes(Object otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	

	public Object getTags() {
		return tags;
	}
	public void setTags(Object tags) {
		this.tags = tags;
	}
	
	public List<Tag> getS3Tags() {
		return s3Tags;
	}
	public void setS3Tags(List<Tag> s3Tags) {
		this.s3Tags = s3Tags;
	}
	@Override
	public String toString() {
		return "fileName=" + fileName + ", uploadedDate=" + uploadedDate + ", uploadedBy=" + uploadedBy
				+ ", refObjectId=" + refObjectId + ", refObject=" + refObject + ", description=" + description
				+ ", otherAttributes=" + otherAttributes + ", size=" + size+ ", fileContentType=" + fileContentType+ ", tags=" + tags;
	}

}
