package uk.gov.hmcts.reform.divorce.caseformatterservice.mapper.divorcetoccdformat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.divorce.caseformatterservice.CaseFormatterServiceApplication;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.DivorceCaseWrapper;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.ccd.CollectionMember;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.ccd.CoreCaseData;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.ccd.DnCaseData;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.ccd.Document;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.ccd.DocumentLink;
import uk.gov.hmcts.reform.divorce.caseformatterservice.domain.model.usersession.DivorceSession;
import uk.gov.hmcts.reform.divorce.caseformatterservice.mapper.DivorceCaseToDnClarificationMapper;
import uk.gov.hmcts.reform.divorce.caseformatterservice.mapper.ObjectMapperTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseFormatterServiceApplication.class)
public class DivorceCaseToDnClarificationMapperUTest {

    @Autowired
    private DivorceCaseToDnClarificationMapper mapper;

    @Test
    public void shouldMapDivorceSessionFieldsToDnCaseData() throws Exception {
        CoreCaseData coreCaseData = new CoreCaseData();

        DnCaseData expectedDnCaseData = ObjectMapperTestUtil
            .retrieveFileContentsAsObject("fixtures/divorcetoccdmapping/ccd/dn-clarification.json", DnCaseData.class);

        DivorceSession divorceSession = ObjectMapperTestUtil
            .retrieveFileContentsAsObject("fixtures/divorcetoccdmapping/divorce/dn-clarification.json",
                DivorceSession.class);

        DivorceCaseWrapper divorceCaseWrapper = new DivorceCaseWrapper(coreCaseData, divorceSession);

        DnCaseData actualDnCaseData = mapper.divorceCaseDataToDnCaseData(divorceCaseWrapper);

        assertThat(actualDnCaseData, samePropertyValuesAs(expectedDnCaseData));
    }

    @Test
    public void shouldMapDivorceSessionFieldsToDnCaseDataWithExistingData() throws Exception {
        Document existingDocument = new Document();
        existingDocument.setDocumentType("other");
        existingDocument.setDocumentDateAdded("2011-11-11");
        existingDocument.setDocumentComment("");
        existingDocument.setDocumentFileName("favicon.ico");
        existingDocument.setDocumentEmailContent("");
        DocumentLink documentLink = new DocumentLink();
        documentLink.setDocumentUrl("http://localhost:5006/documents/1234567-abcd-1234-wxyz-098765432101");
        existingDocument.setDocumentLink(documentLink);

        CollectionMember<Document> collectionMember = new CollectionMember<>();
        collectionMember.setValue(existingDocument);

        List<CollectionMember<Document>> existingDocuments = new ArrayList<>();
        existingDocuments.add(collectionMember);

        CollectionMember<String> clarificationResponse = new CollectionMember<>();
        clarificationResponse.setId("initial-id");
        clarificationResponse.setValue("Clarification 1: This is the initial response");

        CollectionMember<String> uploadAnyOtherDocuments = new CollectionMember<>();
        uploadAnyOtherDocuments.setId("initial-id");
        uploadAnyOtherDocuments.setValue("Clarification 1: No");

        CoreCaseData coreCaseData = new CoreCaseData();
        coreCaseData.setDnClarificationResponse(new ArrayList<>(Arrays.asList(clarificationResponse)));
        coreCaseData.setDnClarificationUploadDocuments(new ArrayList<>(Arrays.asList(uploadAnyOtherDocuments)));
        coreCaseData.setDocumentsUploadedDnClarification(existingDocuments);

        DnCaseData expectedDnCaseData = ObjectMapperTestUtil
            .retrieveFileContentsAsObject("fixtures/divorcetoccdmapping/ccd/dn-clarification-existing-data.json", DnCaseData.class);

        DivorceSession divorceSession = ObjectMapperTestUtil
            .retrieveFileContentsAsObject("fixtures/divorcetoccdmapping/divorce/dn-clarification-existing-data.json",
                DivorceSession.class);

        DivorceCaseWrapper divorceCaseWrapper = new DivorceCaseWrapper(coreCaseData, divorceSession);

        DnCaseData actualDnCaseData = mapper.divorceCaseDataToDnCaseData(divorceCaseWrapper);

        assertThat(actualDnCaseData, samePropertyValuesAs(expectedDnCaseData));
    }

    @Test
    public void shouldNotThrowErrorEventWhenClarificationDataIsNull() throws Exception {
        CoreCaseData coreCaseData = new CoreCaseData();
        DivorceSession divorceSession = new DivorceSession();

        DivorceCaseWrapper divorceCaseWrapper = new DivorceCaseWrapper(coreCaseData, divorceSession);

        DnCaseData expectedDnCaseData = new DnCaseData();

        DnCaseData actualDnCaseData = mapper.divorceCaseDataToDnCaseData(divorceCaseWrapper);

        assertThat(actualDnCaseData, samePropertyValuesAs(expectedDnCaseData));
    }
}
