package hr.algebra.aisi_project.repository;

import hr.algebra.aisi_project.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
