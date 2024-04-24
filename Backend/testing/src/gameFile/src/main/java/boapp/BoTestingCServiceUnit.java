package boapp;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import boapp.Model.StringEntity;
import boapp.Model.StringRepo;
// Import Local classes
import boapp.Service.CapitalizeService;

@RunWith(SpringRunner.class)
public class BoTestingCServiceUnit {

	@TestConfiguration
  static class StringContextConfiguration { // can be named whatever

      @Bean
      public CapitalizeService cService() {
          return new CapitalizeService();
      }

      @Bean
			StringRepo getRepo() {
        return mock(StringRepo.class);
     }
	}

	@Autowired
	private CapitalizeService cs;

	@Autowired
	private StringRepo repo;  // this is the mock one

	@Test
	public void testCapitalize()  {
	// Set up MOCK methods for the REPO
    List<StringEntity> l = new ArrayList<StringEntity>();

    // mock the findAll method return the fixed list
	  when(repo.findAll()).thenReturn(l);

		// mock the save() method to save argument to the list
  	when(repo.save((StringEntity)any(StringEntity.class)))
			.thenAnswer(x -> {
				  StringEntity r = x.getArgument(0);
				  l.add(r);
				  return null;
	  });

		// what does this print
		System.out.println(cs.capitalize("hello"));
		//assertEquals("HELLO", cs.capitalize("hello"));
	}

}
