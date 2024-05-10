package com.epam.gymapptrainingsessionservice.stat;

import com.epam.gymapptrainingsessionservice.api.ActionType;
import com.epam.gymapptrainingsessionservice.api.FullStatRequest;
import com.epam.gymapptrainingsessionservice.api.MonthlyStatRequest;
import com.epam.gymapptrainingsessionservice.api.UpdateStatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@RestController()
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/api/v1/trainer-full-stats")
    @Operation(summary = "Get full stats for trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer full stats retrieved successfully")
    })
    public ResponseEntity<Map<Integer, List<Map<String, Integer>>>> getTrainerFullStats(
            @Valid @RequestBody FullStatRequest fullStatRequest
    ) {
        log.info("\n\n ------- in controller: trainer-full-stats ----------------- \n\n ");
        Integer trainerId = fullStatRequest.getTrainerId();
        List<Stat> fullStatsOfTrainer = statsService.getStatByTrainerId(trainerId);

        Map<Integer, List<Map<String, Integer>>> responseMap = new HashMap<>();

        for (Stat stat : fullStatsOfTrainer) {
            updateResponseMap(stat, responseMap);
            //            int year = stat.getYear();
            //            Integer monthInt = stat.getMonth();
            //            String monthString = convertMonthIntToMonthString(monthInt);
            //            int minutes = stat.getMinutesMonthlyTotal();
            //            responseMap.putIfAbsent(year, new ArrayList<>());
            //            List<Map<String, Integer>> yearStats = responseMap.get(year);
            //            Map<String, Integer> monthMinutesPair = new HashMap<>();
            //            monthMinutesPair.put(monthString, minutes);
            //            yearStats.add(monthMinutesPair);
        }

        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/api/v1/trainer-monthly-stats")
    @Operation(summary = "Get Trainer stats (total minutes of training sessions) for a given month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer stats for a given month retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer stats not found for the given month")
    })
    public ResponseEntity<Integer> getTrainerMonthlyStats(
            @Valid @RequestBody MonthlyStatRequest monthlyStatRequest
    ) {
        log.info("\n\n ------- in controller: trainer-monthly-stats ----------------- \n\n ");
        Integer trainerId = monthlyStatRequest.getTrainerId();
        Integer year = monthlyStatRequest.getYear();
        Integer month = monthlyStatRequest.getMonth();
        Optional<Stat> statOptional = statsService.getByTrainerIdAndYearAndMonth(trainerId, year, month);
        if (statOptional.isPresent()) {
            return ResponseEntity.ok(statOptional.get().getMinutesMonthlyTotal());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/v1/trainer-stats-update")
    @Operation(summary = "update/create trainer stats for a given month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer stats updated successfully"),
            @ApiResponse(responseCode = "201", description = "Trainer stats created successfully")
    })
    public ResponseEntity<Void> updateTrainerStats(
            @Valid @RequestBody UpdateStatRequest updateStatRequest
    ) {

        log.info("\n\n ------- in controller: trainer-stats-update ----------------- \n\n ");
        //        Integer trainerId = updateStatRequest.getTrainerId();
        //        Date date = updateStatRequest.getTrainingDate();
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(date);
        //        Integer year = calendar.get(Calendar.YEAR);
        //        Integer month = calendar.get(Calendar.MONTH) + 1;
        //        Optional<Stat> statOptional = statsService.getByTrainerIdAndYearAndMonth(trainerId, year, month);
        Optional<Stat> statOptional = getStatOptional(updateStatRequest);

        boolean actionTypeIsAdd = updateStatRequest.getActionType() == ActionType.ADD;
        Integer minutes = updateStatRequest.getDuration();
        log.info("updating stats");

        if (statOptional.isPresent()) {
            Stat stat = statOptional.get();
            Integer currentMinutes = stat.getMinutesMonthlyTotal();
            int newMinutes;
            if (actionTypeIsAdd) {
                newMinutes = currentMinutes + minutes;
            } else {
                newMinutes = currentMinutes - minutes;
            }

            stat.setMinutesMonthlyTotal(newMinutes);
            statsService.updateStat(stat);
            return ResponseEntity.ok().build();
        }

        Stat stat = getStat(updateStatRequest);
        statsService.createStat(stat);

        return ResponseEntity.ok().build();
    }

    private void updateResponseMap(Stat stat, Map<Integer, List<Map<String, Integer>>> responseMap) {
        int year = stat.getYear();
        Integer monthInt = stat.getMonth();
        String monthString = convertMonthIntToMonthString(monthInt);
        int minutes = stat.getMinutesMonthlyTotal();
        responseMap.putIfAbsent(year, new ArrayList<>());
        List<Map<String, Integer>> yearStats = responseMap.get(year);
        Map<String, Integer> monthMinutesPair = new HashMap<>();
        monthMinutesPair.put(monthString, minutes);
        log.info("\n\n -------year: {}, month: {}, minutes {} ---- in updateResponseMap----- \n\n ", year, monthString,
                 minutes);
        yearStats.add(monthMinutesPair);
    }

    private String convertMonthIntToMonthString(int monthInt) {
        LocalDate localDate = LocalDate.of(2000, monthInt, 1);
        return localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    private Optional<Stat> getStatOptional(UpdateStatRequest updateStatRequest) {
        Integer trainerId = updateStatRequest.getTrainerId();
        Date date = updateStatRequest.getTrainingDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        return statsService.getByTrainerIdAndYearAndMonth(trainerId, year, month);
    }

    private Stat getStat(UpdateStatRequest updateStatRequest) {
        Stat stat = new Stat();
        Integer trainerId = updateStatRequest.getTrainerId();
        Date date = updateStatRequest.getTrainingDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        stat.setTrainerId(trainerId);
        stat.setYear(year);
        stat.setMonth(month);
        stat.setMinutesMonthlyTotal(updateStatRequest.getDuration());
        return stat;
    }
}
