package ru.gb.shopservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.gb.shopservice.aspect.TrackUserActionAspect;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    public TrackUserActionAspect aspect()
    {
        return new TrackUserActionAspect();
    }

}
