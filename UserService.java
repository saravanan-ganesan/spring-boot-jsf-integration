package com.fedex.rise.service;

import org.springframework.stereotype.Service;

import com.fedex.rise.vo.EmployeeVO;

@Service
public interface UserService {

	public EmployeeVO getUser(String empNo);
}
