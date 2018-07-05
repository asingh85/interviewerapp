package com.softvision.helper;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.softvision.constant.InterviewConstant;
import com.softvision.exception.ServiceException;
import com.softvision.model.Employee;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceClientHelper.class);

    @Inject
    RestTemplate restTemplate;

    @Inject
    private EurekaClient eurekaClient;

    @Value("${admin.service}")
    private String adminService;

    @Value("${admin.method}")
    private String serviceMethod;

    private InstanceInfo getInstance(String serviceName){
        try{
            Application application = eurekaClient.getApplication(serviceName);
            return application.getInstances().get(0);
        }catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    public Collection<Employee> getInterviewers(String technology, int experience) {
        List<Employee> searchList = new ArrayList<>();
        if ((technology != null && !technology.isEmpty()) && (experience > 0)) {
            try {
                InstanceInfo instanceInfo= getInstance(adminService);
                String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                        + "/" + serviceMethod + technology + "&be=" + experience;
                System.out.println("URL : " + url);
                Employee[] forNow = restTemplate.getForObject(url, Employee[].class);
                searchList= Arrays.asList(forNow);
                System.out.println("-------------------------searchList----------------------"+searchList);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(InterviewConstant.INVALID_REQUEST);
        }
        return searchList;
    }

}
