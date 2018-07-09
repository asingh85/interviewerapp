package com.softvision;

import com.softvision.repository.CandidateRepositoryTest;
import com.softvision.repository.EmployeeRepositoryTest;
import com.softvision.serviceimpl.CandidateServiceImplTest;
import com.softvision.serviceimpl.EmployeeServiceImplTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        CandidateRepositoryTest.class,
        EmployeeRepositoryTest.class,
        CandidateServiceImplTest.class,
        EmployeeServiceImplTest.class

})
public class InterviewappApplicationTests {

    @Test
    public void contextLoads() {

    }


}