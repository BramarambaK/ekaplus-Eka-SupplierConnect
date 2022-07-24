package com.eka.supplierconnect.validator;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Component;

@Component
public class CommonValidator {
	public String cleanData(String str){
		//String string=null;
		//String string=null;
				boolean check=false;
				//Use Untrusted Data validation routine as below
				 str = Normalizer.normalize(null!= str?str:"", Form.NFKC);				 
				// Validate
				Pattern pattern = Pattern.compile("[<>]");
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
				  // Found blacklisted tag
				  throw new IllegalStateException("Untrusted Data detected");
				}else {
					if (null!=str) {
						str=ESAPI.encoder().canonicalize(str);
						check=csvInjectionCheck(str);
						/*
						 * Commented the below as per SC-4210
						 * 
						 */
						//needed tags have been whitelisted						
						//Whitelist list=Whitelist.relaxed().addTags("br","u","table","tr","td","th","b","font").addAttributes("font", "color");
						//if(!Jsoup.isValid(str, list) || check)
						if( check){
							 throw new IllegalStateException("Untrusted Data detected");
						}
					}
				}
				
				return ESAPI.encoder().canonicalize(str);
	}
	
	public boolean csvInjectionCheck(String str){
		if (null!=str) {
			str=ESAPI.encoder().canonicalize(str);
			 Pattern scriptPattern = Pattern.compile("[\\\"][\\s]?[=@][\\s]?[A-za-z0-9]+|[\\\"][A-Za-z]+.=cmd |.=cmd");
			 if (scriptPattern.matcher(str).find()) {
				 return true;
				}
		}
		
		return false;
	}
	/**
	 * Usage: security check to validate if input contains valid and allowed html tags
	 * from the safelist 
	 * @param s the input string
	 * @return cleaned input
	 */
	public String htmlCheck(String s) {
		Safelist list= Safelist.relaxed().addTags("br","font","u","table","td","tr","th","b");
		s=Jsoup.clean(s, list);
		s=ESAPI.encoder().canonicalize(s);
		return s;
		
	}

}
