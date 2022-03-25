package org.fiware.tmforum.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.RequiredArgsConstructor;
import org.awaitility.Awaitility;
import org.fiware.canismajor.api.EntityApiClient;
import org.fiware.canismajor.model.EntityTransactionListVO;
import org.fiware.canismajor.model.EntityTransactionVO;
import org.fiware.ngsi.api.EntitiesApiTestClient;
import org.fiware.party.model.CharacteristicVO;
import org.fiware.party.model.ContactMediumVO;
import org.fiware.party.model.DisabilityVO;
import org.fiware.party.model.ExternalReferenceVO;
import org.fiware.party.model.IndividualCreateVO;
import org.fiware.party.model.IndividualIdentificationVO;
import org.fiware.party.model.IndividualVO;
import org.fiware.party.model.LanguageAbilityVO;
import org.fiware.party.model.MediumCharacteristicVO;
import org.fiware.party.model.OrganizationCreateVO;
import org.fiware.party.model.OrganizationIdentificationVO;
import org.fiware.party.model.OrganizationParentRelationshipVO;
import org.fiware.party.model.OrganizationRefVO;
import org.fiware.party.model.OrganizationStateTypeVO;
import org.fiware.party.model.OrganizationVO;
import org.fiware.party.model.OtherNameIndividualVO;
import org.fiware.party.model.OtherNameOrganizationVO;
import org.fiware.party.model.PartyCreditProfileVO;
import org.fiware.party.model.RelatedPartyVO;
import org.fiware.party.model.SkillVO;
import org.fiware.party.model.TaxDefinitionVO;
import org.fiware.party.model.TaxExemptionCertificateVO;
import org.fiware.party.model.TimePeriodVO;
import org.fiware.product.model.CategoryCreateVO;
import org.fiware.product.model.CategoryRefVO;
import org.fiware.product.model.CategoryVO;
import org.fiware.product.model.CharacteristicValueSpecificationVO;
import org.fiware.product.model.DurationVO;
import org.fiware.product.model.MoneyVO;
import org.fiware.product.model.PricingLogicAlgorithmVO;
import org.fiware.product.model.ProductOfferingCreateVO;
import org.fiware.product.model.ProductOfferingPriceCreateVO;
import org.fiware.product.model.ProductOfferingPriceRefOrValueVO;
import org.fiware.product.model.ProductOfferingPriceRefVO;
import org.fiware.product.model.ProductOfferingPriceVO;
import org.fiware.product.model.ProductOfferingTermVO;
import org.fiware.product.model.ProductOfferingVO;
import org.fiware.product.model.ProductSpecificationCharacteristicVO;
import org.fiware.product.model.ProductSpecificationCreateVO;
import org.fiware.product.model.ProductSpecificationRefVO;
import org.fiware.product.model.ProductSpecificationVO;
import org.fiware.product.model.QuantityVO;
import org.fiware.product.model.TaxItemVO;
import org.fiware.tmforum.domain.party.Quantity;
import org.fiware.tmforum.domain.party.individual.Disability;
import org.fiware.tmforum.domain.party.individual.LanguageAbility;
import org.fiware.tmforum.domain.product.CategoryRef;
import org.fiware.tmforum.domain.product.ProductSpecification;
import org.fiware.tmforum.domain.product.offering.ProductOfferingTerm;
import org.fiware.tmforum.domain.product.offering.ProductSpecificationRef;
import org.fiware.tmforum.domain.product.offering.TargetProductSchema;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.fiware.tmforum.rest.CatalogApiController;
import org.fiware.tmforum.rest.CategoryApiController;
import org.fiware.tmforum.rest.IndividualApiController;
import org.fiware.tmforum.rest.OrganizationApiController;
import org.fiware.tmforum.rest.ProductOfferingApiController;
import org.fiware.tmforum.rest.ProductOfferingPriceApiController;
import org.fiware.tmforum.rest.ProductSpecificationApiController;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PREREQUESITE: Have a docker-setup with the broker running.
 */
@RequiredArgsConstructor
@MicronautTest
public class PoCIT {

	private final ObjectMapper objectMapper;
	private final CatalogApiController catalogApiController;
	private final CategoryApiController categoryApiController;
	private final IndividualApiController individualApiController;
	private final OrganizationApiController organizationApiController;
	private final ProductOfferingApiController productOfferingApiController;
	private final ProductOfferingPriceApiController productOfferingPriceApiController;
	private final ProductSpecificationApiController productSpecificationApiController;

