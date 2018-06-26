package com.softvision.service;

import com.softvision.model.Recruiter;
import java.util.List;
import java.util.Optional;

public interface RecruiterService<T extends Recruiter> {

    Optional<List<Recruiter>> getAll();

    Optional<T> getRecruiter(String id);

    Optional<T> addRecruiter(T recruiter);

    Optional<T> updateRecruiter(T recruiter, String id);

    void deleteRecruiter(String id);

    void deleteAllRecruiter();


}
