//package com.cm.backoffice.config;
//
//import com.cm.common.repository.CourseRepository;
//import com.cm.common.service.lesson.LessonService;
//import com.cm.common.service.user.AppUserService;
//import org.mockito.Mockito;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@Profile("test")
//@Configuration
//@EnableJpaRepositories({"com.cm.common.*"})
//public class BeanMockConfig {
//    @Bean
//    @Primary
//    public CourseRepository courseRepository() {
//        return Mockito.mock(CourseRepository.class);
//    }
//
//    @Bean
//    @Primary
//    public AppUserService appUserService() {
//        return Mockito.mock(AppUserService.class);
//    }
//
//    @Bean
//    @Primary
//    public LessonService lessonService() {
//        return Mockito.mock(LessonService.class);
//    }
//
//}