	private final EntityApiClient entityApiClient;

	@Test
	public void test() throws JsonProcessingException, ParseException {

		OrganizationCreateVO myFancyCompanyCreate = getMyFancyCompany();

		HttpResponse<OrganizationVO> myFancyCompanyCreateResponse = organizationApiController.createOrganization(myFancyCompanyCreate);
		assertEquals(HttpStatus.CREATED, myFancyCompanyCreateResponse.getStatus(), "Company should have been created.");
		OrganizationVO myFancyCompany = myFancyCompanyCreateResponse.body();

		IndividualCreateVO earlMustermannCreate = getIndividualEmployee(myFancyCompany.getId());
		HttpResponse<IndividualVO> earlMustermannCreateResponse = individualApiController.createIndividual(earlMustermannCreate);
		assertEquals(HttpStatus.CREATED, earlMustermannCreateResponse.getStatus(), "Individual should have been created.");
		IndividualVO earlMustermann = earlMustermannCreateResponse.body();

		OrganizationCreateVO myOtherCompanyCreate = getFancyChildCompany(myFancyCompany.getId(), earlMustermann.getId());
		HttpResponse<OrganizationVO> myOtherCompanyCreateResponse = organizationApiController.createOrganization(myOtherCompanyCreate);
		assertEquals(HttpStatus.CREATED, myOtherCompanyCreateResponse.getStatus(), "Company should have been created.");
		OrganizationVO myOtherCompany = myOtherCompanyCreateResponse.body();

		ProductOfferingPriceCreateVO productOfferingPriceCreateVO = getProdProductOfferingPrice();
		HttpResponse<ProductOfferingPriceVO> productOfferingPriceVOHttpResponse = productOfferingPriceApiController.createProductOfferingPrice(productOfferingPriceCreateVO);
		assertEquals(HttpStatus.CREATED, productOfferingPriceVOHttpResponse.getStatus(), "POP should have been created.");
		ProductOfferingPriceVO craneAsAServicePrice = productOfferingPriceVOHttpResponse.body();

		ProductSpecificationCreateVO productSpecificationCreateVO = getProductSpecification(myFancyCompany.getId(), myOtherCompany.getId());
		HttpResponse<ProductSpecificationVO> productSpecificationHttpResponse = productSpecificationApiController.createProductSpecification(productSpecificationCreateVO);
		assertEquals(HttpStatus.CREATED, productSpecificationHttpResponse.getStatus(), "Product spec should have been created.");
		ProductSpecificationVO craneAsAServiceSpec = productSpecificationHttpResponse.body();

		CategoryCreateVO categoryCreateVO = getCategory();
		HttpResponse<CategoryVO> categoryVOHttpResponse = categoryApiController.createCategory(categoryCreateVO);
		assertEquals(HttpStatus.CREATED, categoryVOHttpResponse.getStatus(), "Category should have been created.");
		CategoryVO smartServiceCategory = categoryVOHttpResponse.body();

		ProductOfferingCreateVO productOfferingCreateVO = getProductOffering(smartServiceCategory.getId(), craneAsAServicePrice.getId(), craneAsAServiceSpec.getId());
		HttpResponse<ProductOfferingVO> productOfferingVOHttpResponse = productOfferingApiController.createProductOffering(productOfferingCreateVO);
		assertEquals(HttpStatus.CREATED, productOfferingVOHttpResponse.getStatus(), "Product offering should have been created.");
		ProductOfferingVO craneAsAServiceOffering = productOfferingVOHttpResponse.body();

		Awaitility
				.await("Wait for everything to be written to canis-major.")
				.atMost(Duration.of(60, ChronoUnit.SECONDS))
				.until(() -> {
					try {
						HttpResponse<EntityTransactionListVO> transactionListVOHttpResponse = entityApiClient.getEntitiesWithTransactions();
						assertEquals(HttpStatus.OK, transactionListVOHttpResponse.getStatus());
						EntityTransactionListVO entityTransactionListVO = transactionListVOHttpResponse.body();
						List<String> idList = entityTransactionListVO.getRecords().stream().map(EntityTransactionVO::entityId).map(URI::toString).toList();
						assertTrue(idList.contains(myFancyCompany.getId()));
						assertTrue(idList.contains(earlMustermann.getId()));
						assertTrue(idList.contains(myOtherCompany.getId()));
						assertTrue(idList.contains(craneAsAServicePrice.getId()));
						assertTrue(idList.contains(craneAsAServiceSpec.getId()));
						assertTrue(idList.contains(smartServiceCategory.getId()));
						assertTrue(idList.contains(craneAsAServiceOffering.getId()));
					} catch (Throwable t) {
						return false;
					}
					return true;
				});
	}

