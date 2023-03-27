package org.springframework.data.solr.core.query.result;

public interface WithCursor {
    
    public String getCursor();
    public void setCursor(String cursor);

}
