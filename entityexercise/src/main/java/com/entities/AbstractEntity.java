package com.entities;

import java.util.Map;
import java.util.Set;

public abstract class AbstractEntity implements IEntity{
	@Override
	public abstract String getID();

	@Override
	public abstract Set<SampleEntity> getSubEntities();

	@Override
	public abstract Map<String, String> getData();
}
