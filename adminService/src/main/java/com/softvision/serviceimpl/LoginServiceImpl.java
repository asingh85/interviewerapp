package com.softvision.serviceimpl;

import com.softvision.model.Login;
import com.softvision.repository.LoginRepository;
import com.softvision.service.LoginService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;


@Component
public class LoginServiceImpl implements LoginService<Login> {

    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    LoginRepository loginRepository;

    @Override
    public Login register(Login login) {
        mongoTemplate.insert(login);
        return login;
    }

    @Override
    public Login login(String email, String password) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria = criteria.andOperator(Criteria.where("emailid").is(email
                ),
                Criteria.where("password").is(password));
        query = query.addCriteria(criteria);
        System.out.println(query.toString());
        System.out.println(mongoTemplate.getDb().getCollection("login").count());
        System.out.println("All docs : " + mongoTemplate.findAll(Login.class));
        System.out.println("All docs : " + mongoTemplate.findOne(query, Login.class));
        return mongoTemplate.findOne(query, Login.class);
    }

    @Override
    public List<Login> getAll() {
        return loginRepository.findAll();
    }

}
