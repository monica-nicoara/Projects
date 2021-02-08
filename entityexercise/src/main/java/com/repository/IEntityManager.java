package com.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.entities.SampleEntity;

public interface IEntityManager {

	boolean add(final SampleEntity entity);

	ArrayList<SampleEntity> list();

	Optional<SampleEntity> getEntityById(final String id);

}
