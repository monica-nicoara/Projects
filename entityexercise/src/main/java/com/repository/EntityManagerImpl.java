package com.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.entities.SampleEntity;

public class EntityManagerImpl implements IEntityManager{

	private Map<String, SampleEntity> entitiesMap = new HashMap<>();
	private static int id = 0;

	public boolean add(final SampleEntity newEntity) {
		boolean result;

		String parentId = newEntity.getParentId();
		if (parentId.equals("null")) {
			result = addAsRoot(newEntity);
		} else {
			result = addAsSubentity(newEntity, parentId);
		}
		return result;
	}

	public ArrayList<SampleEntity> list() {
		return new ArrayList<>(entitiesMap.values());   
	}

	private int generateId() {
		return ++id;
	}

	private boolean addAsSubentity(SampleEntity entity, String parentId) {
		Optional<SampleEntity> parent = getEntityById(parentId);
		if (parent.isPresent()) {
			saveSubEntity(entity, parent.get());
			return true;
		} 
		return false;
	}

	private boolean addAsRoot(SampleEntity newEntity) {
		//Root has parent id "null"
		Optional<SampleEntity> oldRoot = getEntityById("null");
		if (oldRoot.isPresent()) {
			//The oldRoot will be added as child of the new root
			saveRootEntity(newEntity, oldRoot.get());
		} else {
			//No root defined. This is the first entity added
			saveEntity(newEntity);
		}

		return true;
	}

	private void saveSubEntity(SampleEntity entity, SampleEntity parent) {
		saveEntity(entity);
		parent.addSubEntity(entity);
	}

	private void saveRootEntity(SampleEntity newRoot, SampleEntity oldRoot) {
		newRoot.addSubEntity(oldRoot);
		saveEntity(newRoot);
		oldRoot.setParentId(newRoot.getID());
	}

	public Optional<SampleEntity> getEntityById(String id) {
		Optional<SampleEntity> entity = entitiesMap.values().stream()
				.filter(e -> e.getID().equals(id))
				.findFirst();
		return entity;
	}

	private void saveEntity(SampleEntity entity) {
		String generatedId = Integer.toString(generateId());
		entity.setID(generatedId);
		entitiesMap.put(generatedId, entity);
	}
}
