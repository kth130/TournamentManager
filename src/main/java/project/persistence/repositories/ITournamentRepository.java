package project.persistence.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import project.persistence.entities.Tournament;

import java.util.List;

public interface ITournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament save(Tournament tournament);

    void delete(Tournament tournament);

    List<Tournament> findAll();

    List<Tournament> findAllByOrderByIdDesc();

    @Query(value = "SELECT p FROM Tournament p WHERE p.id = ?1")
    Tournament findOne(Long id);

    @Query(value ="SELECT t FROM Tournament t WHERE UPPER(t.name) LIKE %?1%")
    List<Tournament> findByNameSearch(String search);

    List<Tournament> findByUserId(Long userId);
}
