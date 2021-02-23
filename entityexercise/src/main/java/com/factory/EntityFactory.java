package com.factory;

import java.util.Map;

import com.entities.SampleEntity;
import com.entities.IEntity;

public class EntityFactory {

	private static EntityFactory instance = null;

	private EntityFactory() {

	}

	public static synchronized EntityFactory getInstance() {
		if (instance == null) {
			instance = new EntityFactory();
		}
		return instance;
	}

	public IEntity createEntity(String parentId, Map<String, String> data) {
		return new SampleEntity(parentId, data);
	}

}
