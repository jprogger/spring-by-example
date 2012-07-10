package ua.com.springbyexample.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ua.com.springbyexample.domain.Employee;
import ua.com.springbyexample.service.PersistenceService;

@Controller
@RequestMapping("/employee/")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Resource
	private PersistenceService<Employee, Long> employeeService;

	/*
	 * Commented for JMS example Works ONLY with spring-by-example-3 where
	 * message broker is started in embedded mode
	 */
	// @Resource
	// private ActivityLogger logger;

	@RequestMapping(method = RequestMethod.GET, value = "list")
	public ModelAndView listEmployees() {
		logger.debug("Received request to list persons");
		ModelAndView mav = new ModelAndView();
		List<Employee> employeeList = employeeService.find();

		logger.debug("Person Listing count = " + employeeList.size());

		mav.addObject("employeeList", employeeList);

		mav.setViewName("list");

		return mav;

	}

	@RequestMapping(method = RequestMethod.GET, value = "edit")
	public ModelAndView editEmployeeParam(@RequestParam(value = "id", required = false) Long id) {
		logger.debug("Received request to edit person id : " + id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("edit");
		Employee employee = null;
		if (id == null) {
			employee = new Employee();
		} else {
			employee = employeeService.find(id);
		}

		mav.addObject("employee", employee);
		return mav;

	}

	@RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
	public ModelAndView editEmployeePath(@PathVariable Long id) {
		logger.debug("Received request to edit person id : " + id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("edit");
		Employee employee = null;
		if (id == null) {
			employee = new Employee();
		} else {
			employee = employeeService.find(id);
			if (employee == null) {
				mav.setView(new RedirectView("/employee/edit"));
				return mav;
			}
		}

		mav.addObject("employee", employee);
		return mav;

	}

	@RequestMapping(method = RequestMethod.POST, value = "edit")
	public String savePerson(@Valid @ModelAttribute Employee employee, BindingResult bindingResult) {
		logger.debug("Received postback on person " + employee);
		if (bindingResult.hasErrors()) {
			return "edit";
		} else {
			if (employee.getId() != null) {
				employeeService.update(employee);
			} else {
				employeeService.save(employee);
			}
			return "redirect:list";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "delete")
	public String deleteEmployeeParam(@RequestParam(value = "id", required = false) Long id) {
		logger.debug("Received request to delete person id : " + id);
		Employee employee = employeeService.find(id);
		if (employee != null) {
			employeeService.delete(employee);
		}
		return "redirect:list";

	}

	@RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
	public String deleteEmployeePath(@PathVariable Long id) {
		logger.debug("Received request to delete person id : " + id);
		Employee employee = employeeService.find(id);
		if (employee != null) {
			employeeService.delete(employee);
		}
		return "redirect:list";

	}

	@RequestMapping(method = RequestMethod.GET, value = "rest/view/{id}.json", produces = "application/json")
	@ResponseBody
	public Employee viewJson(@PathVariable Long id) {
		logger.debug("Received request to view JSON");
		Employee employee = employeeService.find(id);
		return employee;
	}

	@RequestMapping(method = RequestMethod.GET, value = "rest/view/{id}.xml", produces = "application/xml")
	@ResponseBody
	public Employee viewXml(@PathVariable Long id) {
		logger.debug("Received request to view XML");
		Employee employee = employeeService.find(id);
		return employee;
	}

}
