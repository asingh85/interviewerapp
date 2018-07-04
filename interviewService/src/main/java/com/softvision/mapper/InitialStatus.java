package com.softvision.mapper;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.softvision.constant.InterviewConstant;
import com.softvision.exception.ServiceException;
import com.softvision.model.Employee;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InitialStatus {

    @Inject
    InterviewService interviewService;

    @Inject
    RestTemplate restTemplate;

    @Inject
    private EurekaClient eurekaClient;

    @Value("${admin.service}")
    private String serviceName;

    @Value("${admin.method}")
    private String serviceMethod;


    public Optional<Interview> publishInterview(String candidateId, int experience, String technology) throws ServiceException {

        List<Employee> interviewerList = rejesterToAdminService(technology, experience);
        if (interviewerList != null && !interviewerList.isEmpty()) {
            getInterviewIds(interviewerList);
        }

        //TODO  fetch intervier list from admin service passing candidate and exp

        List<String> interviewIdList = new ArrayList();
        interviewIdList.add("6224");
        interviewIdList.add("6269");
        interviewIdList.add("2006");

        LocalDateTime joiningDate = LocalDateTime.now();
        Interview interview = new Interview();
        interview.setInterviewStatus(InterviewStatus.INITIATED);
        interview.setInterviewerId(null);
        interview.setInterviewerList(interviewIdList);
        interview.setModifiedDate(joiningDate);
        interview.setCreationTime(joiningDate);
        interview.setCandidateId(candidateId);
        interview.setTechnology(technology);

        return interviewService.addInterview(interview);
    }

    private List rejesterToAdminService(String technology, int experience) {

        List<Employee> list = null;
        if ((technology != null && !technology.isEmpty()) && (experience > 0)) {
            try {
                Application application = eurekaClient.getApplication(serviceName);
                InstanceInfo instanceInfo = application.getInstances().get(0);
                //  String url = "http://spirl181.spi.com:8089/admin/interviewer/bybandexp?tc=JAVA&be=2";

                String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                        + "/" + serviceMethod + technology + "&be=" + experience;
                System.out.println("URL : " + url);
                list = restTemplate.getForObject(url, List.class);
                System.out.println("RESPONSE " + list);


            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(InterviewConstant.INVALID_REQUEST);
        }
        return list;
    }

    private void getInterviewIds(List<Employee> interviewerList) {
        if (interviewerList != null && !interviewerList.isEmpty()) {
            List<String> interviewIds = new ArrayList<>();
            List<String> interviewEmail = new ArrayList<>();

            for (int i = 0; i < interviewerList.size(); i++) {
                Employee interviewer = interviewerList.get(i);
                interviewIds.add(interviewer.getEmployeeId());
                interviewEmail.add(interviewer.getEmailId());
            }

//            interviewerList.stream().forEach(i -> {
//                    interviewIds.add(i.getInterviewerID());
//                    interviewEmail.add(i.getEmailId());
//            });

            // TODO Integrate with email
            System.out.println(interviewIds);

            System.out.println(interviewEmail);
        }
    }
}
