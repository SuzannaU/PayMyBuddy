package oc.paymybuddy.service;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.repository.RelationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationService {

    @Autowired
    private RelationRepo relationRepo;

    public Relation getRelationById(RelationId id) {
        return relationRepo.findById(id).get();
    }

    public List<Relation> getAllRelations() {
        return relationRepo.findAll();
//        List<Relation> relations = relationRepo.findAll();
//        return relations.stream()
//                .map(r -> r.getId())
//                .collect(Collectors.toList());
    }
}
