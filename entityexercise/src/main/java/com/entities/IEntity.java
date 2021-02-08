package com.entities;

import java.util.Map;
import java.util.Set;

public interface IEntity {
	// Returns a unique identifier
	String getID();

	// Returns the sub-entities of this entity
	Set<SampleEntity> getSubEntities();

	// Returns a set of key-value data belonging to this entity
	Map<String, String> getData();
}