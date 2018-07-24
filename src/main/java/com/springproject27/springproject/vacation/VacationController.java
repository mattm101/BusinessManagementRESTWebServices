package com.springproject27.springproject.vacation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user/{userId}/vacation", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class VacationController {
    private final VacationService vacationService;


    @GetMapping
    public List<Vacation> getVacations(@PathVariable Long userId) {
        return vacationService.findAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public Vacation getVacation(@PathVariable Long id) {
        return vacationService.getVacation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacation createVacation(@Valid @RequestBody Vacation vacation, @PathVariable Long userId) {
        vacationService.createVacation(vacation, userId);
        return vacation;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateVacation(@PathVariable Long userId, @PathVariable Long id, @Valid @RequestBody Vacation vacation) {
        vacationService.updateVacation(userId, id, vacation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVacation(@PathVariable Long userId, @PathVariable Long id) {
        vacationService.deleteVacation(userId, id);
    }
}
