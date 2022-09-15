package edu.school21.restful;

import edu.school21.restful.model.Course;
import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private static Course course;
    private static User user;
    private static User teacher;
    private static User admin;
    private String tokenTeacher;
    private String tokenAdmin;

    @BeforeAll
    public static void beforeAll() {
        DateTimeFormatter fmtForDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse("26/02/2022", fmtForDate);
        LocalDate end = LocalDate.parse("01/03/2022", fmtForDate);
        course = new Course(1L,
                start,
                end,
                "Course one",
                "Course for testing",
                new LinkedHashSet<>(),
                null,
                null);
        user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Testoviy");
        user.setRole(Role.STUDENT);
        user.setLogin("Student1");
        user.setPassword("qwe");
        teacher = new User();
        teacher.setId(1L);
        teacher.setFirstName("teacher");
        teacher.setLastName("teacherskiy");
        teacher.setLogin("teacher");
        teacher.setRole(Role.TEACHER);
        teacher.setPassword("$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi");
        admin = new User();
        admin.setPassword("$2a$12$NZ5.MRdSch5OHENQwssQieieIjwI0ao7et0Ql0tVosLn26SedeBaW");
        admin.setId(1L);
        admin.setRole(Role.ADMINISTRATOR);
        admin.setFirstName("admin");
        admin.setLastName("adminskiy");
        admin.setLogin("admin");
    }

    @BeforeEach
    void setUp() throws Exception {
        when(userService.findByLogin("teacher")).thenReturn(Optional.of(teacher));
        when(userService.findByLogin("admin")).thenReturn(Optional.of(admin));
        MvcResult result = mockMvc.perform(post("/signUp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"teacher\", \"password\":\"qwe\"}"))
                .andExpect(status().isOk())
                .andReturn();
        tokenTeacher = result.getResponse().getContentAsString();
        result = mockMvc.perform(post("/signUp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\", \"password\":\"123QWEasd\"}"))
                .andExpect(status().isOk())
                .andReturn();
        tokenAdmin = result.getResponse().getContentAsString();
    }

    @Test
    void shouldCreateMockAndBeans() {
        assertNotNull(mockMvc);
        assertNotNull(courseService);
        assertNotNull(lessonService);
        assertNotNull(userService);
    }

    @Test
    void getAllCourses() throws Exception {
        when(courseService.findAll()).thenReturn(Stream.of(course)
                .collect(Collectors.toList()));
        mockMvc.perform(get("/courses")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0]['course'].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0]['course'].startDate", Matchers.is("26/02/2022")))
                .andExpect(jsonPath("$[0]['course'].endDate", Matchers.is("01/03/2022")))
                .andExpect(jsonPath("$[0]['course'].name", Matchers.is("Course one")))
                .andExpect(jsonPath("$[0]['course'].description", Matchers.is("Course for testing")));
    }

    @Test
    void getCourseById() throws Exception {
        when(courseService.getById(1L)).thenReturn(Optional.of(course));
        mockMvc.perform(get("/courses/1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['course'].id", Matchers.is(1)))
                .andExpect(jsonPath("$['course'].startDate", Matchers.is("26/02/2022")))
                .andExpect(jsonPath("$['course'].endDate", Matchers.is("01/03/2022")))
                .andExpect(jsonPath("$['course'].name", Matchers.is("Course one")))
                .andExpect(jsonPath("$['course'].description", Matchers.is("Course for testing")));
    }

    @Test
    void postStudentInCourse() throws Exception {
        when(courseService.getById(1L)).thenReturn(Optional.of(course));
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(post("/courses/1/students")
                        .content("{\"id\":1}").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['user'].id", Matchers.is(1)))
                .andExpect(jsonPath("$['user'].firstName", Matchers.is("Test")))
                .andExpect(jsonPath("$['user'].lastName", Matchers.is("Testoviy")));
    }

    @Test
    void putCourseById() throws Exception {
        when(courseService.existsById(1L)).thenReturn(true);
        mockMvc.perform(put("/courses/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"startDate\":\"25/02/2022\"," +
                        "\"endDate\":\"05/03/2022\"," +
                        "\"name\":\"Changed\"," +
                        "\"description\":\"Changed course\"}")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['course'].id", Matchers.is(1)))
                .andExpect(jsonPath("$['course'].startDate", Matchers.is("25/02/2022")))
                .andExpect(jsonPath("$['course'].endDate", Matchers.is("05/03/2022")))
                .andExpect(jsonPath("$['course'].name", Matchers.is("Changed")))
                .andExpect(jsonPath("$['course'].description", Matchers.is("Changed course")));

    }

    @Test
    void deleteUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['user'].id", Matchers.is(1)))
                .andExpect(jsonPath("$['user'].firstName", Matchers.is(user.getFirstName())))
                .andExpect(jsonPath("$['user'].lastName", Matchers.is(user.getLastName())))
                .andExpect(jsonPath("$['user'].login", Matchers.is(user.getLogin())))
                .andExpect(jsonPath("$['user'].role", Matchers.is(user.getRole().name())));
    }

    @Test
    void shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/courses")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/courses/1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/courses/1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/courses/1/lessons"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/courses/1/students"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/courses/1/teachers"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/courses"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/courses/1/lessons"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/courses/1/students"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/courses/1/teachers"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/users"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/courses/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/courses/1/lessons/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/courses/1/lessons/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/courses/1/students/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/courses/1/teachers/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnErrorPagination() throws Exception {
        mockMvc.perform(get("/courses?page=-1&size=0")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
        mockMvc.perform(get("/courses?page=0&size=0")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
        mockMvc.perform(get("/courses?page=-1&size=1")
                        .header("Authorization", "Bearer " + tokenTeacher))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error'].message", Matchers.is("Bad request")))
                .andExpect(jsonPath("$['error'].status", Matchers.is(400)));
    }

}