	private ProductOfferingCreateVO getProductOffering(String categoryId, String priceId, String specId) {
		ProductOfferingCreateVO productOfferingCreateVO = new ProductOfferingCreateVO();
		productOfferingCreateVO.setDescription("My crane offering");
		productOfferingCreateVO.setIsBundle(false);
		productOfferingCreateVO.setIsSellable(true);
		productOfferingCreateVO.setLastUpdate(Instant.now());
		productOfferingCreateVO.setLifecycleStatus("in-store");
		productOfferingCreateVO.setName("CaaS");
		productOfferingCreateVO.setStatusReason("Its there.");
		productOfferingCreateVO.setVersion("0.0.1");

		CategoryRefVO categoryRefVO = new CategoryRefVO();
		categoryRefVO.setId(categoryId);
		categoryRefVO.setName("Smartservice Cat");
		categoryRefVO.setVersion("0.0.1");
		productOfferingCreateVO.setCategory(List.of(categoryRefVO));

		ProductOfferingPriceRefOrValueVO productOfferingPriceRefVO = new ProductOfferingPriceRefOrValueVO();
		productOfferingPriceRefVO.setId(priceId);

		productOfferingCreateVO.setProductOfferingPrice(List.of(productOfferingPriceRefVO));

		ProductOfferingTermVO productOfferingTermVO = new ProductOfferingTermVO();
		productOfferingTermVO.setDuration(new DurationVO().units("Days").amount(12));
		productOfferingTermVO.setName("Availability");

		productOfferingCreateVO.setProductOfferingTerm(List.of(productOfferingTermVO));

		ProductSpecificationRefVO productSpecificationRefVO = new ProductSpecificationRefVO();
		productSpecificationRefVO.setId(specId);
		productSpecificationRefVO.setName("The spec.");
		productSpecificationRefVO.setVersion("0.0.1");

		productOfferingCreateVO.setProductSpecification(productSpecificationRefVO);
		return productOfferingCreateVO;
	}

	private ProductSpecificationCreateVO getProductSpecification(String orgId, String org2Id) {
		ProductSpecificationCreateVO productSpecificationCreateVO = new ProductSpecificationCreateVO();
		productSpecificationCreateVO.setBrand("FIWARE");
		productSpecificationCreateVO.setDescription("Crane as a service solution.");
		productSpecificationCreateVO.setIsBundle(false);
		productSpecificationCreateVO.setLastUpdate(Instant.now());
		productSpecificationCreateVO.setLifecycleStatus("in-store");
		productSpecificationCreateVO.setName("Crane-as-a-Service");
		productSpecificationCreateVO.setProductNumber("0123456789");
		productSpecificationCreateVO.setVersion("0.0.1");

		CharacteristicValueSpecificationVO characteristicValueSpecificationVO = new CharacteristicValueSpecificationVO();
		characteristicValueSpecificationVO.setIsDefault(true);
		characteristicValueSpecificationVO.setRangeInterval("0-10000000");
		characteristicValueSpecificationVO.setUnitOfMeasure("kg/m");
		characteristicValueSpecificationVO.setValueFrom(0);
		characteristicValueSpecificationVO.setValueTo(10000000);
		characteristicValueSpecificationVO.setValueType("kg/m");
		characteristicValueSpecificationVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		ProductSpecificationCharacteristicVO productSpecificationCharacteristicVO = new ProductSpecificationCharacteristicVO();
		productSpecificationCharacteristicVO.setConfigurable(false);
		productSpecificationCharacteristicVO.setDescription("My spec");
		productSpecificationCharacteristicVO.setExtensible(false);
		productSpecificationCharacteristicVO.setIsUnique(true);
		productSpecificationCharacteristicVO.setMaxCardinality(1);
		productSpecificationCharacteristicVO.setMinCardinality(1);
		productSpecificationCharacteristicVO.setName("The crane.");
		productSpecificationCharacteristicVO.setProductSpecCharacteristicValue(List.of(characteristicValueSpecificationVO));
		productSpecificationCharacteristicVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		productSpecificationCreateVO.setProductSpecCharacteristic(List.of(productSpecificationCharacteristicVO));

		org.fiware.product.model.RelatedPartyVO relatedPartyVO = new org.fiware.product.model.RelatedPartyVO();
		relatedPartyVO.setId(orgId);
		relatedPartyVO.setRole("Vendor");

		org.fiware.product.model.RelatedPartyVO relatedPartyVO1 = new org.fiware.product.model.RelatedPartyVO();
		relatedPartyVO1.setId(org2Id);
		relatedPartyVO1.setRole("Sub-Vendor");
		productSpecificationCreateVO.setRelatedParty(List.of(relatedPartyVO, relatedPartyVO1));

		productSpecificationCreateVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		return productSpecificationCreateVO;
	}

