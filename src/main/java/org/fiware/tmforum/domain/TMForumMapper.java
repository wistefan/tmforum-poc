package org.fiware.tmforum.domain;

import org.fiware.party.model.AttachmentVO;
import org.fiware.party.model.CharacteristicVO;
import org.fiware.party.model.ContactMediumVO;
import org.fiware.party.model.DisabilityVO;
import org.fiware.party.model.ExternalReferenceVO;
import org.fiware.party.model.IndividualCreateVO;
import org.fiware.party.model.IndividualIdentificationVO;
import org.fiware.party.model.IndividualStateTypeVO;
import org.fiware.party.model.IndividualVO;
import org.fiware.party.model.LanguageAbilityVO;
import org.fiware.party.model.MediumCharacteristicVO;
import org.fiware.party.model.OrganizationChildRelationshipVO;
import org.fiware.party.model.OrganizationCreateVO;
import org.fiware.party.model.OrganizationParentRelationshipVO;
import org.fiware.party.model.OrganizationRefVO;
import org.fiware.party.model.OrganizationVO;
import org.fiware.party.model.OtherNameIndividualVO;
import org.fiware.party.model.OtherNameOrganizationVO;
import org.fiware.party.model.PartyCreditProfileVO;
import org.fiware.party.model.QuantityVO;
import org.fiware.party.model.RelatedPartyVO;
import org.fiware.party.model.SkillVO;
import org.fiware.party.model.TaxDefinitionVO;
import org.fiware.party.model.TaxExemptionCertificateVO;
import org.fiware.party.model.TimePeriodVO;
import org.fiware.product.model.BundledProductOfferingOptionVO;
import org.fiware.product.model.BundledProductOfferingPriceRelationshipVO;
import org.fiware.product.model.BundledProductOfferingVO;
import org.fiware.product.model.CatalogCreateVO;
import org.fiware.product.model.CatalogVO;
import org.fiware.product.model.CategoryCreateVO;
import org.fiware.product.model.CategoryRefVO;
import org.fiware.product.model.CategoryVO;
import org.fiware.product.model.CharacteristicValueSpecificationVO;
import org.fiware.product.model.ConstraintRefVO;
import org.fiware.product.model.DurationVO;
import org.fiware.product.model.MarketSegmentRefVO;
import org.fiware.product.model.MoneyVO;
import org.fiware.product.model.PlaceRefVO;
import org.fiware.product.model.PricingLogicAlgorithmVO;
import org.fiware.product.model.ProductOfferingCreateVO;
import org.fiware.product.model.ProductOfferingPriceCreateVO;
import org.fiware.product.model.ProductOfferingPriceRefOrValueVO;
import org.fiware.product.model.ProductOfferingPriceRefVO;
import org.fiware.product.model.ProductOfferingPriceVO;
import org.fiware.product.model.ProductOfferingRefVO;
import org.fiware.product.model.ProductOfferingRelationshipVO;
import org.fiware.product.model.ProductOfferingTermVO;
import org.fiware.product.model.ProductOfferingVO;
import org.fiware.product.model.ProductSpecificationCharacteristicRelationshipVO;
import org.fiware.product.model.ProductSpecificationCharacteristicVO;
import org.fiware.product.model.ProductSpecificationCharacteristicValueUseVO;
import org.fiware.product.model.ProductSpecificationCreateVO;
import org.fiware.product.model.ProductSpecificationRefVO;
import org.fiware.product.model.ProductSpecificationVO;
import org.fiware.product.model.ResourceCandidateRefVO;
import org.fiware.product.model.SLARefVO;
import org.fiware.product.model.ServiceCandidateRefVO;
import org.fiware.product.model.TargetProductSchemaVO;
import org.fiware.product.model.TaxItemVO;
import org.fiware.tmforum.domain.party.Attachment;
import org.fiware.tmforum.domain.party.Characteristic;
import org.fiware.tmforum.domain.party.ContactMedium;
import org.fiware.tmforum.domain.party.ExternalReference;
import org.fiware.tmforum.domain.party.MediumCharacteristic;
import org.fiware.tmforum.domain.party.individual.Disability;
import org.fiware.tmforum.domain.party.individual.Individual;
import org.fiware.tmforum.domain.party.individual.IndividualIdentification;
import org.fiware.tmforum.domain.party.individual.IndividualState;
import org.fiware.tmforum.domain.party.individual.LanguageAbility;
import org.fiware.tmforum.domain.party.individual.OtherIndividualName;
import org.fiware.tmforum.domain.party.individual.Skill;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.domain.party.organization.OrganizationChildRelationship;
import org.fiware.tmforum.domain.party.organization.OrganizationParentRelationship;
import org.fiware.tmforum.domain.party.organization.OtherOrganizationName;
import org.fiware.tmforum.domain.party.PartyCreditProfile;
import org.fiware.tmforum.domain.party.Quantity;
import org.fiware.tmforum.domain.party.RelatedParty;
import org.fiware.tmforum.domain.party.TaxDefinition;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.TimePeriod;
import org.fiware.tmforum.domain.product.Catalog;
import org.fiware.tmforum.domain.product.CategoryRef;
import org.fiware.tmforum.domain.product.CharacteristicValueSpecification;
import org.fiware.tmforum.domain.product.ProductSpecification;
import org.fiware.tmforum.domain.product.ProductSpecificationCharacteristic;
import org.fiware.tmforum.domain.product.ProductSpecificationCharacteristicRelationship;
import org.fiware.tmforum.domain.product.offering.BundleProductOffering;
import org.fiware.tmforum.domain.product.offering.BundleProductOfferingOption;
import org.fiware.tmforum.domain.product.offering.BundledProductOfferingPriceRelationship;
import org.fiware.tmforum.domain.product.offering.ConstraintRef;
import org.fiware.tmforum.domain.product.offering.Duration;
import org.fiware.tmforum.domain.product.offering.MarketSegmentRef;
import org.fiware.tmforum.domain.product.offering.Money;
import org.fiware.tmforum.domain.product.offering.PlaceRef;
import org.fiware.tmforum.domain.product.offering.PricingLogicAlgorithm;
import org.fiware.tmforum.domain.product.offering.ProductOffering;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPriceRef;
import org.fiware.tmforum.domain.product.offering.ProductOfferingRef;
import org.fiware.tmforum.domain.product.Category;
import org.fiware.tmforum.domain.product.offering.ProductOfferingRelationship;
import org.fiware.tmforum.domain.product.offering.ProductOfferingTerm;
import org.fiware.tmforum.domain.product.offering.ProductSpecificationCharacteristicValueUse;
import org.fiware.tmforum.domain.product.offering.ProductSpecificationRef;
import org.fiware.tmforum.domain.product.offering.ResourceCandidateRef;
import org.fiware.tmforum.domain.product.offering.SLARef;
import org.fiware.tmforum.domain.product.offering.ServiceCandidateRef;
import org.fiware.tmforum.domain.product.offering.TargetProductSchema;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Mapper(componentModel = "jsr330")
public interface TMForumMapper {

