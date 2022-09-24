package edu.school21.restful;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.CourseState;
import edu.school21.restful.repositories.CourseRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class Ex02ApplicationTests {

    private MockMvc mockMvc;

    @MockBean
    CourseRepository courseRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void shouldCreateMockAndBeans() {
        assertNotNull(mockMvc);
        assertNotNull(courseRepository);
    }

    @Test
    void documentedTest() throws Exception {
        DateTimeFormatter fmtForDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse("26/02/2022", fmtForDate);
        LocalDate end = LocalDate.parse("01/03/2022", fmtForDate);
        Course course = new Course(1L,
                start,
                end,
                "Course one",
                "Course for testing",
                CourseState.DRAFT,
                null,
                null,
                null);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        mockMvc.perform(post("/courses/1/publish"))
                .andExpect(status().isOk())
                .andDo(document("index"));
    }
}
