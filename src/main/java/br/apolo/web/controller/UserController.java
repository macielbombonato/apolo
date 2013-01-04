package br.apolo.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserGroupService;
import br.apolo.business.service.UserService;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.User;
import br.apolo.security.SecuredEnum;
import br.apolo.security.UserPermission;
import br.apolo.web.enums.Navigation;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController<User> {

	@Autowired
	UserService userService;
	
	@Autowired
	UserGroupService userGroupService;
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView(Navigation.USER_INDEX.getPath());
		
		mav.addObject("user", userService.getAuthenticatedUser());
		mav.addObject("readOnly", true);
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "change-password", method = RequestMethod.GET)
	public ModelAndView changePassword() {
		ModelAndView mav = new ModelAndView(Navigation.USER_CHANGE_PASSWORD.getPath());
		
		mav.addObject("user", userService.getAuthenticatedUser());
		mav.addObject("readOnly", true);
		mav.addObject("changePassword", true);
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER)
	@RequestMapping(value = "change-password-save", method = RequestMethod.POST)
	public String changePasswordSave(@ModelAttribute("user") User user) throws IOException {
		
		if (user != null) {
			User dbuser = userService.find(user.getId());
			
			dbuser.setPassword(user.getPassword());
			
			userService.save(dbuser, true);
		}
		
		return redirect(Navigation.HOME);
	}

	@SecuredEnum(UserPermission.USER_CREATE)
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView(Navigation.USER_NEW.getPath());
		
		mav.addObject("user", new User());
		mav.addObject("groupList", userGroupService.list());
		mav.addObject("readOnly", false);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_EDIT.getPath());
		
		User user = userService.find(id);
		
		mav.addObject("user", user);
		mav.addObject("groupList", userGroupService.list());
		mav.addObject("readOnly", false);
		mav.addObject("editing", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_VIEW.getPath());
		
		User user = userService.find(id);
		
		mav.addObject("user", user);
		mav.addObject("readOnly", true);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_REMOVE)
	@RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
	public ModelAndView remove(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		User user = userService.find(id);
		
		if (!user.equals(userService.getAuthenticatedUser())) {
			userService.remove(user);
			
			mav = list();
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.remove.success"));
		} else {
			mav = list();
			
			mav.addObject("error", true);
			mav.addObject("message", MessageBundle.getMessageBundle("user.msg.error.remove.yourself"));
		}
		
		return mav;
	}
	
	
	/**
	 * Use public ModelAndView save(@ModelAttribute("user") User user, @RequestParam(defaultValue = "false") boolean changePassword)
	 */
	@Override
	public ModelAndView save(User entity) {
		return null;
	}
	
	@SecuredEnum({ UserPermission.USER_CREATE, UserPermission.USER_EDIT })
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("user") User user, @RequestParam(defaultValue = "false") boolean changePassword) {
		ModelAndView mav = new ModelAndView();
		
		if (user != null) {
			userService.save(user, changePassword);
			
			mav = list();
			
			mav.addObject("msg", true);
			mav.addObject("message", MessageBundle.getMessageBundle("common.msg.save.success"));
		}
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		List<User> userList = userService.list();
		
		mav.addObject("userList", userList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@ModelAttribute("param") String param) {
		ModelAndView mav = new ModelAndView(Navigation.USER_LIST.getPath());
		
		SearchResult<User> result = userService.search(param);
		
		List<User> userList = result.getResults();
		
		mav.addObject("userList", userList);
		
		return mav;
	}
	
	@SecuredEnum(UserPermission.USER_LIST)
	@RequestMapping(value = "search-form", method = RequestMethod.GET)
	public ModelAndView searchForm() {
		ModelAndView mav = new ModelAndView(Navigation.USER_SEARCH.getPath());
		
		return mav;
	}
	
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "groups", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = null;

                if(element instanceof String && !((String)element).equals("")){
                    //From the JSP 'element' will be a String
                    try{
                        id = Long.parseLong((String) element);
                    } catch (NumberFormatException e) {
                        log.error("Element was " + ((String) element), e);
                    }
                } else if(element instanceof Long) {
                    //From the database 'element' will be a Long
                    id = (Long) element;
                }

                return id != null ? userGroupService.find(id) : null;
            }
          });
    }

}
