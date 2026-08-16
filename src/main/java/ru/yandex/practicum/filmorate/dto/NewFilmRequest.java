package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.utils.ReleaseDateValid;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class NewFilmRequest {
    @NonNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NonNull
    @ReleaseDateValid()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    @Positive
    private Integer duration;
    private List<GenreDto> genres;
    private RatingDto mpa;
}