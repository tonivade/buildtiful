package test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class ExampleTest 
{
	@Test
	public void sayHelloTest()
	{
		assertThat(new Example().sayHello("Workd!"), equalTo("Hello World!"));
	}
}