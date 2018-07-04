package com.softvision.serviceimpl;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Candidate;
import com.softvision.repository.CandidateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author durga.s
 */
@RunWith(SpringRunner.class)
public class CandidateServiceImplTest {

    @InjectMocks
    private CandidateServiceImpl candidateServiceImpl;

    @Mock
    CandidateRepository candidateRepository;

    @Mock
    MongoTemplate mongoTemplate;

	/*@InjectMocks
	Query query;

	@Mock
	Criteria criteria;*/

    private Candidate candidate;
    // private Candidate secondCandidate;
    private List<Candidate> candidates;

    private Optional<Candidate> optionalCandidate;

    @Before
    public void setUp() {
        candidate = new Candidate();
        candidate.setCandidateId("1");
        candidate.setFirstName("Test");
        candidate.setLastName("User");
        candidate.setEmail("test@test.com");
        candidate.setExperience("7 years");
        candidate.setGender("Male");
        candidate.setIsActive(true);
        candidate.setPhoneNumber("9986644333");
        candidate.setTechnologyStack("JAVA");
        candidate.setUniqueIdentityNumber("1101");
        candidate.setCreatedBy("user2");
        candidate.setModifiedBy("user2");
        candidate.setInterviewDate(LocalDateTime.now());
        candidate.setInterviewTime(LocalDateTime.now());
        // candidate.setCreatedDate(LocalDateTime.now());
        // candidate.setModifiedDate(LocalDateTime.now());
        candidate.setUniqueIdentityNumber("BTDPK8877J");

        Candidate secondCandidate = new Candidate();
        secondCandidate.setCandidateId("2");
        secondCandidate.setFirstName("Test user2");
        secondCandidate.setLastName("User test 2");
        secondCandidate.setEmail("testuser2@test2.com");
        secondCandidate.setExperience("3 years");
        secondCandidate.setGender("TestGender");
        secondCandidate.setIsActive(false);
        secondCandidate.setPhoneNumber("7766446646");
        secondCandidate.setTechnologyStack("Testing");
        secondCandidate.setUniqueIdentityNumber("3333");
        secondCandidate.setCreatedBy("testuser");
        secondCandidate.setModifiedBy("testuser");
        // secondCandidate.setCreatedDate(LocalDateTime.now());
        // secondCandidate.setModifiedDate(LocalDateTime.now());
        secondCandidate.setInterviewDate(LocalDateTime.now());
        secondCandidate.setInterviewTime(LocalDateTime.now());
        secondCandidate.setUniqueIdentityNumber("ABCDIE88JK");

        candidates = new ArrayList<>();
        candidates.add(candidate);
        candidates.add(secondCandidate);

        optionalCandidate = Optional.of(candidate);
    }

    /**
     *
     */
    @Test
    public void testAddCandidate() {
        Mockito.when(candidateRepository.insert(candidate)).thenReturn(candidate);
        Candidate candidate1 = candidateServiceImpl.addCandidate(candidate);
        assertNotNull(candidate1.getCreatedDate());
        assertTrue(candidate1.getFirstName().equals(candidate.getFirstName()));
        assertNotNull(candidate1.getPhoneNumber());
    }

    @Test
    public void testFindCandidateByID() {
        // Optional<Candidate> candidate = Optional.of(Candidate);
        Mockito.when(candidateRepository.findById("1")).thenReturn(optionalCandidate);
        Candidate candidate1 = candidateServiceImpl.findCandidateById("1");
        Candidate candidate2 = optionalCandidate.get();
        assertNotNull(candidate1);
        assertTrue(candidate1.getCandidateId().equals(candidate2.getCandidateId()));
    }

    @Test
    public void testDeleteCandidateById() {
        String returnValue = null;
        Mockito.when(candidateRepository.findById("1")).thenReturn(optionalCandidate);
        Candidate candidate1 = optionalCandidate.get();
        Mockito.when(candidateRepository.save(candidate1)).thenReturn(candidate1);
        returnValue = candidateServiceImpl.deleteCandidateById("1");
        assertTrue(returnValue.equals(ServiceConstants.CANDIDATE_DELETED));
        assertEquals(candidate1.getIsActive(), false);
        returnValue = candidateServiceImpl.deleteCandidateById("200");
        assertEquals(ServiceConstants.CANDIDATE_NOT_FOUND, returnValue);
    }

    @Test
    public void testSaveAllCandidates() {
        Mockito.when(candidateRepository.saveAll(candidates)).thenReturn(candidates);
        List<Candidate> candidateList = candidateServiceImpl.saveAllCandidates(candidates);
        assertNotNull(candidateList);
        assertEquals(candidateList.size(), 2);
    }

    @Test
    public void testFindAllCandidates() {
        Mockito.when(candidateRepository.findAll()).thenReturn(candidates);
        List<Candidate> candidateList = candidateServiceImpl.findAllCandidates();
        assertEquals(candidateList.size(), 2);
    }

    @Test
    public void testFindByIsActiveIsTrue() {
        List<Candidate> candidateIsActiveList = new ArrayList<>();
        candidateIsActiveList.add(candidate);
        Mockito.when(candidateRepository.findByIsActiveIsTrue()).thenReturn(candidateIsActiveList);
        List<Candidate> candidateList = candidateServiceImpl.findByIsActiveIsTrue();
        assertEquals(candidateList.get(0).getIsActive(), true);
        assertEquals(candidateList.size(), 1);
        assertFalse(!candidateList.get(0).getIsActive());
    }

    @Test
    public void testUpdateCandidate() {
        Mockito.when(candidateRepository.findById("1")).thenReturn(optionalCandidate);
        Candidate candidate1 = optionalCandidate.get();
        candidate1.setFirstName("Updated User");
        candidate1.setPhoneNumber("9999000000");
        candidate1.setCandidateId("1");
        candidate1.setModifiedDate(LocalDateTime.now());
        Mockito.when(candidateRepository.save(candidate1)).thenReturn(candidate1);
        Candidate updatedCandidate = candidateServiceImpl.updateCandidate(candidate, "1");
        assertEquals(updatedCandidate.getFirstName(), candidate1.getFirstName());
        assertEquals(updatedCandidate.getPhoneNumber(), candidate1.getPhoneNumber());

    }

    @Test
    public void testSearchCandidate() {
        String searchAttribute = "7766";
        Criteria criteria = new Criteria();
        criteria = criteria.orOperator(Criteria.where("firstName").regex(searchAttribute, "si"),
                Criteria.where("lastName").regex(searchAttribute, "si"),
                Criteria.where("technologyStack").regex(searchAttribute, "si"),
                Criteria.where("phoneNumber").regex(searchAttribute, "si"),
                Criteria.where("email").regex(searchAttribute, "si"));
        criteria = criteria.andOperator(Criteria.where("isActive").is(true));

        Query query = new Query(criteria);
        Mockito.when(mongoTemplate.find(query, Candidate.class)).thenReturn(candidates);
        List<Candidate> candidateList = candidateServiceImpl.searchCandidate(searchAttribute);
        assertEquals(candidateList.size(), 0);
    }
}
