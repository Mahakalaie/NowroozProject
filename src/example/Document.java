package example;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Document extends Entity implements Trackable {

    public static final int DOCUMENT_ENTITY_CODE = 15;

    private String content;
    private Date creationDate;
    private Date lastModificationDate;

    public Document(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Document copy()
    {

    }

    @Override
    public int getEntityCode() {
        return DOCUMENT_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(Date date)
    {
        creationDate = date;
    }

    @Override
    public Date getCreationDate()
    {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date)
    {
        lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

}
