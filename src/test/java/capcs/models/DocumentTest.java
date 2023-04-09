package capcs.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testGetPath() {
        Document document = new Document("path", "name", System.currentTimeMillis(), 123);
        assertEquals("path/", document.getPath());
    }

    @Test
    public void testGetName() {
        Document document = new Document("path", "name", System.currentTimeMillis(), 123);
        assertEquals("name", document.getName());
    }

    @Test
    public void testGetDate() {
        long date = System.currentTimeMillis();
        Document document = new Document("path", "name", date, 123);
        assertEquals(date, document.getDate());
    }

    @Test
    public void testGetSize() {
        Document document = new Document("path", "name", System.currentTimeMillis(), 123);
        assertEquals(123, document.getSize());
    }

    @Test
    public void testToString() {
        long date = System.currentTimeMillis();
        Document document = new Document("path", "name", date, 123);
        assertEquals("path/name (" + date + ")", document.toString());
    }

    @Test
    public void testEquals() {
        Document document = new Document("path", "name", System.currentTimeMillis(), 123);
        Document document2 = new Document("path", "name2", System.currentTimeMillis(), 123);
        assertEquals(document, document);
        assertNotEquals(document, document2);
    }

    @Test
    public void testHashCode() {
        Document document = new Document("path", "name", System.currentTimeMillis(), 123);
        Document document2 = new Document("path", "name2", System.currentTimeMillis(), 123);
        assertEquals(document.hashCode(), document.hashCode());
        assertNotEquals(document.hashCode(), document2.hashCode());
    }
    
}