	String ID_TEMPLATE = "urn:ngsi-ld:%s:%s";

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"organization\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	OrganizationVO map(OrganizationCreateVO organizationCreateVO);

	@Mappings({
			@Mapping(target = "isHeadOffice", source = "headOffice"),
			@Mapping(target = "isLegalEntity", source = "legalEntity"),
			@Mapping(target = "status", source = "organizationState")
	})
	OrganizationVO map(Organization organization);

	@Mappings({
			@Mapping(target = "organizationState", source = "status")
	})
	Organization map(OrganizationVO organizationVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"individual\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	IndividualVO map(IndividualCreateVO individualCreateVO);

	@Mappings({
			@Mapping(target = "status", source = "individualState")
	})
	IndividualVO map(Individual individual);

	@Mappings({
			@Mapping(target = "individualState", source = "status")
	})
	Individual map(IndividualVO individualVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"catalog\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	CatalogVO map(CatalogCreateVO catalogCreateVO);

	CatalogVO map(Catalog catalog);

	Catalog map(CatalogVO catalogVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"category\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	CategoryVO map(CategoryCreateVO categoryCreateVO);

	@Mappings({
			@Mapping(target = "isRoot", source = "root"),
			@Mapping(target = "parentId", source = "parentId", qualifiedByName = "toIdString")
	})
	CategoryVO map(Category category);

	@Mappings({
			@Mapping(target = "parentId", source = "parentId", qualifiedByName = "toCategoryRef")
	})
	Category map(CategoryVO categoryVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"product-offering\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	ProductOfferingVO map(ProductOfferingCreateVO productOfferingCreateVO);

	@Mappings({
			@Mapping(target = "isBundle", source = "bundle"),
			@Mapping(target = "isSellable", source = "sellable")
	})
	ProductOfferingVO map(ProductOffering productOffering);

	ProductOffering map(ProductOfferingVO productOfferingVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"product-offering-price\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	ProductOfferingPriceVO map(ProductOfferingPriceCreateVO productOfferingPriceCreateVO);

	@Mappings({
			@Mapping(target = "isBundle", source = "bundle")
	})
	ProductOfferingPriceVO map(ProductOfferingPrice productOfferingPrice);

	@Mappings({
			@Mapping(target = "bundle", source = "isBundle")
	})
	ProductOfferingPrice map(ProductOfferingPriceVO productOfferingPriceVO);

	@Mappings({
			// using inline expression, since else it might overwrite the String-String mapping
			@Mapping(target = "id", expression = "java(java.lang.String.format(ID_TEMPLATE, \"product-specification\", java.util.UUID.randomUUID()))"),
			@Mapping(target = "href", ignore = true)
	})
	ProductSpecificationVO map(ProductSpecificationCreateVO productSpecificationCreateVO);

	@Mappings({
			@Mapping(target = "isBundle", source = "bundle")
	})
	ProductSpecificationVO map(ProductSpecification productSpecification);

	@Mappings({
			@Mapping(target = "bundle", source = "isBundle")
	})
	ProductSpecification map(ProductSpecificationVO productSpecificationVO);


	CategoryRef map(CategoryRefVO categoryRefVO);

	CategoryRefVO map(CategoryRef categoryRef);

	ProductOfferingRef map(ProductOfferingRefVO productOfferingRefVO);

	ProductOfferingRefVO map(ProductOfferingRef productOfferingRef);

	RelatedParty map(RelatedPartyVO relatedPartyVO);

	RelatedPartyVO map(RelatedParty relatedParty);

	@Mappings({
			@Mapping(source = "characteristic", target = "mediumCharacteristic")
	})
	ContactMedium map(ContactMediumVO contactMediumVO);

	@Mappings({
			@Mapping(target = "characteristic", source = "mediumCharacteristic"),
			@Mapping(target = "validFor", source = "validFor")
	})
	ContactMediumVO map(ContactMedium contactMedium);

	CharacteristicVO map(Characteristic characteristic);

	Characteristic map(CharacteristicVO characteristicVO);

	Attachment map(AttachmentVO attachmentVO);

	AttachmentVO map(Attachment attachment);

	ExternalReference map(ExternalReferenceVO externalReferenceVO);

	ExternalReferenceVO map(ExternalReference externalReference);

	MediumCharacteristic map(MediumCharacteristicVO mediumCharacteristicVO);

	MediumCharacteristicVO map(MediumCharacteristic mediumCharacteristic);

	OtherOrganizationName map(OtherNameOrganizationVO otherNameOrganizationVO);

	OtherNameOrganizationVO map(OtherOrganizationName otherOrganizationName);

	PartyCreditProfile map(PartyCreditProfileVO value);

	PartyCreditProfileVO map(PartyCreditProfile partyCreditProfile);

	Quantity map(QuantityVO quantityVO);

	QuantityVO map(Quantity quantity);

	TaxDefinition map(TaxDefinitionVO taxDefinitionVO);

	TaxDefinitionVO map(TaxDefinition taxDefinition);

	TaxExemptionCertificate map(TaxExemptionCertificateVO taxExemptionCertificateVO);

	TaxExemptionCertificateVO map(TaxExemptionCertificate taxExemptionCertificate);

	TimePeriodVO map(TimePeriod timePeriod);

	Disability map(DisabilityVO disabilityVO);

	DisabilityVO map(Disability disability);

	IndividualIdentification map(IndividualIdentificationVO individualIdentificationVO);

	IndividualIdentificationVO map(IndividualIdentification individualIdentification);

	LanguageAbility map(LanguageAbilityVO languageAbilityVO);

	@Mappings({
			@Mapping(target = "isFavouriteLanguage", source = "favouriteLanguage")
	})
	LanguageAbilityVO map(LanguageAbility languageAbility);

	OtherIndividualName map(OtherNameIndividualVO otherNameIndividualVO);

	OtherNameIndividualVO map(OtherIndividualName otherIndividualName);

	Skill map(SkillVO skillVO);

	SkillVO map(Skill skill);

	IndividualState map(IndividualStateTypeVO individualStateTypeVO);

	IndividualStateTypeVO map(IndividualState individualState);

	BundleProductOffering map(BundledProductOfferingVO bundledProductOfferingVO);

	BundledProductOfferingVO map(BundleProductOffering bundleProductOffering);

	BundleProductOfferingOption map(BundledProductOfferingOptionVO bundledProductOfferingOptionVO);

	BundledProductOfferingOptionVO map(BundleProductOfferingOption bundleProductOfferingOption);

	Duration map(DurationVO durationVO);

	DurationVO map(Duration duration);

	MarketSegmentRef map(MarketSegmentRefVO marketSegmentRefVO);

	MarketSegmentRefVO map(MarketSegmentRef marketSegmentRef);

	PlaceRef map(PlaceRefVO placeRefVO);

	PlaceRefVO map(PlaceRef placeRef);

	ProductOfferingRelationship map(ProductOfferingRelationshipVO productOfferingRelationshipVO);

	ProductOfferingRelationshipVO map(ProductOfferingRelationship productOfferingRelationship);

	ProductOfferingTerm map(ProductOfferingTermVO productOfferingTermVO);

	ProductOfferingTermVO map(ProductOfferingTerm productOfferingTerm);

	ProductSpecificationCharacteristicValueUse map(ProductSpecificationCharacteristicValueUseVO productSpecificationCharacteristicValueUseVO);

	ProductSpecificationCharacteristicValueUseVO map(ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse);

	@Mappings({
			@Mapping(target = "default", source = "isDefault")
	})
	CharacteristicValueSpecification map(CharacteristicValueSpecificationVO characteristicValueSpecificationVO);

	@Mappings({
			@Mapping(target = "isDefault", source = "default")
	})
	CharacteristicValueSpecificationVO map(CharacteristicValueSpecification characteristicValueSpecification);

	ProductSpecificationRef map(ProductSpecificationRefVO productSpecificationRefVO);

	ProductSpecificationRefVO map(ProductSpecificationRef productSpecificationRef);

	ResourceCandidateRef map(ResourceCandidateRefVO resourceCandidateRefVO);

	ResourceCandidateRefVO map(ResourceCandidateRef resourceCandidateRef);

	ServiceCandidateRef map(ServiceCandidateRefVO serviceCandidateRefVO);

	ServiceCandidateRefVO map(ServiceCandidateRef serviceCandidateRef);

	SLARef map(SLARefVO slaRefVO);

	SLARefVO map(SLARef slaRef);

	BundledProductOfferingPriceRelationship map(BundledProductOfferingPriceRelationshipVO bundledProductOfferingPriceRelationshipVO);

	BundledProductOfferingPriceRelationshipVO map(BundledProductOfferingPriceRelationship bundledProductOfferingPriceRelationship);

	ConstraintRef map(ConstraintRefVO constraintRefVO);

	ConstraintRefVO map(ConstraintRef constraintRef);

	Money map(MoneyVO moneyVO);

	MoneyVO map(Money money);

	PricingLogicAlgorithm map(PricingLogicAlgorithmVO pricingLogicAlgorithmVO);

	PricingLogicAlgorithmVO map(PricingLogicAlgorithm pricingLogicAlgorithm);

	TargetProductSchema map(TargetProductSchemaVO targetProductSchemaVO);

	TargetProductSchemaVO map(TargetProductSchema targetProductSchema);

	TaxItem map(TaxItemVO taxItemVO);

	TaxItemVO map(TaxItem taxItem);

	ProductSpecificationCharacteristic map(ProductSpecificationCharacteristicVO productSpecificationCharacteristicVO);

	@Mappings({
			@Mapping(target = "isUnique", source = "unique")
	})
	ProductSpecificationCharacteristicVO map(ProductSpecificationCharacteristic productSpecificationCharacteristic);

	ProductSpecificationCharacteristicRelationship map(ProductSpecificationCharacteristicRelationshipVO productSpecificationCharacteristicRelationshipVO);

	ProductSpecificationCharacteristicRelationshipVO map(ProductSpecificationCharacteristicRelationship productSpecificationCharacteristicRelationship);

	@Named("toCategoryRef")
	default CategoryRef toCategoryRef(String categoryId) {
		if(categoryId == null) {
			return null;
		}
		return new CategoryRef(categoryId);
	}

	@Named("toIdString")
	default String toIdString(CategoryRef categoryRef) {
		if(categoryRef == null) {
			return null;
		}
		return categoryRef.getId().toString();
	}

	default ProductOfferingPriceRefOrValueVO map(ProductOfferingPriceRef productOfferingPriceRef) {
		if (productOfferingPriceRef == null) {
			return null;
		}
		ProductOfferingPriceRefOrValueVO productOfferingPriceRefOrValueVO = new ProductOfferingPriceRefOrValueVO();
		productOfferingPriceRefOrValueVO.setId(productOfferingPriceRef.getId().toString());
		productOfferingPriceRefOrValueVO.setAtBaseType(productOfferingPriceRef.getAtBaseType());
		productOfferingPriceRefOrValueVO.setAtType(productOfferingPriceRef.getAtType());
		productOfferingPriceRefOrValueVO.setAtSchemaLocation(productOfferingPriceRef.getAtSchemaLocation());
		productOfferingPriceRefOrValueVO.setAtReferredType(productOfferingPriceRef.getAtReferredType());
		productOfferingPriceRefOrValueVO.setVersion(productOfferingPriceRef.getVersion());
		productOfferingPriceRefOrValueVO.setHref(productOfferingPriceRef.getHref());
		productOfferingPriceRefOrValueVO.setName(productOfferingPriceRef.getName());
		return productOfferingPriceRefOrValueVO;
	}

	default ProductOfferingPriceRef map(ProductOfferingPriceRefOrValueVO productOfferingPriceRefOrValueVO) {
		if (productOfferingPriceRefOrValueVO == null) {
			return null;
		}
		ProductOfferingPriceRef productOfferingPriceRef = new ProductOfferingPriceRef(productOfferingPriceRefOrValueVO.id());
		productOfferingPriceRef.setAtBaseType(productOfferingPriceRefOrValueVO.getAtBaseType());
		productOfferingPriceRef.setAtType(productOfferingPriceRefOrValueVO.getAtType());
		productOfferingPriceRef.setAtSchemaLocation(productOfferingPriceRefOrValueVO.getAtSchemaLocation());
		productOfferingPriceRef.setAtReferredType(productOfferingPriceRefOrValueVO.getAtReferredType());
		productOfferingPriceRef.setVersion(productOfferingPriceRefOrValueVO.getVersion());
		productOfferingPriceRef.setHref(productOfferingPriceRefOrValueVO.getHref());
		productOfferingPriceRef.setName(productOfferingPriceRefOrValueVO.getName());
		return productOfferingPriceRef;
	}

	default OrganizationParentRelationshipVO map(OrganizationParentRelationship organizationParentRelationship) {
		if (organizationParentRelationship == null) {
			return null;
		}
		OrganizationParentRelationshipVO organizationParentRelationshipVO = new OrganizationParentRelationshipVO();
		organizationParentRelationshipVO.setRelationshipType(organizationParentRelationship.getRelationshipType());
		organizationParentRelationshipVO.setAtBaseType(organizationParentRelationship.getAtBaseType());
		organizationParentRelationshipVO.setAtSchemaLocation(organizationParentRelationship.getAtSchemaLocation());
		organizationParentRelationshipVO.setAtType(organizationParentRelationship.getAtType());
		organizationParentRelationshipVO.setOrganization(new OrganizationRefVO().id(organizationParentRelationship.getId().toString()));
		return organizationParentRelationshipVO;
	}

	default OrganizationChildRelationshipVO map(OrganizationChildRelationship organizationChildRelationship) {
		if (organizationChildRelationship == null) {
			return null;
		}
		OrganizationChildRelationshipVO organizationChildRelationshipVO = new OrganizationChildRelationshipVO();
		organizationChildRelationshipVO.setRelationshipType(organizationChildRelationship.getRelationshipType());
		organizationChildRelationshipVO.setAtBaseType(organizationChildRelationship.getAtBaseType());
		organizationChildRelationshipVO.setAtSchemaLocation(organizationChildRelationship.getAtSchemaLocation());
		organizationChildRelationshipVO.setAtType(organizationChildRelationship.getAtType());
		organizationChildRelationshipVO.setOrganization(new OrganizationRefVO().id(organizationChildRelationship.getId().toString()));
		return organizationChildRelationshipVO;
	}

	default OrganizationParentRelationship map(OrganizationParentRelationshipVO organizationParentRelationshipVO) {
		if (organizationParentRelationshipVO == null) {
			return null;
		}
		OrganizationParentRelationship organizationParentRelationship = new OrganizationParentRelationship(organizationParentRelationshipVO.getOrganization().id());
		organizationParentRelationship.setRelationshipType(organizationParentRelationshipVO.getRelationshipType());
		organizationParentRelationship.setAtType(organizationParentRelationshipVO.getAtType());
		organizationParentRelationship.setAtSchemaLocation(organizationParentRelationshipVO.getAtSchemaLocation());
		organizationParentRelationship.setAtBaseType(organizationParentRelationshipVO.getAtBaseType());
		return organizationParentRelationship;
	}

	default OrganizationChildRelationship map(OrganizationChildRelationshipVO organizationChildRelationshipVO) {
		if (organizationChildRelationshipVO == null) {
			return null;
		}
		OrganizationChildRelationship organizationChildRelationship = new OrganizationChildRelationship(organizationChildRelationshipVO.getOrganization().id());
		organizationChildRelationship.setRelationshipType(organizationChildRelationshipVO.getRelationshipType());
		organizationChildRelationship.setAtType(organizationChildRelationshipVO.getAtType());
		organizationChildRelationship.setAtSchemaLocation(organizationChildRelationshipVO.getAtSchemaLocation());
		organizationChildRelationship.setAtBaseType(organizationChildRelationshipVO.getAtBaseType());
		return organizationChildRelationship;
	}

	TimePeriod map(TimePeriodVO value);

	default URL map(String value) {
		if (value == null) {
			return null;
		}
		try {
			return new URL(value);
		} catch (MalformedURLException e) {
			throw new MappingException(String.format("%s is not a URL.", value), e);
		}
	}

	default String map(URL value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	default URI mapToURI(String value) {
		if (value == null) {
			return null;
		}
		return URI.create(value);
	}

	default String mapFromURI(URI value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}
}


