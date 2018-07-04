package com.softvision;

import com.softvision.model.Candidate;
import com.softvision.repository.CandidateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@EnableMongoRepositories
@TestPropertySource("classpath:application-dev.properties")
@SpringBootTest
public class InterviewappApplicationTests {

//    @Autowired
//    CandidateRepository repository;
//    private Candidate candidate;
//    private Candidate candidate1;
//    private Candidate insertedCandidate;
//    private Candidate inactiveCandidate;

    /**
     * setUp
     * InterviewappApplicationTests
     * void
     */
	/*@Before
	public void setUp() {
		candidate = new Candidate();
		candidate.setCandidateId("1");
		candidate.setFirstName("Test");
		candidate.setLastName("User");
		candidate.setEmail("test@test.com");
		candidate.setExperiance("7 years");
		candidate.setGender("Male");
		candidate.setIsActive(true);
		candidate.setPhoneNumber("9986644333");
		candidate.setTechnologyStack("JAVA");
		candidate.setUniqueIdentityNumber("1101");
		candidate.setCreatedBy("user2");
		candidate.setModifiedBy("user2");
		candidate.setInterviewDate(LocalDateTime.now());
		candidate.setInterviewTime(LocalDateTime.now());
		candidate.setUniqueIdentityNumber("BTDPK8877J");

		insertedCandidate = repository.save(candidate);

		candidate1 = new Candidate();
		candidate1.setCandidateId("2");
		candidate1.setFirstName("inactive-Test");
		candidate1.setLastName("inactive-User");
		candidate1.setEmail("inactive-test@test.com");
		candidate1.setExperiance("1 years");
		candidate1.setGender("Male");
		candidate1.setIsActive(false);
		candidate1.setPhoneNumber("7788776654");
		candidate1.setTechnologyStack("TESTING");
		candidate1.setUniqueIdentityNumber("1122333");
		candidate1.setCreatedBy("inactive-user2");
		candidate1.setModifiedBy("inactive-user2");
		candidate1.setInterviewDate(LocalDateTime.now());
		candidate1.setInterviewTime(LocalDateTime.now());
		candidate1.setUniqueIdentityNumber("jsdfkdsfJ");

		inactiveCandidate = repository.save(candidate1);
	}*/
    @Test
    public void contextLoads() {

    }

	/*@Test
	public void testRepositoryInsert() {
		assertNotNull(insertedCandidate);
		assertThat(insertedCandidate.getFirstName(), is(equalTo(candidate.getFirstName())));
		assertEquals(insertedCandidate.getUniqueIdentityNumber(), candidate.getUniqueIdentityNumber());
	}


	@Test
	public void testRepositoryFindById() {
		Candidate dbCandidate = repository.findById("1").get();
		assertThat(dbCandidate.getFirstName(), is(equalTo(candidate.getFirstName())));
	}

	@Test
	public void testRepositoryFindIsActiveIsTrue() {
		Candidate activeCandidate = repository.findByIsActiveIsTrue().get(0);
		assertThat(activeCandidate.getIsActive(), is(equalTo(insertedCandidate.getIsActive())));
	}

	@Test
	public void testRepositoryFindAll() {
		List<Candidate> candidates = repository.findAll();
		assertTrue(candidates.size()==2);
	}

	@Test
	public void testRepositoryExists() {
		Boolean value = repository.existsById("1");
		assertNotNull(value);
		assertTrue(value);

		Boolean value1 = repository.existsById("100");
		assertFalse(value1);
	}

	@Test
	public void testRepositoryDelete() {
		repository.deleteById("1");
		Optional<Candidate> deletedCandidate = repository.findById("1");
		assertFalse(deletedCandidate.isPresent());
	}

*/

}
