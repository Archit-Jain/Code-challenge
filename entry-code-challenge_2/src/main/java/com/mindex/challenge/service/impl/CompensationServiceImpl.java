package com.mindex.challenge.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
//import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
//import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
	@Autowired
	private CompensationRepository compensationRepository;
	/*
	 * @Autowired private EmployeeRepository employeeRepository;
	 */

	@Override
	public Compensation read(String id) {
		// TODO Auto-generated method stub
		LOG.debug("Read compensation with id: [{}]", id);

		Compensation compensation = compensationRepository.findByEmployee(id);
		if (compensation == null) {
			throw new RuntimeException("Invalid CompensationId: " + id);
		}

		return compensation;
	}

	@Override
	public Compensation create(Compensation compensation) {
		// TODO Auto-generated method stub
		LOG.debug("Creating New Compensation [{}]", compensation);
		//adding Compensation Id to avoid redundancy
		compensation.setCompensationid(UUID.randomUUID().toString());
		compensationRepository.insert(compensation);

		return compensation;
	}
}
