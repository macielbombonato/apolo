package apolo.web.service;

import apolo.data.model.Tenant;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseDashboardService {

    public abstract void generateDashboardData(ModelAndView mav, Tenant tenant);

}
