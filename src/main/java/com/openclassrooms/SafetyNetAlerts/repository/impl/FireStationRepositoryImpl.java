package com.openclassrooms.SafetyNetAlerts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;

@Repository
public class FireStationRepositoryImpl implements FireStationRepository {

    private static final Logger logger = LogManager.getLogger(FireStationRepositoryImpl.class);

	private List<FireStation> fireStations = new ArrayList<>();
	
	@Bean
	@Scope("singleton")
	public FireStationRepositoryImpl fireStationRepoSingleton() {
		return new FireStationRepositoryImpl();
	}

    @Override
    public void add(FireStation fireStation) {
        logger.info("Repo add fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Add payload: {}", fireStation);
        try {
            fireStations.add(fireStation);
            logger.info("Repo add -> OK (size={})", fireStations.size());
        } catch (Exception e) {
            logger.error("Repo add FAILED for mapping {} -> {}: {}",
                    fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(FireStation fireStation) {
        logger.info("Repo delete fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        try {
            boolean removed = fireStations.remove(fireStation);
            if (removed) {
                logger.info("Repo delete -> OK (size={})", fireStations.size());
            } else {
                logger.error("Repo delete -> mapping not found: {} -> {}",
                        fireStation.getAddress(), fireStation.getStation());
            }
        } catch (Exception e) {
            logger.error("Repo delete FAILED for mapping {} -> {}: {}",
                    fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void update(FireStation fireStation) {
        logger.info("Repo update fireStation mapping {} -> station {}",
                fireStation.getAddress(), fireStation.getStation());
        logger.debug("Update payload: {}", fireStation);
        try {
            boolean updated = false;
            for (FireStation f : fireStations) {
                if (f.equals(fireStation)) {
                    f.setStation(fireStation.getStation());
                    updated = true;
                    break;
                }
            }
            if (updated) {
                logger.info("Repo update -> OK for mapping {} -> {}",
                        fireStation.getAddress(), fireStation.getStation());
                logger.debug("Repo update result: {}", fireStation);
            } else {
                logger.error("Repo update -> mapping not found: {} -> {}",
                        fireStation.getAddress(), fireStation.getStation());
            }
        } catch (Exception e) {
            logger.error("Repo update FAILED for mapping {} -> {}: {}",
                    fireStation.getAddress(), fireStation.getStation(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public FireStation get(FireStation fireStation) {
        logger.debug("Repo get fireStation for address='{}'", fireStation.getAddress());
        try {
            for (FireStation f : fireStations) {
                if (f.equals(fireStation)) {
                    logger.info("Repo get -> found mapping {} -> station {}", f.getAddress(), f.getStation());
                    logger.debug("Repo get result: {}", f);
                    return f;
                }
            }
            logger.info("Repo get -> result=empty (address='{}')", fireStation.getAddress());
            return null;
        } catch (Exception e) {
            logger.error("Repo get FAILED for address {}: {}", fireStation.getAddress(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<FireStation> findAll() {
        logger.info("Repo findAll fireStations");
        try {
            logger.info("Repo findAll -> count={}", fireStations.size());
            logger.debug("Repo findAll sample: {}", fireStations.stream().limit(3).toList());
            return fireStations;
        } catch (Exception e) {
            logger.error("Repo findAll FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void addAll(List<FireStation> element) {
        try {
            if (element != null) {
                fireStations.addAll(element);
            }
            logger.info("Initial data loaded: firestations={}", fireStations.size());
        } catch (Exception e) {
            logger.error("Repo add all FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void clear() {
        logger.debug("Repo clear fireStations ({} -> 0)", fireStations.size());
        fireStations.clear();
        logger.info("Repo clear -> OK");
    }
}
