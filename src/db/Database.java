package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
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
        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

        e.id = ids;

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
        entities.remove(get(id));
    }

    public static void update(Entity e) throws InvalidEntityException
    {
        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

        for(int i = 0; i < entities.size(); i++)
        {
            if(entities.get(i).id == e.id)
            {
                entities.set(i, e.copy());
                return;
            }
        }

        throw new EntityNotFoundException();
    }
}
