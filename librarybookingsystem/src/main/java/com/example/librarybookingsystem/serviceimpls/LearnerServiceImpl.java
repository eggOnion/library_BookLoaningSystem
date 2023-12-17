package com.example.librarybookingsystem.serviceimpls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.librarybookingsystem.entities.Book;
import com.example.librarybookingsystem.entities.Learner;
import com.example.librarybookingsystem.exceptions.LearnerEmailDuplicateException;
import com.example.librarybookingsystem.exceptions.LearnerNotFoundException;
import com.example.librarybookingsystem.repositories.LearnerRepository;
import com.example.librarybookingsystem.services.LearnerService;


@Service
public class LearnerServiceImpl implements LearnerService {

    private LearnerRepository learnerRepository;    

    //@Autowired
    public LearnerServiceImpl(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    @Override
    public Learner createLearner(Learner learner) {
        List<Learner> emailDuplication = learnerRepository.findByEmail(learner.getEmail());

        if (emailDuplication.isEmpty()) {
            Learner newUser = learnerRepository.save(learner);
            return newUser;
        }
        throw new LearnerEmailDuplicateException(learner.getEmail());
    }

    @Override
    public Learner getLearner(int id) {
        return learnerRepository.findById(id).orElseThrow(() -> new LearnerNotFoundException(id));
    }

    @Override
    public ArrayList<Learner> getAllLearners() {
        List<Learner> allLearners = learnerRepository.findAll();
        return (ArrayList<Learner>) allLearners;
    }

    @Override
    public Learner updateLearner(int id, Learner learner) {
  
        Learner learnerToUpdate = learnerRepository.findById(id).get();
        learnerToUpdate.setFirstName(learner.getFirstName());        
        learnerToUpdate.setLastName(learner.getLastName());
        learnerToUpdate.setEmail(learner.getEmail());
        learnerToUpdate.setContact_num(learner.getContact_num());
        return learnerRepository.save(learnerToUpdate);    
    }

    @Override
    public void deleteLearner(int id) {
        Optional<Learner> learnerOptional = learnerRepository.findById(id);
        if (learnerOptional.isPresent())
            learnerRepository.deleteById(id);
        else
            throw new LearnerNotFoundException(id);
    }    

    @Override
    public ArrayList<Learner> searchLearner(String email) {
        List<Learner> findLearner = learnerRepository.findByEmail(email);

        if(findLearner.isEmpty())
            throw new LearnerNotFoundException(email);
        else
            return (ArrayList<Learner>) findLearner;       
    }     
}


