package com.bravolt.movieservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieController {
	
	private final MovieRepository repository;
	
	@PostMapping("/movie")
	public Movie create(@RequestBody Movie movie) {
		Movie newMovie = repository.save(movie);

		String createdMsg = """
				Movie created
				With Spring Boot 3 controller
				""";
		System.out.println(createdMsg);		
		
		return newMovie;
	}
	
	@GetMapping("/movies")
	public List<MovieDTO> getAll(Movie movie) {
		
		List<Movie> movieList = repository.findAll();
		List<MovieDTO> movieDtoList = makeMovieDTOList(movieList);
		
		return movieDtoList;
	}
	
	private List<MovieDTO> makeMovieDTOList(List<Movie> movieList) {
		List<MovieDTO> movieDtoList = new ArrayList<MovieDTO>();
		for(Movie movie: movieList) {
			MovieDTO movieDto =
					new MovieDTO(
							movie.getId(),
							movie.getName(),
							movie.getReleaseYear());
			movieDtoList.add(movieDto);
		}
		
		return movieDtoList;
	}
}
