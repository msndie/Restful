package edu.school21.restful;

import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import edu.school21.restful.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class Ex02ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private static User teacher;
    private static User admin;
    private String tokenTeacher;
    private String tokenAdmin;

    @BeforeAll
    public static void beforeAll() {
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
        when(userRepository.findByLogin("teacher")).thenReturn(Optional.of(teacher));
        when(userRepository.findByLogin("admin")).thenReturn(Optional.of(admin));
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
        assertNotNull(userRepository);
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
}
