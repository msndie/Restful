package edu.school21.restful;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.CourseState;
import edu.school21.restful.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PublishTest extends Ex02ApplicationTests {

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
    void publish() throws Exception {
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
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }
}
