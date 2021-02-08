package com.factory;

import java.util.Map;

import com.entities.SampleEntity;
import com.entities.IEntity;

public class EntityFactory {

	private static EntityFactory instance;

	private EntityFactory() {

	}

	public static EntityFactory getInstance() {
		if (instance == null) {
			return new EntityFactory();
		}
		return instance;
	}

	public IEntity createEntity(String parentId, Map<String, String> data) {
		return new SampleEntity(parentId, data);
	}

}
