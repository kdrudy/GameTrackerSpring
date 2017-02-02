package com.theironyard.services;

import com.theironyard.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by kdrudy on 12/21/16.
 */
public interface GameRepository extends PagingAndSortingRepository<Game, Integer> {
    Page<Game> findByGenre(Pageable pageable, String genre);
    Page<Game> findByReleaseYear(Pageable pageable, int year);

    Page<Game> findByGenreOrderByReleaseYearDesc(Pageable pageable, String genre);

    @Query("SELECT g FROM Game g WHERE g.name LIKE ?1%")
    Page<Game> findByNameStartsWith(Pageable pageable, String name);
}
