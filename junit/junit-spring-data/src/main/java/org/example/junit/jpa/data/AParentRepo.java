package org.example.junit.jpa.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AParentRepo extends CrudRepository<AParent,Long> {
}
