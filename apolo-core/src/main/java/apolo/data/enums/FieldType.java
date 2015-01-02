package apolo.data.enums;

import apolo.data.model.User;
import apolo.data.model.UserCustomField;
import apolo.data.model.UserCustomFieldOption;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.HapoloMap;
import java.util.Map;

public enum FieldType {
	TEXT("text") {
		@Override
		public boolean hasOptions() {
			return false;
		}

		@Override
		public String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user) {
			StringBuilder result = new StringBuilder();
			
			result.append("<div class=\"form-group\">");
			result.append("<label for=\"field_" + index + "\" class=\"control-label\">");
			result.append(field.getLabel());
			result.append("</label>");
			
			result.append("<input type=\"hidden\" name=\"customFields[" + index + "].userCustomField.id\" class=\"form-control\" value=\"" + field.getId() + "\"  />");
			
			if (user != null && user.getId() != null && user.getId() != null && user.getId() != 0L) {
				result.append("<input type=\"hidden\" name=\"customFields[" + index + "].user.id\" class=\"form-control\" value=\"" + user.getId() + "\"  />");
			} 
			
			String value = "";
			if (fieldMap.get(field.getId()) != null) {
				value = fieldMap.get(field.getId());
			}
			
			result.append("<input type=\"" + "text" + "\" id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" class=\"form-control\" value=\"" + value + "\" ");
			
			if (readOnly) {
				result.append("readonly=\"true\"");
			}
			
			result.append(" />");
			result.append("</div>");
				
			return result.toString();
		}
	},
	TEXT_AREA("text_area") {
		@Override
		public boolean hasOptions() {
			return false;
		}

		@Override
		public String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user) {
			StringBuilder result = new StringBuilder();
			
			result.append("<div class=\"form-group\">");
			result.append("<label for=\"field_" + index + "\" class=\"control-label\">");
			result.append(field.getLabel());
			result.append("</label>");
			
			result.append("<input type=\"hidden\" name=\"customFields[" + index + "].userCustomField.id\" class=\"form-control\" value=\"" + field.getId() + "\"  />");
			
			if (user != null && user.getId() != null && user.getId() != null && user.getId() != 0L) {
				result.append("<input type=\"hidden\" name=\"customFields[" + index + "].user.id\" class=\"form-control\" value=\"" + user.getId() + "\"  />");
			} 
			
			String value = "";
			if (fieldMap.get(field.getId()) != null) {
				value = fieldMap.get(field.getId());
			}
			
			result.append("<textarea id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" row=\"5\" class=\"form-control\" ");
			
			if (readOnly) {
				result.append("readonly=\"true\"");
			}
			
			result.append(">");
			result.append(value);
			result.append("</textarea>");
			result.append("</div>");
				
			return result.toString();
		}
	},
	SELECT("select") {
		@Override
		public boolean hasOptions() {
			return true;
		}

		@Override
		public String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user) {
			StringBuilder result = new StringBuilder();
			
			result.append("<div class=\"form-group\">");
			result.append("<label for=\"field_" + index + "\" class=\"control-label\">");
			result.append(field.getLabel());
			result.append("</label>");
			
			result.append("<input type=\"hidden\" name=\"customFields[" + index + "].userCustomField.id\" class=\"form-control\" value=\"" + field.getId() + "\"  />");
			
			if (user != null && user.getId() != null && user.getId() != 0L) {
				result.append("<input type=\"hidden\" name=\"customFields[" + index + "].user.id\" class=\"form-control\" value=\"" + user.getId() + "\"  />");
			} 
			
			String value = "";
			if (fieldMap.get(field.getId()) != null) {
				value = fieldMap.get(field.getId());
			}
			
			// TODO resolve messageBundle
			// result.append("<select id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" class=\"input-block-level applyChosen form-control\" data-placeholder='" + MessageBundle.getMessageBundle("common.select") + "'");
			result.append("<select id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" class=\"form-control chzn-select\" data-placeholder='" + "Select" + "'");
			if (readOnly) {
				result.append("disabled=\"disabled\" ");
			}
			result.append(">");
			result.append("<option value=\"\" data-show-option=\"false\"></option>");
			
			for (UserCustomFieldOption option : field.getOptions()) {
				result.append("<option value=\"" + option.getValue() + "\"");
				
				if (value.equals(option.getValue())) {
					result.append("selected=\"selected\"");
				}
				result.append(">" + option.getValue() + "</option>");
			}
			
			result.append("</select>");
			
			result.append("</div>");
				
			return result.toString();
		}
	},
	SELECT_MULTIPLE("select_multiple") {
		@Override
		public boolean hasOptions() {
			return true;
		}

		@Override
		public String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user) {
			StringBuilder result = new StringBuilder();
			
			result.append("<div class=\"form-group\">");
			result.append("<label for=\"field_" + index + "\" class=\"control-label\">");
			result.append(field.getLabel());
			result.append("</label>");
			
			result.append("<input type=\"hidden\" name=\"customFields[" + index + "].userCustomField.id\" class=\"form-control\" value=\"" + field.getId() + "\"  />");
			
			if (user != null && user.getId() != null && user.getId() != 0L) {
				result.append("<input type=\"hidden\" name=\"customFields[" + index + "].user.id\" class=\"form-control\" value=\"" + user.getId() + "\"  />");
			} 
			
			Map<String, String> values = new HapoloMap<String, String>();
			if (fieldMap.get(field.getId()) != null) {
				String[] valuesStr = fieldMap.get(field.getId()).split(",");
				for (String value : valuesStr) {
					values.put(value, value);
				}
			}
			
			// TODO resolve messageBundle
			//result.append("<select id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" multiple=\"multiple\" class=\"input-block-level applyChosen form-control\" data-placeholder='" + MessageBundle.getMessageBundle("common.select") + "'");
			result.append("<select id=\"field_" + index + "\" name=\"customFields[" + index + "].value\" multiple=\"multiple\" class=\"input-block-level applyChosen form-control\" data-placeholder='" + "Select" + "'");
			if (readOnly) {
				result.append("disabled=\"disabled\" ");
			}
			result.append(">");
			result.append("<option value=\"\" data-show-option=\"false\"></option>");
			
			for (UserCustomFieldOption option : field.getOptions()) {
				result.append("<option value=\"" + option.getValue() + "\"");
				
				if (values.get(option.getValue()) != null) {
					result.append("selected=\"selected\"");
				}
				result.append(">" + option.getValue() + "</option>");
			}
			
			result.append("</select>");
			
			result.append("</div>");
				
			return result.toString();
		}
	},
	RADIO("radio") {
		@Override
		public boolean hasOptions() {
			return true;
		}

		@Override
		public String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user) {
			StringBuilder result = new StringBuilder();
			
			result.append("<div class=\"form-group\">");
			result.append("<label for=\"field_" + index + "\" class=\"control-label\">");
			result.append(field.getLabel());
			result.append("</label>");
			
			result.append("<input type=\"hidden\" name=\"customFields[" + index + "].userCustomField.id\" class=\"form-control\" value=\"" + field.getId() + "\"  />");
			
			if (user != null && user.getId() != null && user.getId() != 0L) {
				result.append("<input type=\"hidden\" name=\"customFields[" + index + "].user.id\" class=\"form-control\" value=\"" + user.getId() + "\"  />");
			} 
			
			String value = "";
			if (fieldMap.get(field.getId()) != null) {
				value = fieldMap.get(field.getId());
			}
			
			result.append("<div id=\"field_" + index + "\">");
			for (UserCustomFieldOption option : field.getOptions()) {
				result.append("<div class=\"radio\">");
				result.append("<label>");
				
				result.append("<input type=\"" + "radio" + "\" id=\"field_" + index + "_option" + option.getId() + "\" name=\"customFields[" + index + "].value\" value=\"" + option.getValue() + "\" ");
				if (readOnly) {
					result.append("disabled=\"disabled\" ");
				}
				
				if (value.equals(option.getValue())) {
					result.append("checked=\"checked\" ");
				}
				result.append("/>");
				result.append(option.getValue());
				
				result.append("</label>");
				
				result.append("</div>");
			}
			result.append("</div>");
			
			result.append("</div>");
				
			return result.toString();
		}
	},
	
	;
	
	private final String code;
	
	private static final Map<String, FieldType> valueMap;
	
	static {
		Builder<String, FieldType> builder = ImmutableMap.builder();
		for (FieldType tipo : values()) {
			builder.put(tipo.code, tipo);
		}
		valueMap = builder.build();
	}
	
	public static FieldType fromCode(String code) {
		return valueMap.get(code);
	}

	private FieldType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public abstract boolean hasOptions();
	
	public abstract String getHtml(boolean readOnly, Map<Long, String> fieldMap, int index, UserCustomField field, User user);
	
	public boolean getShowOptions() {
		return hasOptions();
	}
}
