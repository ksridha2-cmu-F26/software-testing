import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestList {

    @Test
    public void verifyListInteractions() {
        List<String> mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    public void stubListResults() {
        LinkedList<String> mockedList = mock(LinkedList.class);

        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        assertEquals("first", mockedList.get(0));

        try {
            mockedList.get(1);
            fail();
        } catch (RuntimeException expected) {
            // Expected stubbed exception.
        }

        assertEquals(null, mockedList.get(999));
        verify(mockedList).get(0);
    }

    @Test
    public void useArgumentMatchersWhenStubbing() {
        List<String> mockedList = mock(List.class);

        when(mockedList.get(anyInt())).thenReturn("element");

        assertEquals("element", mockedList.get(999));
        verify(mockedList).get(anyInt());
    }
}