	private ProductOfferingPriceCreateVO getProdProductOfferingPrice() {
		ProductOfferingPriceCreateVO productOfferingPriceVO = new ProductOfferingPriceCreateVO();
		productOfferingPriceVO.setDescription("Price of the service");
		productOfferingPriceVO.setIsBundle(false);
		productOfferingPriceVO.setLastUpdate(Instant.now());
		productOfferingPriceVO.setLifecycleStatus("in-store");
		productOfferingPriceVO.setName("Lifting charge");
		productOfferingPriceVO.setPriceType("standard");
		productOfferingPriceVO.setVersion("0.0.1");
		productOfferingPriceVO.setPrice(new MoneyVO().unit("Euro").value(10.12f));

		PricingLogicAlgorithmVO pricingLogicAlgorithmVO = new PricingLogicAlgorithmVO();
		pricingLogicAlgorithmVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));
		pricingLogicAlgorithmVO.setName("lifting based price");

		productOfferingPriceVO.setPricingLogicAlgorithm(List.of(pricingLogicAlgorithmVO));

		ProductOfferingTermVO productOfferingTermVO = new ProductOfferingTermVO();
		productOfferingTermVO.setDescription("Available on sundays");
		productOfferingTermVO.setName("Availability");
		productOfferingTermVO.setDuration(new DurationVO().amount(1).units("Days"));

		ProductOfferingTermVO productOfferingTermVO1 = new ProductOfferingTermVO();
		productOfferingTermVO1.setDescription("Available on next month");
		productOfferingTermVO1.setName("Availability");
		productOfferingTermVO1.setDuration(new DurationVO().amount(10).units("Weeks"));

		productOfferingPriceVO.setProductOfferingTerm(List.of(productOfferingTermVO1, productOfferingTermVO));

		TaxItemVO taxItemVO = new TaxItemVO();
		taxItemVO.setTaxCategory("Mehrwertsteuer");
		taxItemVO.setTaxRate(0.17f);

		TaxItemVO taxItemVO1 = new TaxItemVO();
		taxItemVO1.setTaxCategory("Umsatzsteuer");
		taxItemVO.setTaxAmount(new MoneyVO().setUnit("Euro").setValue(10.50f));

		productOfferingPriceVO.setTax(List.of(taxItemVO1, taxItemVO));

		QuantityVO quantityVO = new QuantityVO();
		quantityVO.setAmount(1000f);
		quantityVO.setUnits("kg/m");
		productOfferingPriceVO.setUnitOfMeasure(quantityVO);

		productOfferingPriceVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(300, ChronoUnit.DAYS))));
		return productOfferingPriceVO;
	}

	private CategoryCreateVO getCategory() {
		CategoryCreateVO categoryCreateVO = new CategoryCreateVO();
		categoryCreateVO.setDescription("Smart services");
		categoryCreateVO.isRoot(true);
		categoryCreateVO.setLastUpdate(Instant.now());
		categoryCreateVO.setLifecycleStatus("available");
		categoryCreateVO.setName("Smart-Services");
		categoryCreateVO.setVersion("0.0.1");
		categoryCreateVO.setValidFor(new org.fiware.product.model.TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		return categoryCreateVO;
	}

	private OrganizationCreateVO getFancyChildCompany(String parentId, String employeeId) throws JsonProcessingException {
		OrganizationCreateVO companyVO = getMyFancyCompany();

		OrganizationRefVO organizationRefVO = new OrganizationRefVO();
		organizationRefVO.setId(parentId);
		OrganizationParentRelationshipVO organizationParentRelationshipVO = new OrganizationParentRelationshipVO();
		organizationParentRelationshipVO.setOrganization(organizationRefVO);
		organizationParentRelationshipVO.setRelationshipType("Parent");

		companyVO.setOrganizationParentRelationship(organizationParentRelationshipVO);

		RelatedPartyVO relatedPartyVO = new RelatedPartyVO();
		relatedPartyVO.setId(employeeId);
		relatedPartyVO.setRole("CEO");
		relatedPartyVO.setName("Employee");

		RelatedPartyVO relatedPartyVO1 = new RelatedPartyVO();
		relatedPartyVO1.setId(parentId);
		relatedPartyVO1.setName("Parent Company");
		relatedPartyVO1.setRole("Parent");

		companyVO.setRelatedParty(List.of(relatedPartyVO, relatedPartyVO1));
		return companyVO;
	}

	private OrganizationCreateVO getMyFancyCompany() throws JsonProcessingException {
		// create the organization
		OrganizationCreateVO organizationVO = new OrganizationCreateVO();
		organizationVO.setIsHeadOffice(true);
		organizationVO.setIsLegalEntity(true);
		organizationVO.setName("My Fancy Company");
		organizationVO.setNameType("Inc");
		organizationVO.setOrganizationType("Company");
		organizationVO.setTradingName("My Fancy Company");

		MediumCharacteristicVO mediumCharacteristicVO = new MediumCharacteristicVO();
		mediumCharacteristicVO.setCity("Dresden");
		mediumCharacteristicVO.setContactType("postal address");
		mediumCharacteristicVO.setCountry("Germany");
		mediumCharacteristicVO.setEmailAddress("my-fancy@company.org");
		mediumCharacteristicVO.setPhoneNumber("0123/4567890-0");
		mediumCharacteristicVO.setFaxNumber("0123/4567890-1");
		mediumCharacteristicVO.setPostCode("01189");
		mediumCharacteristicVO.setSocialNetworkId("@fancy");
		mediumCharacteristicVO.setStateOrProvince("Saxony");
		mediumCharacteristicVO.street1("Prager Straße 1");

		PartyCreditProfileVO partyCreditProfileVO = new PartyCreditProfileVO();
		partyCreditProfileVO.setCreditAgencyName("Experian");
		partyCreditProfileVO.setCreditAgencyType("Rating-Agency");
		partyCreditProfileVO.setRatingReference("Rating ref");
		partyCreditProfileVO.setRatingScore(100);
		partyCreditProfileVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		PartyCreditProfileVO partyCreditProfileVO2 = new PartyCreditProfileVO();
		partyCreditProfileVO2.setCreditAgencyName("TransUnion");
		partyCreditProfileVO2.setCreditAgencyType("Rating-Agency");
		partyCreditProfileVO2.setRatingReference("Rating ref");
		partyCreditProfileVO2.setRatingScore(100);
		partyCreditProfileVO2.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		ContactMediumVO contactMediumVO = new ContactMediumVO();
		contactMediumVO.setMediumType("postal address");
		contactMediumVO.setPreferred(true);
		contactMediumVO.setCharacteristic(mediumCharacteristicVO);
		contactMediumVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		OrganizationIdentificationVO organizationIdentificationVO = new OrganizationIdentificationVO();
		organizationIdentificationVO.setIdentificationId("My-Fancy-Company-ID");
		organizationIdentificationVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(20, ChronoUnit.DAYS))));
		organizationIdentificationVO.setIdentificationType("Country-ID");
		organizationIdentificationVO.setIssuingAuthority("Gewerbeamt Dresden");
		organizationIdentificationVO.setIssuingDate(Instant.now().minus(Duration.of(1, ChronoUnit.DAYS)));

		OtherNameOrganizationVO otherOrganizationName = new OtherNameOrganizationVO();
		otherOrganizationName.setName("My-Other-Company");
		otherOrganizationName.setNameType("Ldt.");
		otherOrganizationName.setTradingName("My-Other-Company");
		otherOrganizationName.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(20, ChronoUnit.DAYS))));

		CharacteristicVO characteristicVO = new CharacteristicVO();
		characteristicVO.setName("My-Company-Valuation");
		characteristicVO.setValueType("valuation");
		characteristicVO.setValue(objectMapper.writeValueAsString(new TestCharacteristic(1000000l, "Euro")));

		TaxDefinitionVO taxDefinitionVO = new TaxDefinitionVO();
		taxDefinitionVO.setName("Gewerbe-Steuer");
		taxDefinitionVO.setTaxType("Gewerbe-Steuer");

		TaxExemptionCertificateVO taxExemptionCertificateVO = new TaxExemptionCertificateVO();
		taxExemptionCertificateVO.setTaxDefinition(List.of(taxDefinitionVO));
		taxExemptionCertificateVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(20, ChronoUnit.DAYS))));

		organizationVO.setContactMedium(List.of(contactMediumVO));
		organizationVO.setCreditRating(List.of(partyCreditProfileVO, partyCreditProfileVO2));
		organizationVO.setExistsDuring(new TimePeriodVO().startDateTime(Instant.now().minus(Duration.of(100, ChronoUnit.DAYS))).endDateTime(Instant.now().plus(Duration.of(100, ChronoUnit.DAYS))));
		organizationVO.setExternalReference(List.of(new ExternalReferenceVO().name("Ext-Ref").externalReferenceType("Ref")));
		organizationVO.setOrganizationIdentification(List.of(organizationIdentificationVO));
		organizationVO.setOtherName(List.of(otherOrganizationName));
		organizationVO.setPartyCharacteristic(List.of(characteristicVO));
		organizationVO.setStatus(OrganizationStateTypeVO.VALIDATED);
		organizationVO.setTaxExemptionCertificate(List.of(taxExemptionCertificateVO));

		return organizationVO;
	}

	private IndividualCreateVO getIndividualEmployee(String orgId) throws ParseException {
		IndividualCreateVO individualCreateVO = new IndividualCreateVO();
		individualCreateVO.setAristocraticTitle("Earl");
		individualCreateVO.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01").toInstant());
		individualCreateVO.setCountryOfBirth("Germany");
		individualCreateVO.setFamilyName("Mustermann");
		individualCreateVO.setFormattedName("Max Markus Mustermann, Earl of Saxony");
		individualCreateVO.setFullName("Max Markus Mustermann");
		individualCreateVO.setGender("male");
		individualCreateVO.setGeneration("III.");
		individualCreateVO.setGivenName("Max");
		individualCreateVO.setLegalName("Mustermann");
		individualCreateVO.setLocation("Schloßstraße 1, Dresden");
		individualCreateVO.setMaritalStatus("married");
		individualCreateVO.setMiddleName("Markus");
		individualCreateVO.setNationality("German");
		individualCreateVO.setPlaceOfBirth("Dresden");
		individualCreateVO.setPreferredGivenName("The earl");
		individualCreateVO.setTitle("Dr.");

		MediumCharacteristicVO mediumCharacteristicVO = new MediumCharacteristicVO();
		mediumCharacteristicVO.setCity("Dresden");
		mediumCharacteristicVO.setContactType("postal address");
		mediumCharacteristicVO.setCountry("Germany");
		mediumCharacteristicVO.setEmailAddress("the-earl@company.org");
		mediumCharacteristicVO.setPhoneNumber("0123/4567890-2");
		mediumCharacteristicVO.setFaxNumber("0123/4567890-3");
		mediumCharacteristicVO.setPostCode("01189");
		mediumCharacteristicVO.setSocialNetworkId("@earl");
		mediumCharacteristicVO.setStateOrProvince("Saxony");
		mediumCharacteristicVO.street1("Schlossstraße 1");


		ContactMediumVO contactMediumVO = new ContactMediumVO();
		contactMediumVO.setMediumType("postal address");
		contactMediumVO.setPreferred(true);
		contactMediumVO.setCharacteristic(mediumCharacteristicVO);
		contactMediumVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		ContactMediumVO contactMediumVO2 = new ContactMediumVO();
		contactMediumVO2.setMediumType("email");
		contactMediumVO2.setPreferred(false);
		contactMediumVO2.setCharacteristic(mediumCharacteristicVO);
		contactMediumVO2.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		individualCreateVO.setContactMedium(List.of(contactMediumVO, contactMediumVO2));

		PartyCreditProfileVO partyCreditProfileVO = new PartyCreditProfileVO();
		partyCreditProfileVO.setCreditAgencyName("Experian");
		partyCreditProfileVO.setCreditAgencyType("Rating-Agency");
		partyCreditProfileVO.setRatingReference("Rating ref");
		partyCreditProfileVO.setRatingScore(100);
		partyCreditProfileVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		individualCreateVO.setCreditRating(List.of(partyCreditProfileVO));

		DisabilityVO disabilityVO = new DisabilityVO();
		disabilityVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));
		disabilityVO.setDisabilityCode("02");
		disabilityVO.setDisabilityName("Hearing");

		DisabilityVO disabilityVO2 = new DisabilityVO();
		disabilityVO2.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));
		disabilityVO2.setDisabilityCode("03");
		disabilityVO2.setDisabilityName("Manual Dexterity");

		DisabilityVO disabilityVO3 = new DisabilityVO();
		disabilityVO3.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));
		disabilityVO3.setDisabilityCode("09");
		disabilityVO3.setDisabilityName("Sight");

		individualCreateVO.setDisability(List.of(disabilityVO, disabilityVO2, disabilityVO3));

		ExternalReferenceVO externalReferenceVO = new ExternalReferenceVO();
		externalReferenceVO.setExternalReferenceType("ext");
		externalReferenceVO.setName("My-Ref");

		individualCreateVO.setExternalReference(List.of(externalReferenceVO));

		IndividualIdentificationVO individualIdentificationVO = new IndividualIdentificationVO();
		individualIdentificationVO.setIdentificationId("T22000129");
		individualIdentificationVO.setIdentificationType("Passport");
		individualIdentificationVO.setIssuingAuthority("Einwohnermeldeamt Dresden");
		individualIdentificationVO.setIssuingDate(Instant.now());
		individualIdentificationVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));

		individualCreateVO.setIndividualIdentification(List.of(individualIdentificationVO));

		LanguageAbilityVO languageAbilityVO = new LanguageAbilityVO();
		languageAbilityVO.isFavouriteLanguage(true);
		languageAbilityVO.setLanguageCode("DE");
		languageAbilityVO.setLanguageName("German");
		languageAbilityVO.setListeningProficiency("C2");
		languageAbilityVO.setReadingProficiency("C2");
		languageAbilityVO.setSpeakingProficiency("C2");
		languageAbilityVO.setWritingProficiency("C2");
		languageAbilityVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(100, ChronoUnit.DAYS))));

		individualCreateVO.setLanguageAbility(List.of(languageAbilityVO));

		OtherNameIndividualVO otherNameIndividualVO = new OtherNameIndividualVO();
		otherNameIndividualVO.setFamilyName("Mannmuster");
		otherNameIndividualVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(10, ChronoUnit.DAYS))));
		otherNameIndividualVO.setFormattedName("Max Markus Mustermann, Earl of Saxony");
		otherNameIndividualVO.setFullName("Max Markus Mustermann");
		otherNameIndividualVO.setGeneration("III.");
		otherNameIndividualVO.setGivenName("Max");
		otherNameIndividualVO.setLegalName("Mustermann");
		otherNameIndividualVO.setMiddleName("Markus");
		otherNameIndividualVO.setPreferredGivenName("The earl");
		otherNameIndividualVO.setTitle("Dr.");

		individualCreateVO.setOtherName(List.of(otherNameIndividualVO));

		CharacteristicVO characteristicVO = new CharacteristicVO();
		characteristicVO.setName("Position");
		characteristicVO.setValueType("String");
		characteristicVO.setValue("CEO");

		individualCreateVO.setPartyCharacteristic(List.of(characteristicVO));

		RelatedPartyVO partyVO = new RelatedPartyVO();
		partyVO.setRole("Employer");
		partyVO.setId(orgId);

		individualCreateVO.setRelatedParty(List.of(partyVO));

		SkillVO skillVO = new SkillVO();
		skillVO.setComment("Programming proficiency.");
		skillVO.setEvaluatedLevel("High");
		skillVO.setSkillCode("0123");
		skillVO.setSkillName("Java");
		skillVO.setValidFor(new TimePeriodVO().startDateTime(Instant.now()).endDateTime(Instant.now().plus(Duration.of(100, ChronoUnit.DAYS))));

		individualCreateVO.setSkill(List.of(skillVO));

		return individualCreateVO;
	}

	class TestCharacteristic {
		public final long valuation;
		public final String unit;

		TestCharacteristic(long valuation, String unit) {
			this.valuation = valuation;
			this.unit = unit;
		}
	}

}
