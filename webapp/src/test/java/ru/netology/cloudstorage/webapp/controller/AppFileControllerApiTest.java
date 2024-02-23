package ru.netology.cloudstorage.webapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.create.CreateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.delete.DeleteCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.download.DownloadCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.list.ListCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInput;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputRequest;
import ru.netology.cloudstorage.contracts.core.boundary.update.UpdateCloudFileInputResponse;
import ru.netology.cloudstorage.contracts.core.model.CloudFile;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatus;
import ru.netology.cloudstorage.contracts.core.model.CloudFileStatusCode;
import ru.netology.cloudstorage.contracts.core.model.FileResource;
import ru.netology.cloudstorage.core.model.CoreCloudFile;
import ru.netology.cloudstorage.core.model.CoreCloudFileStatus;
import ru.netology.cloudstorage.core.model.CoreTraceId;
import ru.netology.cloudstorage.storage.local.model.ClasspathFileResource;
import ru.netology.cloudstorage.webapp.boundary.AppCreateCloudFileInputResponse;
import ru.netology.cloudstorage.webapp.config.SecurityDisabledConfiguration;
import ru.netology.cloudstorage.webapp.factory.AppListCloudFileInputResponsePresenter;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;
import ru.netology.cloudstorage.webapp.model.TestCloudUser;
import ru.netology.cloudstorage.webapp.model.TestStorageFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ActiveProfiles("security-disabled")
@AutoConfigureMockMvc(addFilters = false, webDriverEnabled = false)
@WebMvcTest(AppFileController.class)
@Import({AuthenticationTestFactory.class, SecurityDisabledConfiguration.class,
        AppListCloudFileInputResponsePresenter.class})
class AppFileControllerApiTest {

    private static final String testFileName = "test-cloud-file";

    @MockBean
    CreateCloudFileInput createCloudFileInteractor;

    @MockBean
    ListCloudFileInput listCloudFileInteractor;

    @MockBean
    UpdateCloudFileInput updateCloudFileInteractor;

    @MockBean
    DownloadCloudFileInput downloadCloudFileInteractor;

    @MockBean
    DeleteCloudFileInput deleteCloudFileInteractor;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void givenRequest_whenGetFilesList_thenSuccess() throws Exception {
        CloudFile cloudFile = createTestCloudFile();
        ListCloudFileInputResponse inputResponse = mock(ListCloudFileInputResponse.class);
        given(inputResponse.getCloudFiles()).willReturn(List.of(cloudFile));
        given(listCloudFileInteractor.find(any())).willReturn(inputResponse);
        ArgumentCaptor<ListCloudFileInputRequest> inputRequestCaptor = ArgumentCaptor.forClass(
                ListCloudFileInputRequest.class);


        MvcResult result = mvc.perform(get("/list").queryParam("limit", "1")).andReturn();


        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        verify(listCloudFileInteractor, times(1)).find(inputRequestCaptor.capture());
        ListCloudFileInputRequest inputRequest = inputRequestCaptor.getValue();
        assertNotNull(inputRequest.getUser());
        assertNotNull(inputRequest.getTraceId());
        assertEquals(1, inputRequest.getLimit());
        String outJson = result.getResponse().getContentAsString();
        assertJson(outJson).hasSize(1)
                .at("/0").containsKeysExactlyInAnyOrder("filename", "size", "editedAt");
        assertJson(outJson).at("/0/filename").isText(cloudFile.getFileName());
        assertJson(outJson).at("/0/editedAt").isNumberEqualTo(cloudFile.getUpdatedAt().toEpochMilli());
        assertJson(outJson).at("/0/size").isNumberEqualTo(cloudFile.getStorageFile().getSize());
    }

    @Test
    void givenNewName_whenUpdateFile_thenSuccess() throws Exception {
        UUID cloudId = UUID.randomUUID();
        CloudFileStatus fileStatus = CoreCloudFileStatus.builder()
                .id(UUID.randomUUID()).code(CloudFileStatusCode.READY).build();
        UpdateCloudFileInputResponse inputResponse = mock(UpdateCloudFileInputResponse.class);
        given(inputResponse.getCloudFileId()).willReturn(cloudId);
        given(inputResponse.getCloudFileStatus()).willReturn(fileStatus);
        given(updateCloudFileInteractor.update(any())).willReturn(inputResponse);
        ArgumentCaptor<UpdateCloudFileInputRequest> inputRequestCaptor = ArgumentCaptor.forClass(
                UpdateCloudFileInputRequest.class);
        JsonNode request = objectMapper.createObjectNode().put("filename", "new-file-name");


        MvcResult result = mvc.perform(put("/file").queryParam("filename", testFileName)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                .content(request.toString())).andReturn();


        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        verify(updateCloudFileInteractor, times(1)).update(inputRequestCaptor.capture());
        UpdateCloudFileInputRequest inputRequest = inputRequestCaptor.getValue();
        assertEquals(testFileName, inputRequest.getFileName());
        assertEquals("new-file-name", inputRequest.getNewFileName());
        assertNotNull(inputRequest.getUser());
        assertNotNull(inputRequest.getTraceId());
        String outJson = result.getResponse().getContentAsString();
        assertJson(outJson).containsKeysExactlyInAnyOrder("id", "status");
        assertJson(outJson).at("/id").isEqualTo(cloudId);
        assertJson(outJson).at("/status").isText(CloudFileStatusCode.READY.name());
    }

