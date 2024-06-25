package com.bodytok.healthdiary.repository.Image;

import com.bodytok.healthdiary.domain.IBaseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface ImageRepository<T extends IBaseImage> extends JpaRepository<T, Long> {
}
