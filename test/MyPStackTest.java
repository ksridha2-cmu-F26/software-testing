/* 
 We strongly suggest that you use a dependency management tool 
 such as Maven or Gradle to add Hamcrest and Mockito dependencies
*/


import org.junit.Before;
import org.junit.Test;

/* these import statements are for latest versions of Hamcrest */ 
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;


import static org.mockito.Mockito.*;

import java.beans.Transient;

import org.mockito.InOrder;


public class MyPStackTest {

    private IDataBase db;
    private MyPStack s;
    
    
	@Before
	public void setUp() throws Exception {
	  //
        db = mock(IDataBase.class);
        s = new MyPStack(db);
	}

	@Test
	public void justATemplate() {
      //
	}

	@Test
	public void canInstantiateWithDataBase() {
		assertThat(s, is(notNullValue()));
	}

	@Test
    public void initiallyThereIsNoEntryInDB() 
		throws OverflowException, InvalidOperationException {
			verify(db, never()).create(anyString(), anyInt());
	}

	@Test
	public void pushSavesTopInDBDuringFirstPush() 
		throws OverflowException, InvalidOperationException {
		String id = s.getId();
		s.push(100);
		verify(db).create(id, s.peek());
	}

	@Test
	public void pushUpdatesTopInDBIncConsecutivePush()
			throws OverflowException, InvalidOperationException {
		String id = s.getId();
		s.push(100);
		s.push(200);
		verify(db).update(id, s.peek());
	}

	@Test
	public void popUpdatesTopInDB() 
			throws OverflowException, InvalidOperationException {
		String id = s.getId();
		s.push(100);
		s.push(200);
		s.pop();
		verify(db).update(id, s.peek());
	}

	@Test 
	public void resetReadWriteValueFromDBWhenStackIsNonEmpty() 
			throws OverflowException, InvalidOperationException {
		String id = s.getId();
		s.push(100);
		when(db.read(id)).thenReturn(100);
		s.reset();
		assertThat(s.peek(), is((equalTo(100))));
	}	

	@Test 
	public void afterResetStackHasOnlyLastTopElement() 
			throws OverflowException, InvalidOperationException {
		String id = s.getId();
		s.push(100);
		s.push(200);
		when(db.read(id)).thenReturn(s.peek());
		s.reset();
		assertThat(s.size(), is(equalTo(1)));
		assertThat(s.peek(), is((equalTo(200))));
	}
}
