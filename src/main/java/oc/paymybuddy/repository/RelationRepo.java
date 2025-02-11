package oc.paymybuddy.repository;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepo extends JpaRepository<Relation, RelationId> {

}
