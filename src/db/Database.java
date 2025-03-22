package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {

    private static ArrayList<Entity> entities = new ArrayList<>();

    private Database(){}

    public static int ids = 1;

    public static void add(Entity e)
    {
        entities.add(e);

        e.id = ids;

        ids++ ;
    }

    public static Entity get(int id)
    {
        for(Entity entity : entities)
        {
            if(entity.id == id)
                return entity;
        }

        throw new EntityNotFoundException(id);
    }

    public static void delete(int id)
    {
        entities.remove(get(id));
    }

    public static void update(Entity e)
    {
        for(int i = 0; i < entities.size(); i++)
        {
            if(entities.get(i).id == e.id)
            {
                entities.set(i, e);
                return;
            }
        }

        throw new EntityNotFoundException();
    }
}
