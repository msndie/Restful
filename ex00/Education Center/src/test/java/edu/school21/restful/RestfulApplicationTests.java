package edu.school21.restful;

import edu.school21.restful.model.Course;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RestfulApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        DateTimeFormatter fmtForDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Course course = new Course(1L,
                LocalDate.parse("26/02/2022", fmtForDate),
                LocalDate.parse("02/03/2022", fmtForDate),
                "Course two",
                "Second course for testing",
                null,
                null,
                null);
        when(courseService.save(course)).thenReturn(course);
        when(courseService.findAll()).thenReturn(Stream.of(
                new Course(1L,
                        LocalDate.parse("26/02/2022", fmtForDate),
                        LocalDate.parse("01/03/2022", fmtForDate),
                        "Course one",
                        "Course for testing",
                        null,
                        null,
                        null))
                .collect(Collectors.toList()));
//        when(courseService.existsById(1L)).thenReturn(true);
    }

    @Test
    void shouldCreateMockAndBeans() {
        assertNotNull(mockMvc);
        assertNotNull(courseService);
        assertNotNull(lessonService);
        assertNotNull(userService);
    }

    @Test
    void shouldReturnOneCourseGetCourses() throws Exception {
        mockMvc.perform(get("/courses"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0]['course'].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0]['course'].startDate", Matchers.is("26/02/2022")))
                .andExpect(jsonPath("$[0]['course'].endDate", Matchers.is("01/03/2022")))
                .andExpect(jsonPath("$[0]['course'].name", Matchers.is("Course one")))
                .andExpect(jsonPath("$[0]['course'].description", Matchers.is("Course for testing")));
    }

    @Test
    void shouldReturnOneCoursePostCourse() throws Exception {
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startDate\": \"26/02/2022\", " +
                        "\"endDate\": \"02/03/2022\", " +
                        "\"name\": \"Course two\", " +
                        "\"description\": \"Second course for testing\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['lesson'].id", Matchers.is(1)))
                .andExpect(jsonPath("$['lesson'].startDate", Matchers.is("26/02/2022")))
                .andExpect(jsonPath("$['lesson'].endDate", Matchers.is("02/03/2022")))
                .andExpect(jsonPath("$['lesson'].name", Matchers.is("Course two")))
                .andExpect(jsonPath("$['lesson'].description", Matchers.is("Second course for testing")));

    }

    @Test
    void shouldReturnError() throws Exception {
        mockMvc.perform(get("/courses?page=-1&size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
        mockMvc.perform(get("/courses?page=0&size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
        mockMvc.perform(get("/courses?page=-1&size=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
    }

}
