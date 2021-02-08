package com.service;

import java.util.List;
import java.util.Optional;

import com.entities.SampleEntity;
import com.repository.IEntityManager;

import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.annotation.RequestParam;
import io.advantageous.qbit.reactive.Callback;

@RequestMapping(value = "/entity-service", description = "Entities service")
public class EntityService {

	private IEntityManager entitiesManager;

	public EntityService(IEntityManager entitiesManager) {
		this.entitiesManager = entitiesManager;
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			description = "ping", summary = "ping",
			returnDescription = "returns true if successful")
	public void ping(Callback<Boolean> callback) {
		callback.accept(true);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST,
			description = "add an entity to the list", summary = "adds entity",
			returnDescription = "returns message if successful")
	public void add(final Callback<String> callback, final SampleEntity entity) { 
		boolean result = entitiesManager.add(entity);
		if (result) {
			callback.accept("Entity with id " + entity.getID() + " was added.");
		} else {
			callback.onError(new NullPointerException("No entity with id " + entity.getParentId() + " was found."));
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET,
			description = "List all entities in the system",  summary = "list entities",
			returnDescription = "return list of all items in system")
	public void list(final Callback<List<SampleEntity>> callback) {
		callback.accept(entitiesManager.list());   
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET,
			description = "Get a specific entity from the system",  summary = "get entity",
			returnDescription = "return a specific entity from the system")
	public void getEntity(final Callback<SampleEntity> callback,
			@RequestParam("id") final String id) {

		Optional<SampleEntity> item = entitiesManager.getEntityById(id);
		if (item.isPresent()) {
			callback.accept(item.get());
		} else {
			callback.onError(new NullPointerException("Entity with id " + id.toString() + " was not found."));
		}  	
	}

}
