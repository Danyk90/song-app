package com.project.resourceservice.repo;

import com.project.resourceservice.entity.Mp3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Mp3FileRepository extends JpaRepository<Mp3File, Long> {
}
