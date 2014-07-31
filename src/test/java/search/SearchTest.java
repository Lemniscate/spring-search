package search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemniscate.spring.search.OperationParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = { DemoApplication.class, DataConfig.class}) // WHY DOESN'T THIS WORK!?
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = { SearchTest.Config.class})
@WebAppConfiguration
public class SearchTest {

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties
//    @EnableJpaRepositories(basePackageClasses = UserRepository.class)
    public static class Config {// extends DataConfig{
        // TODO point to a local h2 instance

        @Bean
        public ConversionService conversionService(){
            DefaultConversionService s = new DefaultConversionService();
            s.addConverter(new Converter<String, Date>() {

                @Override
                public Date convert(String source) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        return sdf.parse(source);
                    } catch (ParseException e) {
                        return null;
                    }
                }
            });
            return s;
        }
    }

    private Map<String, Object> search, search3;

    @Inject
    private ObjectMapper mapper;

    @Inject
    private ConversionService conversionService;

    @Before
    public void setup() throws IOException {
        InputStream input = getClass().getResourceAsStream("/search.json");
        search = mapper.readValue( input, Map.class);

        search3 = mapper.readValue( getClass().getResourceAsStream("/search3.json"), Map.class);
    }

    @Test
    public void search1(){
        OperationParser parser = new OperationParser(conversionService);
        Specification spec = parser.parse(search);
        Assert.notNull(spec);
    }

    @Test
    public void search3(){
        OperationParser parser = new OperationParser(conversionService);
        Specification spec = parser.parse(search3);
        Assert.notNull(spec);
    }

}

