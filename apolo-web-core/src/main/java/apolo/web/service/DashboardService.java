package apolo.web.service;

import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@Service("dashboardService")
public class DashboardService {

    @Inject
    UserService userService;

    @Inject
    private TenantService tenantService;

    public void generateDashboardData(ModelAndView mav, Tenant tenant) {

        User authUser = userService.getAuthenticatedUser();

        // TODO put your code here
    }
}
