package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database {

    private static ArrayList<Entity> entities = new ArrayList<>();

    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private Database(){}

    public static int ids = 1;

    public static void registerValidator(int entityCode, Validator validator)
    {
        if(validators.containsKey(entityCode))
            throw new IllegalArgumentException("A validator already exists for the given key");

        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException
    {
        if(validators.containsKey(e.getEntityCode())) {
            Validator validator = validators.get(e.getEntityCode());
            validator.validate(e);
        }

        e.id = ids;

        Date addingToDatabaseMoment = new Date();

        if(e instanceof Trackable)
        {
            ((Trackable) e).setCreationDate(addingToDatabaseMoment);
            ((Trackable) e).setLastModificationDate(addingToDatabaseMoment);
        }
        
        entities.add(e.copy());
        
        ids++ ;
    }

    public static Entity get(int id)
    {
        for(Entity entity : entities)
        {
            if(entity.id == id)
                return entity.copy();
        }

        throw new EntityNotFoundException(id);
    }

    public static void delete(int id)
    {
        entities.removeIf(entity -> entity.id == id);
    }

    public static void update(Entity e) throws InvalidEntityException
    {
        if(validators.containsKey(e.getEntityCode())) {
            Validator validator = validators.get(e.getEntityCode());
            validator.validate(e);
        }

        for(int i = 0; i < entities.size(); i++)
        {
            if(entities.get(i).id != e.id)
                continue;

            Date updateInDatabaseMoment = new Date();

            if( e instanceof Trackable)
                ((Trackable) e).setLastModificationDate(updateInDatabaseMoment);

            entities.set(i, e.copy());
        }

        throw new EntityNotFoundException();
    }

    public static ArrayList<Entity> getAll(int entityCode) {

        ArrayList<Entity> entitiesWithThisCode = new ArrayList<>();

        for(int i = 0; i < entities.size(); i++)
        {
            if(entities.get(i).getEntityCode() != entityCode)
                continue;

            entitiesWithThisCode.add(entities.get(i));
        }

        return entitiesWithThisCode;
    }
}
