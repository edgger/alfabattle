package com.github.edgger.alfabattle.task3.service;

import com.github.edgger.alfabattle.task3.dto.BranchDto;
import com.github.edgger.alfabattle.task3.dto.BranchDtoWithPredicting;
import com.github.edgger.alfabattle.task3.entity.Branch;
import com.github.edgger.alfabattle.task3.entity.BranchRepository;
import com.github.edgger.alfabattle.task3.entity.QueueLogRepository;
import com.github.edgger.alfabattle.task3.exception.BranchNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Slf4j
@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final QueueLogRepository queueLogRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository,
                         QueueLogRepository queueLogRepository) {
        this.branchRepository = branchRepository;
        this.queueLogRepository = queueLogRepository;
    }

    public BranchDto getBranchById(Long id) {
        return branchRepository.findById(id)
                .map(this::mapToBranchDto)
                .orElseThrow(BranchNotFoundException::new);
    }

    public BranchDto getNearestBranch(double targetLat, double targetLon) {
        return branchRepository.findAll()
                .stream()
                .min(Comparator.comparingDouble(branch ->
                        distanceMeters(targetLat, targetLon, branch.getLat(), branch.getLon())))
                .map(branch -> mapToBranchesWithDistance(branch,
                        Math.round(distanceMeters(targetLat, targetLon, branch.getLat(), branch.getLon()))))
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional(readOnly = true)
    public BranchDtoWithPredicting getBranchWithPredicting(Long id, Integer dayOfWeek, Integer hourOfDay) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(BranchNotFoundException::new);

        double[] waitSeconds = branch.getQueueLog().stream()
                .filter(queueLog -> queueLog.getData().getDayOfWeek().getValue() == dayOfWeek)
                .filter(queueLog -> queueLog.getEndWait().getHour() == hourOfDay)
                .map(value -> ChronoUnit.SECONDS.between(value.getStartWait(), value.getEndWait()))
                .mapToDouble(Long::doubleValue)
                .toArray();
        Median median = new Median();
        median.setData(waitSeconds);
        long predictingSec = Math.round(median.evaluate());
        return mapToBranchDtoWithPredicting(branch, dayOfWeek, hourOfDay, predictingSec);
    }

    private BranchDto mapToBranchDto(Branch branch) {
        return new BranchDto(branch.getId(), branch.getAddress(), branch.getLon(), branch.getLat(), branch.getTitle());
    }

    private BranchDto mapToBranchesWithDistance(Branch branch, long distance) {
        return new BranchDto(branch.getId(), branch.getAddress(), branch.getLon(), branch.getLat(), branch.getTitle(), distance);
    }

    private BranchDtoWithPredicting mapToBranchDtoWithPredicting(Branch branch, Integer dayOfWeek, Integer hourOfDay, Long predicting) {
        return new BranchDtoWithPredicting(branch.getId(), branch.getAddress(), branch.getLat(), branch.getLon(), branch.getTitle(), dayOfWeek, hourOfDay, predicting);
    }

    public double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final int r = 6371; // radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c * 1000; // convert to meters
    }
}
