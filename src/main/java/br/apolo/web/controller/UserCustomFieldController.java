package br.apolo.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.service.UserCustomFieldService;
import br.apolo.common.exception.AccessDeniedException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.FieldType;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.UserCustomField;
import br.apolo.security.SecuredEnum;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user-custom-field")
public class UserCustomFieldController extends BaseController<UserCustomField> {

	@Autowired
	private UserCustomFieldService userCustomFieldService;
	
	@SecuredEnum({ UserPermission.USER_CUSTOM_FIELD_CREATE, UserPermission.USER_CUSTOM_FIELD_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@Valid @ModelAttribute("entity") UserCustomField entity, BindingResult result, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		/*
		 * Object validation
		 */
		if (result.hasErrors()) {
			mav.setViewName(getRedirectionPath(request, Navigation.USER_CUSTOM_FIELD_NEW, Navigation.USER_CUSTOM_FIELD_EDIT));
			
			mav.addObject("userCustomField", entity);
			mav.addObject("typeList", FieldType.values());
			mav.addObject("readOnly", false);
			mav.addObject("error", true);
			
			StringBuilder message = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
				
				message.append(MessageBundle.getMessageBundle("common.field") + " " + MessageBundle.getMessageBundle("user.custom.field." + argument.getDefaultMessage()) + ": " + error.getDefaultMessage() + "\n <br />");
			}
			
			mav.addObject("message", message.toString());
			
			return mav;
		} 
		
		if (entity != null) {
			try {
				userCustomFieldService.save(entity);
				
				mav = view(entity.getId(), request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
			} catch (AccessDeniedException e) {
				mav = list(request);
				mav.addObject("error", true);
				mav.addObject("message", e.getCustomMsg());
			}
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) {
		return list(1, request);
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "list/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField.list"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		Page<UserCustomField> page = userCustomFieldService.list(pageNumber);
		
	    configurePageable(mav, page, "/user-custom-field/list");
	    
	    mav.addObject("searchParameter", "");
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userCustomFieldList", page.getContent());	
		}
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField.new"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_NEW.getPath());
		
		UserCustomField userCustomField = new UserCustomField();
		
		userCustomField.setCreatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setCreationDate(new Date());
		
		userCustomField.setLastUpdatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setLastUpdateDate(new Date());
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField.edit"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_EDIT.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(id);
		
		userCustomField.setLastUpdatedBy(userCustomFieldService.getAuthenticatedUser());
		userCustomField.setLastUpdateDate(new Date());
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@Override
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public @ResponseBody String remove(@PathVariable Long id) {
		String result = "";
		
		JSONObject jsonSubject = new JSONObject();
		JSONObject jsonItem = new JSONObject();
		
		UserCustomField userCustomField = userCustomFieldService.find(id);
		
		if (userCustomField != null) {
			try {
				userCustomFieldService.remove(userCustomField);
				
				result = MessageBundle.getMessageBundle("common.msg.remove.success");
				jsonItem.put("success", true);
			} catch (Throwable e) {
				result = MessageBundle.getMessageBundle("common.remove.msg.error");
				jsonItem.put("success", false);
			}
		}
		
		jsonItem.put("message", result);
		jsonSubject.accumulate("result", jsonItem);
		
		return jsonSubject.toString();
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_REMOVE)
	@RequestMapping(value = "remove-registry/{id}", method = RequestMethod.GET)
	public ModelAndView removeRegistry(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(id);
		
		if (userCustomField != null) {
			try {
				userCustomFieldService.remove(userCustomField);

				mav = list(request);
				mav.addObject("msg", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.msg.remove.success"));
			} catch (Throwable e) {
				mav = list(request);
				mav.addObject("error", true);
				mav.addObject("message", MessageBundle.getMessageBundle("common.remove.msg.error"));
			}
		}
		
		return mav;
	}
	
	@SecuredEnum({UserPermission.USER_CUSTOM_FIELD_VIEW, UserPermission.USER_CUSTOM_FIELD_LIST})
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_VIEW.getPath());
		
		UserCustomField userCustomField = userCustomFieldService.find(id);
		
		mav.addObject("userCustomField", userCustomField);
		mav.addObject("typeList", FieldType.values());
		mav.addObject("readOnly", true);
		
		return mav;
	}

	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("searchParameter") String searchParameter, HttpServletRequest request) {
		return search(1, searchParameter, request);
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search/{searchParameter}/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView search(@PathVariable Integer pageNumber, @PathVariable String searchParameter, HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField.list"), 2, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_LIST.getPath());
		
		Page<UserCustomField> page = userCustomFieldService.search(pageNumber, searchParameter);
		
		configurePageable(mav, page, "/user-custom-field/search/"+searchParameter);
		
		mav.addObject("searchParameter", searchParameter);
		
		if (page != null && page.getContent() != null) {
			mav.addObject("userCustomFieldList", page.getContent());	
		}
		
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_CUSTOM_FIELD_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm(HttpServletRequest request) {
		breadCrumbService.addNode(MessageBundle.getMessageBundle("breadcrumb.userCustomField.search"), 1, request);
		
		ModelAndView mav = new ModelAndView(Navigation.USER_CUSTOM_FIELD_SEARCH.getPath());
		
		return mav;
	}
}
