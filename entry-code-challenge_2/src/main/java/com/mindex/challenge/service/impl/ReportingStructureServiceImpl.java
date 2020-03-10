package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ReportingStructure read(String id) {
		LOG.debug("Reading Reporting-Structure for employee id [{}]", id);

		Employee employee = employeeRepository.findByEmployeeId(id);
		// check if employee does not exists then throw error
		if (employee == null)
			throw new RuntimeException("Invalid employeeId: " + id);

		ReportingStructure reportingStructure = new ReportingStructure();
		reportingStructure.setEmployee(employee);

		if (employee.getDirectReports() != null) {
			reportingStructure.setNumberOfReports(getAllReports(employee));
		} else
			reportingStructure.setNumberOfReports(0);
		return reportingStructure;
	}

	/**
	 * Recursive method to count direct reporting employee
	 * 
	 * @param Employee instance
	 * @return total sum of all employees reporting
	 */
	private int getAllReports(Employee employee) {
		if (employee == null)
			return 0;
		int reportCount = 0;
		//keeps a check on all visted nodes
		HashSet<String> visitedNodes = new HashSet<String>();
		visitedNodes.add(employee.getEmployeeId());
		for (Employee e : employee.getDirectReports()) {
			if (!visitedNodes.contains(e.getEmployeeId())) {
				visitedNodes.add(e.getEmployeeId());
				reportCount += getAllReports(employeeRepository.findByEmployeeId(e.getEmployeeId())) + 1;
			} else {
				throw new RuntimeException(
						//throw exception to avoid infinite loop problem
						"Conflict Found! employees reporting to each other [{}]" + e.getEmployeeId());
			}
		}
		return reportCount;
	}
}
