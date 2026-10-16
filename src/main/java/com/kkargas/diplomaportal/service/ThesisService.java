package com.kkargas.diplomaportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kkargas.diplomaportal.model.ProfessorProfile;
import com.kkargas.diplomaportal.model.Thesis;
import com.kkargas.diplomaportal.repository.ThesisApplicationRepository;
import com.kkargas.diplomaportal.repository.ThesisAssignmentRepository;
import com.kkargas.diplomaportal.repository.ThesisRepository;

import jakarta.transaction.Transactional;

@Service
public class ThesisService {

    private final ThesisRepository thesisRepository;
    private final ThesisAssignmentRepository thesisAssignmentRepository;
    private final ThesisApplicationRepository thesisApplicationRepository;
    
    
    public ThesisService(ThesisRepository thesisRepository,
    						ThesisAssignmentRepository thesisAssignmentRepository,
    						ThesisApplicationRepository thesisApplicationRepository) {
        
    		this.thesisRepository = thesisRepository;
        this.thesisAssignmentRepository = thesisAssignmentRepository;
        this.thesisApplicationRepository = thesisApplicationRepository;
    }

    public List<Thesis> findAll() {
        return thesisRepository.findAll();
    }

    public List<Thesis> findBySupervisor(
            ProfessorProfile supervisor) {

        return thesisRepository.findBySupervisor(supervisor);
    }

    public Thesis findById(Long thesisId) {
        return thesisRepository.findById(thesisId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Thesis not found."
                        ));
    }

    public Thesis save(Thesis thesis) {
        return thesisRepository.save(thesis);
    }

    public void deleteById(Long thesisId) {
        thesisRepository.deleteById(thesisId);
    }
    
    @Transactional
    public Thesis updateThesis(
            Long thesisId,
            Thesis formThesis) {

        Thesis thesis =
                findById(thesisId);


        thesis.setThesisTitle(
                formThesis.getThesisTitle()
        );

        thesis.setThesisDescription(
                formThesis.getThesisDescription()
        );


        return thesisRepository.save(
                thesis
        );
    }

	@Transactional
	public void deleteThesis(Long thesisId) {
	
	    Thesis thesis =
	            findById(thesisId);
	
	
	    thesisAssignmentRepository
	            .deleteByThesis(thesis);
	
	
	    thesisApplicationRepository
	            .deleteByThesis(thesis);
	
	
	    thesisRepository.delete(
	            thesis
	    );
	}
}