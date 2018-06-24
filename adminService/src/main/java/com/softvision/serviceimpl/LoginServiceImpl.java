package com.softvision.serviceimpl;

import com.softvision.model.Login;
import com.softvision.service.LoginService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.inject.Inject;

@Component
public class LoginServiceImpl implements LoginService<Login> {

    @Inject
    MongoTemplate mongoTemplate;

    @Override
    public Login register(Login login) {
        mongoTemplate.insert(login);
        return login;
    }

    @Override
    public Login login(String userName, String password) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria = criteria.andOperator(Criteria.where("userName").is(userName),
                Criteria.where("password").is(password));
        query = query.addCriteria(criteria);
        System.out.println(query.toString());
        System.out.println(mongoTemplate.getDb().getCollection("login").count());
        System.out.println("All docs : " + mongoTemplate.findAll(Login.class));
        System.out.println("All docs : " + mongoTemplate.findOne(query,Login.class));
        return mongoTemplate.findOne(query,Login.class);
    }

}
