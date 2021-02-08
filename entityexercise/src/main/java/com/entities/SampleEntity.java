package com.entities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SampleEntity extends AbstractEntity {
	private String id, parentId;
	private Map<String, String> data;
	private Set<SampleEntity> subEntities = new HashSet<SampleEntity>();

	public SampleEntity() {
	}

	public SampleEntity(String parentId, Map<String, String> data) {
		this();
		this.parentId = parentId;
		this.data = data;
	}

	@Override
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public Set<SampleEntity> getSubEntities() {
		return subEntities;
	}

	public void addSubEntity(SampleEntity subEntity) {
		this.subEntities.add(subEntity);
	}

	@Override
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
