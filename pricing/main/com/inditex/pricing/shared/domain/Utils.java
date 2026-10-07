
package com.inditex.pricing.shared.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.CaseFormat;

import java.io.Serializable;
import java.util.Map;

public final class Utils {

  public static String toSnake(String text) {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text);
  }

  public static String toParsedJson(Map<String, Serializable> map){
	  try {
		  return new ObjectMapper()
			  .enable(SerializationFeature.INDENT_OUTPUT)
			  .writeValueAsString(map);
	  }catch (JsonProcessingException e){
		  throw new RuntimeException("Error parsing map to json", e);
	  }
  }
}
