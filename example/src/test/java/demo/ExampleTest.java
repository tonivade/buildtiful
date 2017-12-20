package demo;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class ExampleTest 
{
	@Test
	public void testSayHello()
	{
		assertThat(new Example().sayHello("Workd!"), equalTo("Hello World!"));
	}
}