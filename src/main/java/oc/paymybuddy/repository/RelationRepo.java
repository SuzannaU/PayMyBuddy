package oc.paymybuddy.repository;

import oc.paymybuddy.model.Relation;
import oc.paymybuddy.model.RelationId;
import oc.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepo extends JpaRepository<Relation, RelationId> {

    @Query (value="SELECT * FROM user_user WHERE user1_id=?1 OR user2_id=?2", nativeQuery=true)
    List<Relation> getRelationsByUserId(int user1Id,int user2Id);
}
