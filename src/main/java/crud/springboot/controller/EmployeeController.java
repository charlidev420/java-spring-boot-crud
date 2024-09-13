	package crud.springboot.controller;

	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.domain.Page;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestParam;

	import crud.springboot.model.Employee;
	import crud.springboot.service.EmployeeService;

	@Controller
	public class EmployeeController {

		@Autowired
		private EmployeeService employeeService;

		// Display the dashboard
		@GetMapping("/dashboard")
		public String dashboard(Model model) {
			return findPaginated(1, "firstName", "asc", model);
		}

		// Display form to add new employee
		@GetMapping("/dashboard/showNewEmployeeForm")
		public String showNewEmployeeForm(Model model) {
			Employee employee = new Employee();
			model.addAttribute("employee", employee);
			return "new_employee";
		}

		// Save a new employee
		@PostMapping("/dashboard/saveEmployee")
		public String saveEmployee(@ModelAttribute("employee") Employee employee) {
			employeeService.saveEmployee(employee);
			return "redirect:/dashboard";
		}

		// Display form to update an employee
		@GetMapping("/dashboard/showFormForUpdate/{id}")
		public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
			Employee employee = employeeService.getEmployeeById(id);
			model.addAttribute("employee", employee);
			return "update_employee";
		}

		// Delete an employee
		@GetMapping("/dashboard/deleteEmployee/{id}")
		public String deleteEmployee(@PathVariable(value = "id") long id) {
			employeeService.deleteEmployeeById(id);
			return "redirect:/dashboard";
		}

		// Pagination and sorting
		@GetMapping("/dashboard/page/{pageNo}")
		public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
									@RequestParam("sortField") String sortField,
									@RequestParam("sortDir") String sortDir,
									Model model) {
			int pageSize = 20;

			Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
			List<Employee> listEmployees = page.getContent();

			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());

			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

			model.addAttribute("listEmployees", listEmployees);
			return "dashboard"; // This should match your template name
		}
	}
