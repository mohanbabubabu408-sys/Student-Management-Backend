package com.example.STUDENT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void malformedStudentJsonReturnsReadableBadRequest() throws Exception {
		mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ invalid json"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value(
						"Invalid or missing JSON request body. Check the JSON syntax and ensure values "
								+ "match the expected field types."));
	}

	@Test
	void validStudentJsonCreatesStudent() throws Exception {
		mockMvc.perform(post("/api/departments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "departmentCode": "TEST",
						  "departmentName": "Test Department"
						}
						"""))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "registerNo": "TEST-2026-001",
						  "name": "Test Student",
						  "email": "student@example.com",
						  "phone": "+12345678901",
						  "department": "TEST",
						  "year": 1,
						  "semester": 1
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.registerNo").value("TEST-2026-001"))
				.andExpect(jsonPath("$.name").value("Test Student"));
	}

	@Test
	void departmentManagementPreservesStudentApiBehavior() throws Exception {
		MvcResult departmentResult = mockMvc.perform(post("/api/departments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "departmentCode": "IT-TEST",
						  "departmentName": "Information Technology"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.departmentCode").value("IT-TEST"))
				.andReturn();
		Long departmentId = idFromLocation(departmentResult);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/departments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[?(@.departmentCode == 'IT-TEST')]").exists());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/departments/{id}", departmentId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.departmentName").value("Information Technology"));

		mockMvc.perform(post("/api/departments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "departmentCode": "it-test",
						  "departmentName": "Duplicate"
						}
						"""))
				.andExpect(status().isConflict());

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.put("/api/departments/{id}", departmentId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "departmentCode": "IT-TEST",
						  "departmentName": "IT and Computing"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.departmentName").value("IT and Computing"));

		mockMvc.perform(post("/api/departments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "departmentCode": " ",
						  "departmentName": ""
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Validation failed"));

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/departments/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Department not found"));

		MvcResult studentResult = mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "registerNo": "IT-TEST-STUDENT",
						  "name": "Department Student",
						  "email": "it.student@example.com",
						  "phone": "+12345678902",
						  "department": "IT-TEST",
						  "year": 2,
						  "semester": 3
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.department").value("IT and Computing"))
				.andReturn();
		Long studentId = idFromLocation(studentResult);

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/departments/{id}/students", departmentId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].registerNo").value("IT-TEST-STUDENT"));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[?(@.registerNo == 'IT-TEST-STUDENT')]").exists());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/students")
				.param("department", "IT-TEST")
				.param("year", "2")
				.param("semester", "3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].registerNo").value("IT-TEST-STUDENT"));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/students/search")
				.param("name", "Department Student"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].registerNo").value("IT-TEST-STUDENT"));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.get("/api/students/{id}", studentId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.department").value("IT and Computing"));
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.put("/api/students/{id}", studentId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "registerNo": "IT-TEST-STUDENT",
						  "name": "Updated Department Student",
						  "email": "it.student@example.com",
						  "phone": "+12345678902",
						  "department": "IT-TEST",
						  "year": 2,
						  "semester": 3
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Department Student"));

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/api/departments/{id}", departmentId))
				.andExpect(status().isConflict());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/api/students/{id}", studentId))
				.andExpect(status().isNoContent());
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/api/departments/{id}", departmentId))
				.andExpect(status().isNoContent());
	}

	private Long idFromLocation(MvcResult result) {
		String location = result.getResponse().getHeader("Location");
		return Long.valueOf(location.substring(location.lastIndexOf('/') + 1));
	}

}