    @Test
    void givenFilename_whenDownloadFile_thenSuccess() throws Exception {
        FileResource fileResource = new ClasspathFileResource(Path.of("db/fixture/users/01-insert-test-user.sql"));
        given(downloadCloudFileInteractor.getResource(any())).willReturn(fileResource);
        ArgumentCaptor<DownloadCloudFileInputRequest> inputRequestCaptor =
                ArgumentCaptor.forClass(DownloadCloudFileInputRequest.class);


        MvcResult result = mvc.perform(get("/file").queryParam("filename", testFileName)).andReturn();


        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.parseMediaType(fileResource.getMediaType()).toString(),
                result.getResponse().getContentType());
        assertEquals(fileResource.getSize(), result.getResponse().getContentLengthLong());
        assertEquals("form-data; name=\"attachment\"; filename=\"test-cloud-file\"",
                result.getResponse().getHeader("Content-Disposition"));
        verify(downloadCloudFileInteractor, times(1)).getResource(inputRequestCaptor.capture());
        DownloadCloudFileInputRequest inputRequest = inputRequestCaptor.getValue();
        assertEquals(testFileName, inputRequest.getFileName());
        assertNotNull(inputRequest.getUser());
        assertNotNull(inputRequest.getTraceId());
    }

    @Test
    void givenFilename_whenDeleteFile_thenSuccess() throws Exception {
        UUID cloudId = UUID.randomUUID();
        CloudFileStatus fileStatus = CoreCloudFileStatus.builder()
                .id(UUID.randomUUID()).code(CloudFileStatusCode.DELETED).build();
        DeleteCloudFileInputResponse inputResponse = mock(DeleteCloudFileInputResponse.class);
        given(inputResponse.getCloudFileId()).willReturn(cloudId);
        given(inputResponse.getCloudFileStatus()).willReturn(fileStatus);
        given(deleteCloudFileInteractor.delete(any())).willReturn(inputResponse);
        ArgumentCaptor<DeleteCloudFileInputRequest> inputRequestCaptor = ArgumentCaptor.forClass(
                DeleteCloudFileInputRequest.class);


        MvcResult result = mvc.perform(delete("/file").queryParam("filename", testFileName))
                .andReturn();


        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        verify(deleteCloudFileInteractor, times(1)).delete(inputRequestCaptor.capture());
        DeleteCloudFileInputRequest inputRequest = inputRequestCaptor.getValue();
        assertEquals(testFileName, inputRequest.getFileName());
        assertNotNull(inputRequest.getUser());
        assertNotNull(inputRequest.getTraceId());
        String outJson = result.getResponse().getContentAsString();
        assertJson(outJson).containsKeysExactlyInAnyOrder("id", "status");
        assertJson(outJson).at("/id").isEqualTo(cloudId);
        assertJson(outJson).at("/status").isText(CloudFileStatusCode.DELETED.name());
    }

    @Test
    void givenCreateRequest_whenCreateCloudFile_thenSuccess() throws Exception {
        UUID cloudId = UUID.randomUUID();
        CloudFileStatus fileStatus = CoreCloudFileStatus.builder()
                .id(UUID.randomUUID()).code(CloudFileStatusCode.LOADING).build();
        AppCreateCloudFileInputResponse inputResponse = new AppCreateCloudFileInputResponse(cloudId, fileStatus);
        given(createCloudFileInteractor.create(any())).willReturn(inputResponse);
        ArgumentCaptor<CreateCloudFileInputRequest> inputRequestCaptor = ArgumentCaptor.forClass(
                CreateCloudFileInputRequest.class);
        MockMultipartFile testFile = new MockMultipartFile("file", testFileName, MediaType.TEXT_PLAIN_VALUE,
                "some test content".getBytes());


        MvcResult result = mvc.perform(multipart("/file").file(testFile)
                .param("filename", testFileName)).andReturn();


        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        verify(createCloudFileInteractor, times(1)).create(inputRequestCaptor.capture());
        CreateCloudFileInputRequest inputRequest = inputRequestCaptor.getValue();
        assertEquals(testFileName, inputRequest.getUserFile().getFileName());
        assertTrue(inputRequest.getUserFile().getSize() > 0);
        assertNotNull(inputRequest.getUser());
        assertNotNull(inputRequest.getTraceId());
        String outJson = result.getResponse().getContentAsString();
        assertJson(outJson).containsKeysExactlyInAnyOrder("id", "status");
        assertJson(outJson).at("/id").isEqualTo(cloudId);
        assertJson(outJson).at("/status").isText(CloudFileStatusCode.LOADING.name());
    }

    private CloudFile createTestCloudFile() {
        return CoreCloudFile.builder()
                .id(UUID.randomUUID())
                .fileName(testFileName)
                .user(new TestCloudUser())
                .storageFile(TestStorageFile.builder().size(2L).build())
                .status(CoreCloudFileStatus.builder()
                        .id(UUID.randomUUID())
                        .code(CloudFileStatusCode.READY)
                        .traceId(new CoreTraceId(1, UUID.randomUUID()))
                        .build())
                .build();
    }
}