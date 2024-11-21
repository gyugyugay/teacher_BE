package com.teacher.qualification.repository.question;

import com.teacher.qualification.domain.question.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Choice, Long> {
}

