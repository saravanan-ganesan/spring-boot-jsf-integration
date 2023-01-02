package com.fedex.rise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fedex.rise.entity.UserEntity;
import com.fedex.rise.repository.UserRepository;
import com.fedex.rise.vo.EmployeeVO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	public EmployeeVO getUser(String empNo) {
		
		EmployeeVO employeeVO = null;
		UserEntity userEntity = userRepository.findByEmpNbr(empNo);
		if(!ObjectUtils.isEmpty(userEntity)) {
			employeeVO = new EmployeeVO();
			employeeVO.set_emp_first_nm(userEntity.getEmpFirstNm());
			employeeVO.set_emp_last_nm(userEntity.getEmpLastNm());
			employeeVO.set_emp_nbr(userEntity.getEmpNbr());
			employeeVO.set_emp_role_cd(userEntity.getEmpRoleCd());
		}
		return employeeVO;
	}
}
