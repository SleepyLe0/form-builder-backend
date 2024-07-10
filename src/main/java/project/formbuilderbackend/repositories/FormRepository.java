package project.formbuilderbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.formbuilderbackend.entities.form.Form;
import project.formbuilderbackend.entities.form.FormStatus;
import project.formbuilderbackend.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findAllByUserId(Long userId);
    Optional<Form> findByIdAndUser(Long formId, UserEntity user);
}